package com.keeply.api.image.service

import com.keeply.api.image.dto.ImageRequestDTO
import com.keeply.api.image.dto.ImageResponseDTO
import com.keeply.api.image.validator.ImageValidator
import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.entity.Image
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.tag.entity.Tag
import com.keeply.domain.tag.repository.TagRepository
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.redis.RedisService
import com.keeply.global.s3.S3Service
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.Base64

@Service
@Transactional
class ImageService (
    private val imageRepository: ImageRepository,
    private val folderRepository: FolderRepository,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository,
    private val redisService: RedisService,
    private val imageValidator: ImageValidator,
    private val s3Service: S3Service
) {
    fun saveCachedImage(userId: Long, requestDTO: ImageRequestDTO.SaveRequestDTO): ApiResponse<ImageResponseDTO.SaveResponseDTO> {
        imageValidator.validateSaveRequest(requestDTO)
        val cachedImageId = requestDTO.cachedImageId
        val imageInsight = requestDTO.imageInsight
        val folderId = requestDTO.folderId
        val tagName = requestDTO.tag

        val folder = getFolder(userId, folderId)
        val user = getUser(userId)
        val tag = tagRepository.findByName(tagName)
            ?: tagRepository.save(Tag(name = tagName))

        val cachedOcrImage = redisService.getCachedImage(cachedImageId)

        val base64Image = cachedOcrImage.base64Image
        val detectedText = cachedOcrImage.detectedText

        var image = Image(
            insight = imageInsight,
            user = user,
            folder = folder,
            tag = tag
        )

        imageRepository.save(image)

        val s3Key = s3Service.uploadBase64Image(userId, image.id!!, base64Image)

        image.s3Key = s3Key

        return ApiResponse<ImageResponseDTO.SaveResponseDTO> (
            success = true,
            data = ImageResponseDTO.SaveResponseDTO(
                imageId = image.id!!
            )
        )
    }

    fun setFolderOfImage(userId: Long, requestDTO: ImageRequestDTO.SaveRequestDTO): ApiResponse<ImageResponseDTO.SaveResponseDTO> {
        imageValidator.validateSaveRequest(requestDTO)
        val imageId = requestDTO.imageId
        val imageInsight = requestDTO.imageInsight
        val folderId = requestDTO.folderId
        val tagName = requestDTO.tag

        val folder = getFolder(userId, folderId)

        val image = getImage(imageId!!, userId)

        val tag = tagRepository.findByName(tagName)
            ?: tagRepository.save(Tag(name = tagName))

        image.insight = imageInsight
        image.folder = folder
        image.tag = tag

        return ApiResponse<ImageResponseDTO.SaveResponseDTO>(
            success = true,
            data = ImageResponseDTO.SaveResponseDTO(
                imageId = imageId
            )
        )
    }

    fun saveUncategorizedImage(userId: Long, file: MultipartFile): ApiResponse<ImageResponseDTO.SaveResponseDTO> {
        imageValidator.validateImage(file)
        val user = getUser(userId)
        val image = Image(
            insight = null,
            user = user,
            folder = null,
            tag = null
        )
        imageRepository.save(image)
        val base64Image = Base64.getEncoder().encodeToString(file.bytes)
        val s3Key = s3Service.uploadBase64Image(userId, image.id!!, base64Image)
        image.s3Key = s3Key
        return ApiResponse<ImageResponseDTO.SaveResponseDTO>(
            success = true,
            data = ImageResponseDTO.SaveResponseDTO(
                imageId = image.id!!,
            )
        )
    }

    fun moveImage(userId: Long, imageId: Long, requestDTO: ImageRequestDTO.MoveImageRequestDTO): ApiResponse<ImageResponseDTO.MoveImageResponseDTO> {
        imageValidator.validateMoveRequest(requestDTO)
        val folder = getFolder(userId, requestDTO.folderId)
        val image = getImage(imageId, userId)
        image.folder = folder
        return ApiResponse<ImageResponseDTO.MoveImageResponseDTO>(
            success = true,
            data = ImageResponseDTO.MoveImageResponseDTO(
                imageId = image.id!!,
                folderId = folder.id!!,
            )
        )
    }

    private fun getUser(userId: Long): User = (userRepository.findUserById(userId)
        ?: throw Exception("존재하지 않는 유저입니다."))


    private fun getImage(imageId: Long, userId: Long): Image = imageRepository.findImageByIdAndUserId(imageId,userId)
        ?: throw Exception("imageId에 해당하는 이미지가 없습니다.")


    private fun getFolder(userId: Long, folderId: Long): Folder = (folderRepository.findByUserIdAndId(userId, folderId)
        ?: throw Exception("폴더를 찾을 수 없습니다."))



}