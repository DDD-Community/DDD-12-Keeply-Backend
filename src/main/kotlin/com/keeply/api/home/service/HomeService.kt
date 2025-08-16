package com.keeply.api.home.service

import com.keeply.api.home.dto.FolderInfo
import com.keeply.api.home.dto.HomeResponseDTO
import com.keeply.api.home.dto.ImageInfo
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.global.aws.s3.S3Service
import com.keeply.global.dto.ApiResponse
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HomeService(
    private val imageRepository: ImageRepository,
    private val folderRepository: FolderRepository,
    private val s3Service: S3Service
) {
    fun getHome(userId: Long): ApiResponse<HomeResponseDTO> {
        val imagesOrderByUpdatedAtDesc = imageRepository.findAllByUserIdOrderByUpdatedAtDesc(userId)
        val foldersOrderByUpdatedAtDesc = folderRepository.findAllByUserIdOrderByUpdatedAtDesc(userId)

        var uncategorizedImageList: List<ImageInfo> = imagesOrderByUpdatedAtDesc
            .mapNotNull{
                image ->
                if(image.folder == null) {
                    ImageInfo(
                        imageId = image.id!!,
                        presignedUrl = s3Service.generatePresignedUrl(image.s3Key!!),
                        tag = image.tag?.name,
                        insight = image.insight,
                        updatedAt = image.updatedAt
                    )
                } else null
            }

        val uncategorizedImageCount = uncategorizedImageList.size
        uncategorizedImageList = uncategorizedImageList.take(3)

        var scheduledToDeleteImageList: List<ImageInfo> = imagesOrderByUpdatedAtDesc
            .mapNotNull{
                image ->
                if(!image.isCategorized && image.scheduledDeleteAt!!.toLocalDate() == LocalDate.now()) {
                    ImageInfo(
                        imageId = image.id!!,
                        presignedUrl = s3Service.generatePresignedUrl(image.s3Key!!),
                        tag = image.tag?.name,
                        insight = image.insight,
                        updatedAt = image.updatedAt,
                    )
                } else null
            }

        val scheduledToDeleteImageCount = scheduledToDeleteImageList.size
        scheduledToDeleteImageList = scheduledToDeleteImageList.take(3)

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
                    folderName = folder.name,
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
                imageCount = imagesOrderByUpdatedAtDesc.size,
                uncategorizedImageCount = uncategorizedImageCount,
                uncategorizedImageList = uncategorizedImageList,
                scheduledToDeleteImageCount = scheduledToDeleteImageCount,
                scheduledToDeleteImageList = scheduledToDeleteImageList,
                recentImages = recentImages,
                recentFolders = recentFolders,
                recentSavedImages = recentSavedImages,
            )
        )
    }
}