package com.keeply.api.home.service

import com.keeply.api.home.dto.FolderInfo
import com.keeply.api.home.dto.HomeResponseDTO
import com.keeply.api.home.dto.ImageInfo
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.global.aws.s3.S3Service
import com.keeply.global.dto.ApiResponse
import org.springframework.stereotype.Service

@Service
class HomeService(
    private val imageRepository: ImageRepository,
    private val folderRepository: FolderRepository,
    private val s3Service: S3Service
) {
    fun getHome(userId: Long): ApiResponse<HomeResponseDTO> {
        val imagesOrderByUpdatedAtDesc = imageRepository.findAllByUserIdOrderByUpdatedAtDesc(userId)
        val foldersOrderByUpdatedAtDesc = folderRepository.findAllByUserIdOrderByUpdatedAtDesc(userId)

        val imageCount = imagesOrderByUpdatedAtDesc.size
        val recentImages = imagesOrderByUpdatedAtDesc
            .take(3)
            .map{ image ->
                ImageInfo(
                    imageId = image.id!!,
                    presignedUrl = s3Service.generatePresignedUrl(image.s3Key!!),
                    tag = image.tag?.name,
                    insight = image.insight,
                    updatedAt = image.updatedAt,
                )
            }
        val recentFolders = foldersOrderByUpdatedAtDesc
            .take(3)
            .map{ folder ->
                FolderInfo(
                    folderId = folder.id!!,
                    color = folder.color,
                    updatedAt = folder.updatedAt,
                    imageCount = folder.images.count()
                )
            }
        val recentSavedImages = imagesOrderByUpdatedAtDesc
            .filter { it.folder != null }
            .take(3)
            .map{ image ->
                ImageInfo(
                    imageId = image.id!!,
                    presignedUrl = s3Service.generatePresignedUrl(image.s3Key!!),
                    tag = image.tag?.name,
                    insight = image.insight,
                    updatedAt = image.updatedAt,
                )
            }

        return ApiResponse<HomeResponseDTO>(
            success = true,
            response = HomeResponseDTO(
                userId = userId,
                imageCount = imageCount.toLong(),
                recentImages = recentImages,
                recentFolders = recentFolders,
                recentSavedImages = recentSavedImages,
            )
        )
    }
}