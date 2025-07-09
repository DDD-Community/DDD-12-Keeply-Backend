package com.keeply.api.folder.dto

class FolderResponseDTO {
    data class Create(
        val folderId: Long? = null,
        val folderName: String
    )
}
