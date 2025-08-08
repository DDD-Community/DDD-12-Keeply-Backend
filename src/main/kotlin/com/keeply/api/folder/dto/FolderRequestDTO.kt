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
    data class GetFoldersRequestDTO(
        @Schema(description = "폴더 검색 키워드")
        val keyword: String?,
        @Schema(description = "폴더 정렬 기준")
        val sortBy: String,
        @Schema(description = "내림차순(desc)/ 오름차순(asc)")
        val orderBy: String
    )
}