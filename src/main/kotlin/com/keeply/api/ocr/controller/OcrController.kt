package com.keeply.api.ocr.controller

import com.keeply.api.ocr.dto.OcrRequestDTO
import com.keeply.api.ocr.dto.OcrResponseDTO
import com.keeply.api.ocr.service.OcrService
import com.keeply.global.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ocr")
class OcrController (
    private val ocrService: OcrService
) {
    @PostMapping("/analyze", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun analyze(
        @ModelAttribute requestDTO: OcrRequestDTO.analyze
    ): ResponseEntity<ApiResponse<OcrResponseDTO>> {
        val apiResponse = ocrService.analyzeImage(requestDTO.file)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }
}