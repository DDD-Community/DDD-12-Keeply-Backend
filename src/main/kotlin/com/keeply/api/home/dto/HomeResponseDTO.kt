package com.keeply.api.home.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class HomeResponseDTO(
    val userId: Long,
    val imageCount: Int,
    val uncategorizedImageCount: Int,
    val uncategorizedImageList: List<ImageInfo>,
    val scheduledToDeleteImageCount: Int,
    val scheduledToDeleteImageList: List<ImageInfo>,
    val recentImages: List<ImageInfo>,
    val recentFolders: List<FolderInfo>,
    val recentSavedImages: List<ImageInfo>
)

data class ImageInfo(
    val imageId: Long,
    val presignedUrl: String,
    val tag: String? = null,
    val insight: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime? = null,
)

data class FolderInfo(
    val folderId: Long,
    val color: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime? = null,
    val imageCount: Int
)

