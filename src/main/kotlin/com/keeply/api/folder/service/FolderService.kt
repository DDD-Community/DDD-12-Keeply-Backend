package com.keeply.api.folder.service

import com.keeply.api.folder.dto.FolderResponseDTO
import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class FolderService (
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository
) {
    fun createFolder(userId: Long, folderName: String): ApiResponse<FolderResponseDTO.Folder> {
        val user = getUser(userId)
            ?: return ApiResponse(
                success = false,
                message = "존재하지 않는 유저입니다."
            )

        var folder = getFolderByUserIdAndFolderName(userId, folderName)
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
            data = FolderResponseDTO.Folder(
                folder.id,
                folder.name
            )
        )
    }

    fun getFolders(userId: Long): ApiResponse<FolderResponseDTO.FolderList> {
        val folderList = getFolderListByUserId(userId)
        val result = folderList.map { folder ->
            FolderResponseDTO.Folder(folder.id, folder.name)
        }
        return ApiResponse<FolderResponseDTO.FolderList>(
            success = true,
            data = FolderResponseDTO.FolderList(
                result
            )
        )
    }

    fun getFolderImages(userId: Long, folderId: Long): ApiResponse<FolderResponseDTO.FolderImages> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        if(folder == null) {
            return ApiResponse(
                success = false,
                message = "존재하지 않는 폴더입니다."
            )
        }

        val result = folder.images.map { image ->
            FolderResponseDTO.ImageInfo(image.id, image.presignedUrl, image.tag.name)
        }

        return ApiResponse<FolderResponseDTO.FolderImages>(
            success = true,
            data = FolderResponseDTO.FolderImages(
                result
            )
        )
    }

    fun updateFolder(userId: Long, folderId: Long, folderName: String): ApiResponse<FolderResponseDTO.Folder> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        if(folder == null) {
            return ApiResponse(
                success = false,
                message = "존재하지 않는 폴더입니다."
            )
        }
        folder.updateFolderName(folderName)
        return ApiResponse<FolderResponseDTO.Folder>(
            success = true,
            data = FolderResponseDTO.Folder(
                folder.id,
                folder.name
            )
        )
    }

    fun deleteFolder(userId: Long, folderId: Long): ApiResponse<Message> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        if(folder == null) {
            return ApiResponse(
                success = false,
                message = "존재하지 않는 폴더입니다."
            )
        }
        folderRepository.delete(folder)
        return ApiResponse<Message>(
            success = true,
            data = Message(
                "${folder.name} 가 삭제되었습니다."
            )
        )
    }

    private fun getFolderByUserIdAndFolderId(userId: Long, folderId: Long): Folder? =
        folderRepository.findByUserIdAndId(userId, folderId)

    private fun getFolderListByUserId(userId: Long): List<Folder> = folderRepository.findAllByUserId(userId)

    private fun getFolderByUserIdAndFolderName(userId: Long, folderName: String): Folder? =
        folderRepository.findByUserIdAndName(userId, folderName)

    private fun getUser(userId: Long): User? =
        userRepository.findById(userId).get()
}