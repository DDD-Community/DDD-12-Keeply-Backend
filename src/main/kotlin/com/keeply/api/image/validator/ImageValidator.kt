package com.keeply.api.image.validator

import com.keeply.api.image.dto.ImageRequestDTO.MoveImageRequestDTO
import com.keeply.api.image.dto.ImageRequestDTO.SaveRequestDTO
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class ImageValidator {
    fun validateImage(file: MultipartFile) {
        if(file.size > 1_048_576) {
            throw Exception("이미지 파일의 크기는 최대 1MB로 제한됩니다.")
        }
    }

    fun validateSaveRequest(request: SaveRequestDTO) {
        if (request.folderId <= 0) {
            throw IllegalArgumentException("folderId는 0보다 커야 합니다.")
        }

        if (request.isCached) {
            if (request.cachedImageId.isNullOrBlank()) {
                throw IllegalArgumentException("isCached가 true일 경우 cachedImageId는 필수입니다.")
            }
        } else {
            if (request.imageId == null) {
                throw IllegalArgumentException("isCached가 false일 경우 imageId는 필수입니다.")
            }
        }
    }

    fun validateMoveRequest(request: MoveImageRequestDTO) {
        if (request.folderId <= 0) {
            throw IllegalArgumentException("folderId는 0보다 커야 합니다.")
        }
    }
    fun validateDeleteRequest(userId: Long, imageId: Long) {
        if(imageId <= 0) {
            throw IllegalArgumentException("imgaeId는 0보다 커야 합니다.")
        }
        if(userId <= 0) {
            throw IllegalArgumentException("userId는 0보다 커야 합니다.")
        }
    }
}