package com.keeply.api.folder.validator

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.global.common.Constants
import org.springframework.stereotype.Component

@Component
class FolderValidator {
    private val allowedColors = Constants.Colors.FOLDER_COLORS
    private val allowedSortBy = setOf("updatedAt", "imageCount")
    private val allowedOrderBy = setOf("asc", "desc")
    fun validateCreate(requestDTO: FolderRequestDTO.CreateRequestDTO) {
        require(requestDTO.folderName.isNotBlank()) { "폴더명은 공백일 수 없습니다." }
        require(requestDTO.folderName.length <= 20) { "폴더명은 20자로 제한됩니다." }
    }

    fun validateUpdate(requestDTO: FolderRequestDTO.UpdateRequestDTO) {
        require(requestDTO.folderName.isNotBlank()) { "폴더명은 공백일 수 없습니다." }
        require(requestDTO.folderName.length <= 20) { "폴더명은 20자로 제한됩니다." }
    }

    fun validateGetFolders(requestDTO: FolderRequestDTO.GetFoldersRequestDTO) {
        require(requestDTO.sortBy in allowedSortBy) {
            "정렬기준(sortBy)은 ${allowedSortBy.joinToString(", ")} 중 하나여야 합니다."
        }
        require(requestDTO.orderBy in allowedOrderBy) {
            "정렬방향(orderBy)은 ${allowedOrderBy.joinToString(", ")} 중 하나여야 합니다."
        }
    }
}