package com.keeply.api.ocr.dto

import org.springframework.web.multipart.MultipartFile

class OcrRequestDTO{
    data class analyze(
        val file : MultipartFile
    )
}
