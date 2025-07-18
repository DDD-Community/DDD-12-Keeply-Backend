package com.keeply.api.folder.dto

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
        val imageList: List<ImageInfo>? = null
    )

    data class ImageInfo(
        val imageId: Long? = null,
        val presignedUrl: String? = null,
        val tag: String? = null
    )
}
