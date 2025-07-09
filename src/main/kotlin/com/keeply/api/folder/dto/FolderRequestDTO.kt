package com.keeply.api.folder.dto

import jakarta.validation.constraints.NotBlank

class FolderRequestDTO{
    data class Create (
        val userId: Long,
        val folderName: String
    )
}
