package com.keeply.api.folder.service

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.api.folder.dto.FolderRequestDTO.GetFoldersRequestDTO
import com.keeply.api.folder.dto.FolderResponseDTO
import com.keeply.api.folder.validator.FolderValidator
import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.folder.repository.FolderRepository
import com.keeply.domain.image.entity.Image
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.image.service.ImageDomainService
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.aws.s3.S3Service
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

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
        if(folder != null) throw Exception("이미 존재하는 폴더입니다.")
        folder = Folder.builder()
            .name(setFolderName(folderName,userId))
            .color(color)
            .user(user)
            .build()

        folder = folderRepository.save(folder)

        return ApiResponse(
            success = true,
            response = FolderResponseDTO.Folder(
                folderId = folder.id!!,
                folderName = folder.name,
                color = folder.color,
                imageCount = folder.images.size,
                updatedAt = folder.updatedAt,
                isDuplicate = folder.name != folderName,
                duplicatedMessage = if(folder.name != folderName) "이미 존재하는 폴더명 입니다. \'${folder.name}\'로 추가되었습니다."
                else "",
            )
        )
    }

    fun getFolders(userId: Long, requestDTO: GetFoldersRequestDTO): ApiResponse<FolderResponseDTO.FolderList> {
        folderValidator.validateGetFolders(requestDTO)

        val keyword = requestDTO.keyword
        val sortBy = requestDTO.sortBy
        val orderBy = requestDTO.orderBy

        val folderList = getFolderListByUserId(userId, sortBy, orderBy)
        val result = folderList
            .filter { folder ->
                keyword?.let {
                    folder.name.contains(it, ignoreCase = true)
                } ?: true
            }
            .map { folder ->
                FolderResponseDTO.Folder(
                    folder.id!!,
                    folder.name,
                    folder.color,
                    folder.images.size,
                    folder.updatedAt
                )
            }
        return ApiResponse(
            success = true,
            response = FolderResponseDTO.FolderList(result)
        )
    }

    fun getFolderImages(userId: Long, folderId: Long): ApiResponse<FolderResponseDTO.FolderImages> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)

        val result = folder.images.map { image ->
            FolderResponseDTO.ImageInfo(
                imageId = image.id!!,
                presignedUrl = s3Service.generatePresignedUrl(image.s3Key!!),
                insight = image.insight,
                tag = image.folder?.name,
                tagColor = image.folder!!.color,
                isCategorized = image.isCategorized,
                scheduledDeleteAt = image.scheduledDeleteAt,
                updatedAt = image.updatedAt
            )
        }

        return ApiResponse<FolderResponseDTO.FolderImages>(
            success = true,
            response = FolderResponseDTO.FolderImages(
                result
            )
        )
    }

    fun getUncategorizedImages(userId: Long): ApiResponse<FolderResponseDTO.FolderImages> {
        val images = getImages(userId)

        val result = images.map { image ->
            val daysUntilDeletion = Duration.between(LocalDateTime.now(), image.scheduledDeleteAt).toDays()
            FolderResponseDTO.ImageInfo(
                image.id!!,
                s3Service.generatePresignedUrl(image.s3Key!!),
                image.insight,
                image.folder?.name,
                null,
                image.isCategorized,
                image.scheduledDeleteAt,
                daysUntilDeletion,
                image.updatedAt
            )
        }

        return ApiResponse<FolderResponseDTO.FolderImages>(
            success = true,
            response = FolderResponseDTO.FolderImages(
                result
            )
        )
    }

    private fun getImages(userId: Long): List<Image> = imageRepository.findAllByUserIdAndFolderIsNull(userId)

    fun updateFolder(userId: Long, folderId: Long, requestDTO: FolderRequestDTO.UpdateRequestDTO): ApiResponse<FolderResponseDTO.Folder> {
        folderValidator.validateUpdate(requestDTO)
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        folder.name = setFolderName(requestDTO.folderName, userId)
        folder.color = requestDTO.color
        return ApiResponse<FolderResponseDTO.Folder>(
            success = true,
            response = FolderResponseDTO.Folder(
                folder.id!!,
                folder.name,
                folder.color,
                folder.images.size,
                folder.updatedAt,
                isDuplicate = folder.name != requestDTO.folderName,
                duplicatedMessage = if(folder.name != requestDTO.folderName) "이미 존재하는 폴더명 입니다. \'${folder.name}\'로 추가되었습니다."
                else "",
            )
        )
    }

    fun deleteFolder(userId: Long, folderId: Long): ApiResponse<Message> {
        val folder = getFolderByUserIdAndFolderId(userId, folderId)
        imageDomainService.deleteAllImagesInFolder(folder)
        folderRepository.delete(folder)
        return ApiResponse<Message>(
            success = true,
            response = Message(
                "${folder.name} 가 삭제되었습니다."
            )
        )
    }

    private fun getFolderByUserIdAndFolderId(userId: Long, folderId: Long): Folder =
        folderRepository.findByUserIdAndId(userId, folderId)
            ?: throw Exception("존재하지 않는 폴더입니다.")

    private fun getFolderListByUserId(userId: Long, sortBy: String, orderBy: String): List<Folder> {
        val folderList = folderRepository.findAllByUserId(userId)
        var folders = when(sortBy) {
            "updatedAt" -> if(orderBy == "asc") folderList.sortedBy{ it.updatedAt } else folderList.sortedByDescending { it.updatedAt }
            "imageCount" -> if(orderBy == "asc") folderList.sortedBy{ it.images.size } else folderList.sortedByDescending { it.images.size }
            else -> folderList
        }
        return folders
    }

    private fun getFolderByUserIdAndFolderName(userId: Long, folderName: String): Folder? =
        folderRepository.findByUserIdAndName(userId, folderName)


    private fun getUser(userId: Long): User? =
        userRepository.findById(userId).get()

    private fun setFolderName(folderName: String, userId: Long): String {
        val folderCount = folderRepository.countByUserIdAndName(userId, folderName)
        if(folderCount>0) return folderName + (folderCount+1).toString()
        else return folderName
    }
}