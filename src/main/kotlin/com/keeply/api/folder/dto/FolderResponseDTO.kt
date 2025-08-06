package com.keeply.api.folder.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

class FolderResponseDTO {
    data class Folder(
        @Schema(description = "폴더 Id")
        val folderId: Long,
        @Schema(description = "폴더명")
        val folderName: String,
        @Schema(description = "폴더 색상 코드")
        val color: String,
        @Schema(description = "폴더내 이미지 개수")
        val imageCount: Int,
        @Schema(description = "폴더 최근 업데이트 시각")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val updatedAt: LocalDateTime? = null,
    )

    @Schema(name = "Folder_ImageInfo", description = "folderAPI_ImageInfo")
    data class ImageInfo(
        @Schema(description = "이미지 Id")
        val imageId: Long,
        @Schema(description = "이미지 presignedUrl")
        val presignedUrl: String? = null,
        @Schema(description = "이미지 인사이트")
        val insight: String? = null,
        @Schema(description = "태그")
        val tag: String? = null,
        @Schema(description = "폴더 저장, 미분류 여부")
        val isCategorized: Boolean,
        @Schema(description = "미분류 이미지 삭제 예정 시각")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        val scheduledDeleteAt: LocalDate? = null,
        @Schema(description = "미분류 이미지 보관 남은 일수")
        val daysUntilDeletion: Long? = null,
    )

    data class FolderList(
        @Schema(description = "폴더 리스트")
        val folderList: List<Folder>
    )

    data class FolderImages(
        @Schema(description = "이미지 리스트")
        val imageList: List<ImageInfo>
    )

}
