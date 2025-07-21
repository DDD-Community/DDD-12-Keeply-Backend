package com.keeply.api.ocr.dto

import org.springframework.web.multipart.MultipartFile

class OcrRequestDTO{
    data class Analyze(
        val isNew: Boolean,
        val imageId: Long? = null
    )
}
