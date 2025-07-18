package com.keeply.api.image.service

import com.keeply.api.image.dto.ImageRequestDTO
import com.keeply.api.image.dto.ImageResponseDTO
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.entity.Image
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.tag.entity.Tag
import com.keeply.domain.tag.repository.TagRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.redis.RedisService
import com.keeply.global.s3.S3Service
import jakarta.transaction.Transactional
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service

@Service
@Transactional
@Slf4j
class ImageService (
    private val imageRepository: ImageRepository,
    private val folderRepository: FolderRepository,
    private val tagRepository: TagRepository,
    private val redisService: RedisService,
    private val s3Service: S3Service
) {
    fun saveCachedImage(userId: Long, requestDTO: ImageRequestDTO.Save): ApiResponse<ImageResponseDTO.Save> {
        val cachedImageId = requestDTO.cachedImageId
        val imageInsight = requestDTO.imageInsight
        val folderId = requestDTO.folderId
        val tagName = requestDTO.tag

        println("$imageInsight")

        val folder = folderRepository.findByUserIdAndId(userId, folderId)
            ?: throw Exception("폴더를 찾을 수 없습니다.")

        val tag = tagRepository.findByName(tagName)
            ?: tagRepository.save(Tag(name = tagName))

        val cachedOcrImage = redisService.getCachedImage("image:$cachedImageId")

        val base64Image = cachedOcrImage.base64Image
        val detectedText = cachedOcrImage.detectedText

        var image = Image(
            insight = imageInsight,
            folder = folder,
            tag = tag
        )

        val s3Key = s3Service.uploadBase64Image(userId, image.id!!, base64Image)

        image.s3Key = s3Key

        return ApiResponse<ImageResponseDTO.Save> (
            success = true,
            data = ImageResponseDTO.Save(
                imageId = image.id!!
            )
        )
    }

    fun setFolderOfImage(userId: Long, requestDTO: ImageRequestDTO.Save): ApiResponse<ImageResponseDTO.Save> {
        val imageId = requestDTO.imageId
            ?: throw Exception("이미 s3저장된 이미지는 imageId가 필수입니다.")
        val imageInsight = requestDTO.imageInsight
        val folderId = requestDTO.folderId
        val tagName = requestDTO.tag

        val folder = folderRepository.findByUserIdAndId(userId, folderId)
            ?: throw Exception("폴더를 찾을 수 없습니다.")

        val image = imageRepository.findById(imageId)
            .orElseThrow { Exception("imageId에 해당하는 이미지가 없습니다.") }

        val tag = tagRepository.findByName(tagName)
            ?: tagRepository.save(Tag(name = tagName))

//        image.setInsight(imageInsight)
//        image.setFolder(folder)
//        image.setTag(tag)

        image.insight = imageInsight
        image.folder = folder
        image.tag = tag

        return ApiResponse<ImageResponseDTO.Save>(
            success = true,
            data = ImageResponseDTO.Save(
                imageId = imageId
            )
        )
    }

}