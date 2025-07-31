package com.keeply.api.ocr.dto

import io.swagger.v3.oas.annotations.media.Schema

data class OcrResponseDTO(
    @Schema(description = """
            OCR 후 캐싱되어있는 이미지 ID
            신규 이미지 -> cachedImageId = {cachedImageId}
            미분류 이미지 -> cachedImageId = null
        """
    )
    val cachedImageId: String? = null,
    @Schema(description = """
        추출된 텍스트
    """)
    var detectedText: String? = null,
    @Schema(description = """
        추천 태그
    """)
    var recommendedTags: List<String>? = null
)
