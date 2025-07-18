package com.keeply.api.ocr.controller

import com.keeply.api.ocr.dto.OcrRequestDTO
import com.keeply.api.ocr.dto.OcrResponseDTO
import com.keeply.api.ocr.service.OcrService
import com.keeply.global.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ocr")
class OcrController (
    private val ocrService: OcrService
) {
    @PostMapping("/analyze", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun analyze(
        @RequestPart requestDTO: OcrRequestDTO.Analyze
    ): ResponseEntity<ApiResponse<OcrResponseDTO>> {
        try {
            val apiResponse = if (requestDTO.isNew) {
                ocrService.analyzeNewImage(requestDTO)
            } else {
                ocrService.analyzeSavedImage(requestDTO)
            }
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val ApiResponse = ApiResponse<OcrResponseDTO>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse)
        }
    }
}