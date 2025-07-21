package com.keeply.api.folder.validator

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.global.common.Constants
import org.springframework.stereotype.Component

@Component
class FolderValidator {
    private val allowedColors = Constants.Colors.FOLDER_COLORS

    fun validateCreate(requestDTO: FolderRequestDTO.CreateRequestDTO) {
        when {
            requestDTO.folderName.isBlank() -> throw Exception("폴더명은 공백일 수 없습니다.")
            requestDTO.folderName.length > 20 -> throw Exception("폴더명은 20자로 제한됩니다.")
            !allowedColors.contains(requestDTO.color) -> throw Exception("지원하지 않는 폴더 색상입니다.")
        }
    }

    fun validateUpdate(requestDTO: FolderRequestDTO.UpdateRequestDTO) {
        when {
            requestDTO.folderName.isBlank() -> throw Exception("폴더명은 공백일 수 없습니다.")
            requestDTO.folderName.length > 20 -> throw Exception("폴더명은 20자로 제한됩니다.")
            !allowedColors.contains(requestDTO.color) -> throw Exception("지원하지 않는 폴더 색상입니다.")
        }
    }
}