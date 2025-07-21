package com.keeply.api.ocr.validator

import com.keeply.api.ocr.dto.OcrRequestDTO.Analyze
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class OcrValidator {
    fun validateImageFile(file: MultipartFile?) {
        file ?: throw Exception("Image 파일을 찾을 수 없습니다.")
        if(file.size > 1_048_576) {
            throw Exception("이미지 파일의 크기는 최대 1MB로 제한됩니다.")
        }
    }
    fun validateAnalyzeRequest(request: Analyze) {
        if (request.isNew) {
            if (request.imageId != null) {
                throw IllegalArgumentException("isNew가 true인 경우 imageId는 null이어야 합니다.")
            }
        } else {
            if (request.imageId == null) {
                throw IllegalArgumentException("isNew가 false인 경우 imageId는 필수입니다.")
            }
        }
    }
}