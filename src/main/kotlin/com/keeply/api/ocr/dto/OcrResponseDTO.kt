package com.keeply.api.ocr.dto

data class OcrResponseDTO(
    val cachedImageId: String? = null,
    var detectedText: String? = null,
    var recommendedTags: List<String>? = null
)
