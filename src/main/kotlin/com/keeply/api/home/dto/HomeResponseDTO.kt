package com.keeply.api.home.dto

import java.time.LocalDateTime

data class HomeResponseDTO(
    val userId: Long,
    val imageCount: Long,
    val recentImages: List<ImageInfo>,
    val recentFolders: List<FolderInfo>,
    val recentSavedImages: List<ImageInfo>
)

data class ImageInfo(
    val imageId: Long,
    val presignedUrl: String,
    val tag: String? = null,
    val insight: String? = null,
    val updatedAt: LocalDateTime
)

data class FolderInfo(
    val folderId: Long,
    val color: String,
    val updatedAt: LocalDateTime,
    val imageCount: Int
)

