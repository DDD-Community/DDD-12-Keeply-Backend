package com.keeply.api.folder.dto

class FolderRequestDTO{
    data class Create (
        val folderName: String,
        val color: String
    )
    data class Update (
        val folderName: String,
        val color: String
    )
}
