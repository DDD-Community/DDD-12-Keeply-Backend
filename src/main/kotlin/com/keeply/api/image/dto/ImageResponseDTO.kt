package com.keeply.api.image.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

class ImageResponseDTO {
    data class SaveResponseDTO(
        @Schema(description = "이미지 Id")
        val imageId: Long
    )

    data class MoveImageResponseDTO(
        @Schema(description = "이미지 Id")
        val imageId: Long,
        @Schema(description = "폴더 Id")
        val folderId: Long
    )

    data class ImageInfoDTO(
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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        val scheduledDeleteAt: LocalDateTime? = null,
        @Schema(description = "미분류 이미지 보관 남은 일수")
        val daysUntilDeletion: Long? = null,
    )
}