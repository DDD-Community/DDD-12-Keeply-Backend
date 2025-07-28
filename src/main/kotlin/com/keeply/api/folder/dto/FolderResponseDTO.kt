package com.keeply.api.folder.dto

import java.time.LocalDateTime

class FolderResponseDTO {
    data class Folder(
        val folderId: Long? = null,
        val folderName: String,
        val color: String
    )

    data class FolderList(
        val folderList: List<Folder>
    )

    data class FolderImages(
        val imageList: List<ImageInfo>
    )

    data class ImageInfo(
        val imageId: Long? = null,
        val presignedUrl: String? = null,
        val insight: String? = null,
        val tag: String? = null,
        val isCategorized: Boolean,
        val scheduledDeleteAt: LocalDateTime? = null,
        val daysUntilDeletion: Long? = null,
    )
}
