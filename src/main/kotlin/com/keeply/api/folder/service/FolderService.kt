package com.keeply.api.folder.service

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.api.folder.dto.FolderResponseDTO
import com.keeply.api.folder.validator.FolderValidator
import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.image.service.ImageDomainService
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import com.keeply.global.aws.s3.S3Service
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class FolderService (
    private val userRepository: UserRepository,
    private val folderRepository: FolderRepository,
    private val imageRepository: ImageRepository,
    private val imageDomainService: ImageDomainService,
    private val folderValidator: FolderValidator,
    private val s3Service: S3Service
) {
    fun createFolder(userId: Long, requestDTO: FolderRequestDTO.CreateRequestDTO): ApiResponse<FolderResponseDTO.Folder> {
        folderValidator.validateCreate(requestDTO)

        val folderName = requestDTO.folderName
        val color = requestDTO.color

        val user = getUser(userId)
            ?: throw Exception("존재하지 않는 유저입니다.")

        var folder = getFolderByUserIdAndFolderName(userId, folderName)
        if(folder != null) {
            throw Exception("이미 존재하는 폴더입니다.")
        }

        folder = Folder(name = folderName, color = color, user = user)
        folderRepository.save(folder)

        return ApiResponse(
            success = true,
            response = FolderResponseDTO.Folder(
                folder.id,
                folder.color,
                folder.name
            )
        )
    }

    fun getFolders(userId: Long): ApiResponse<FolderResponseDTO.FolderList> {
        val folderList = getFolderListByUserId(userId)
        val result = folderList.map { folder ->
            FolderResponseDTO.Folder(folder.id, folder.color, folder.name)
        }
        return ApiResponse<FolderResponseDTO.FolderList>(
            success = true,
            response = FolderResponseDTO.FolderList(
                result
            )
        )
    }

    fun getFolderImages(userId: Long, folderId: Long): ApiResponse<FolderResponseDTO.FolderImages> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        if(folder == null) {
            throw Exception("존재하지 않는 폴더입니다.")
        }

        val result = folder.images.map { image ->
            FolderResponseDTO.ImageInfo(image.id, s3Service.generatePresignedUrl(image.s3Key!!),image.insight, image.tag!!.name)
        }

        return ApiResponse<FolderResponseDTO.FolderImages>(
            success = true,
            response = FolderResponseDTO.FolderImages(
                result
            )
        )
    }

    fun getUncategorizedImages(userId: Long): ApiResponse<FolderResponseDTO.FolderImages> {
        val images = imageRepository.findAllByUserIdAndFolderIsNull(userId)
            ?: throw Exception("미분류 이미지가 없습니다.")
        val result = images.map { image ->
            FolderResponseDTO.ImageInfo(image.id, s3Service.generatePresignedUrl(image.s3Key!!))
        }

        return ApiResponse<FolderResponseDTO.FolderImages>(
            success = true,
            response = FolderResponseDTO.FolderImages(
                result
            )
        )
    }

    fun updateFolder(userId: Long, folderId: Long, requestDTO: FolderRequestDTO.UpdateRequestDTO): ApiResponse<FolderResponseDTO.Folder> {
        folderValidator.validateUpdate(requestDTO)
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        if(folder == null) {
            throw Exception("존재하지 않는 폴더입니다.")
        }
        folder.name = requestDTO.folderName
        folder.color = requestDTO.color
        return ApiResponse<FolderResponseDTO.Folder>(
            success = true,
            response = FolderResponseDTO.Folder(
                folder.id,
                folder.color,
                folder.name
            )
        )
    }

    fun deleteFolder(userId: Long, folderId: Long): ApiResponse<Message> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        if(folder == null) {
            throw Exception("존재하지 않는 폴더입니다.")
        }
        imageDomainService.deleteAllImagesInFolder(folder)
        folderRepository.delete(folder)
        return ApiResponse<Message>(
            success = true,
            response = Message(
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