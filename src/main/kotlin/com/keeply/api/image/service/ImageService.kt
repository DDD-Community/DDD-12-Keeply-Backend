package com.keeply.api.image.service

import com.keeply.api.image.dto.ImageRequestDTO
import com.keeply.api.image.dto.ImageResponseDTO
import com.keeply.api.image.validator.ImageValidator
import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.entity.Image
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.image.service.ImageDomainService
import com.keeply.domain.tag.entity.Tag
import com.keeply.domain.tag.repository.TagRepository
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.aws.s3.S3Service
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import com.keeply.global.redis.RedisService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.time.LocalDateTime
import java.util.Base64

@Service
@Transactional
class ImageService (
    private val imageRepository: ImageRepository,
    private val imageDomainService: ImageDomainService,
    private val folderRepository: FolderRepository,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository,
    private val redisService: RedisService,
    private val s3Service: S3Service,
    private val imageValidator: ImageValidator
) {
    fun saveCachedImage(userId: Long, requestDTO: ImageRequestDTO.SaveRequestDTO): ApiResponse<ImageResponseDTO.SaveResponseDTO> {
        imageValidator.validateSaveRequest(requestDTO)
        val cachedImageId = requestDTO.cachedImageId
        val imageInsight = requestDTO.imageInsight
        val folderId = requestDTO.folderId
        val tagName = requestDTO.tag

        val folder = getFolder(userId, folderId)
        val user = getUser(userId)
        val tag = getTag(tagName)

        val cachedOcrImage = redisService.getCachedImage(cachedImageId)

        val base64Image = cachedOcrImage.base64Image
        val detectedText = cachedOcrImage.detectedText

        val image = imageDomainService.saveImage(
            insight = imageInsight,
            user = user,
            folder = folder,
            tag = tag,
            base64Image = base64Image,
        )

        return ApiResponse<ImageResponseDTO.SaveResponseDTO> (
            success = true,
            response = ImageResponseDTO.SaveResponseDTO(
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

        val tag = getTag(tagName)

        image.insight = imageInsight
        image.folder = folder
        image.tag = tag
        image.isCategorized = true
        image.scheduledDeleteAt = null

        return ApiResponse<ImageResponseDTO.SaveResponseDTO>(
            success = true,
            response = ImageResponseDTO.SaveResponseDTO(
                imageId = imageId
            )
        )
    }

    private fun getTag(tagName: String): Tag = (tagRepository.findByName(tagName)
        ?: tagRepository.save(
            Tag.builder().name(tagName).build()
        ))

    fun saveUncategorizedImage(userId: Long, file: MultipartFile): ApiResponse<ImageResponseDTO.SaveResponseDTO> {
        imageValidator.validateImage(file)
        val base64Image = Base64.getEncoder().encodeToString(file.bytes)
        val user = getUser(userId)
        val image = imageDomainService.saveImage(
            insight = null,
            user = user,
            folder = null,
            tag = null,
            base64Image = base64Image,
        )
        image.isCategorized = false
        image.scheduledDeleteAt = image.createdAt!!.plusDays(30)
        return ApiResponse<ImageResponseDTO.SaveResponseDTO>(
            success = true,
            response = ImageResponseDTO.SaveResponseDTO(
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
            response = ImageResponseDTO.MoveImageResponseDTO(
                imageId = image.id!!,
                folderId = folder.id!!,
            )
        )
    }

    fun getImageInfo(userId: Long, imageId: Long): ApiResponse<ImageResponseDTO.ImageInfoDTO> {
        val image = getImage(imageId, userId)
        val daysUntilDeletion = if(image.isCategorized) null
        else Duration.between(LocalDateTime.now(), image.scheduledDeleteAt).toDays()
        return ApiResponse<ImageResponseDTO.ImageInfoDTO>(
            success = true,
            response = ImageResponseDTO.ImageInfoDTO(
                imageId = image.id!!,
                presignedUrl = s3Service.generatePresignedUrl(image.s3Key!!),
                insight = image.insight,
                tag = image.tag?.name,
                isCategorized = image.isCategorized,
                scheduledDeleteAt = image.scheduledDeleteAt,
                daysUntilDeletion = daysUntilDeletion
            )
        )
    }

    fun deleteImage(userId: Long, imageId: Long) : ApiResponse<Message> {
        imageValidator.validateDeleteRequest(userId,imageId)
        val image = getImage(imageId, userId)
        val user = getUser(userId)
        imageDomainService.deleteImage(user, image)

        return ApiResponse<Message>(
            success = true,
            response = Message(
                "$userId 유저의 $imageId 가 삭제되었습니다."
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