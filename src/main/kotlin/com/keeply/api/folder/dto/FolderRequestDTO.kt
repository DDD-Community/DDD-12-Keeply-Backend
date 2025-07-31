package com.keeply.api.folder.dto

import io.swagger.v3.oas.annotations.media.Schema

class FolderRequestDTO{
    data class CreateRequestDTO (
        @Schema(description = "폴더명")
        val folderName: String,
        @Schema(description = "폴더 색상 코드")
        val color: String
    )
    data class UpdateRequestDTO (
        @Schema(description = "폴더명")
        val folderName: String,
        @Schema(description = "폴더 색상 코드")
        val color: String
    )
}