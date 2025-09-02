package com.keeply.api.ocr.dto

import io.swagger.v3.oas.annotations.media.Schema

data class OcrRequestDTO(
    @Schema(description = """
        신규이미지 -> isNew = true,
        미분류이미지 -> isNew = false
    """)
    val isNew: Boolean,
    @Schema(description = """
        신규이미지 -> imageId = null,
        미분류이미지 -> imageId = {imageId}
    """)
    val imageId: Long? = null,
    @Schema(description = """
        OCR 스킵 후 cachedImageId만 받기 위한 isSkip
    """)
    val isSkip: Boolean,
)
