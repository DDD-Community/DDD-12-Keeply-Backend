package com.keeply.api.image.dto

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
        val presignedUrl: String,
        @Schema(description = "이미지 인사이트")
        val insight: String?,
        @Schema(description = "태그")
        val tag: String?,
        @Schema(description = """
            폴더 저장 여부
            ex)
            폴더에 저장된 이미지 -> isCategorized = true
            미분류 이미지 -> isCategorized = false
        """)
        val isCategorized: Boolean,
        @Schema(description = """
            미분류 이미지 삭제 예정 시각
        """)
        val scheduledDeleteAt: LocalDateTime?,
        @Schema(description = """
            미분류 이미지 보관 남은 일수
        """)
        val daysUntilDeletion: Long? = null
    )
}