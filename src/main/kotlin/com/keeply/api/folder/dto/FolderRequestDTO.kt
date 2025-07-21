package com.keeply.api.folder.dto

class FolderRequestDTO{
    data class CreateRequestDTO (
        val folderName: String,
        val color: String
    )
    data class UpdateRequestDTO (
        val folderName: String,
        val color: String
    )
}
