package com.keeply.api.ocr.controller

import com.keeply.api.ocr.dto.OcrRequestDTO
import com.keeply.api.ocr.dto.OcrResponseDTO
import com.keeply.api.ocr.service.OcrService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/ocr")
class OcrController (
    private val ocrService: OcrService
) {
    @PostMapping("/analyze", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "OCR을 통한 텍스트 추출 및 태그 추천 API",
        description =
            "isNew: true = 신규 이미지 \n" +
                    "isNew: false = 미분류 이미지 (imageId 필수)"
    )
    fun analyze(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @ModelAttribute requestDTO: OcrRequestDTO.Analyze,
        @RequestPart("file") file: MultipartFile?
    ): ResponseEntity<ApiResponse<OcrResponseDTO>> {
        try {
            val apiResponse = if (requestDTO.isNew) {
                ocrService.analyzeNewImage(requestDTO, file)
            } else {
                ocrService.analyzeSavedImage(userDetails.userId, requestDTO, file)
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