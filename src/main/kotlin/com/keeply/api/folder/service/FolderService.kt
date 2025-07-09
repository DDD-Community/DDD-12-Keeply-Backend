package com.keeply.api.folder.service

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.api.folder.dto.FolderResponseDTO
import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.dto.ApiResponse
import org.springframework.stereotype.Service

@Service
class FolderService (
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository
) {
    fun createFolder(requestDTO: FolderRequestDTO.Create): ApiResponse<FolderResponseDTO.Create> {
        val userId = requestDTO.userId
        val folderName = requestDTO.folderName

        val user = userRepository.findById(userId)
            .orElse(null) ?: return ApiResponse(
                success = false,
                message = "존재하지 않는 유저입니다."
            )

        var folder = folderRepository.findByUserIdAndName(userId, folderName)
        if(folder != null) {
            return ApiResponse(
                success = false,
                message = "이미 존재하는 폴더입니다."
            )
        }

        folder = Folder(name = folderName, user = user)
        folderRepository.save(folder)

        return ApiResponse(
            success = true,
            data = FolderResponseDTO.Create(
                folder.id,
                folder.name
            )
        )
    }
}