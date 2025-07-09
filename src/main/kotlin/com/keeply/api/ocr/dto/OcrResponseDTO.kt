package com.keeply.api.ocr.dto

data class OcrResponseDTO(
    var detectedText: String? = null,
    var recommendedTags: List<String>? = null
)
