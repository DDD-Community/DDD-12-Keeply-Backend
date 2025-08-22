package com.keeply.domain.image.service

import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.image.entity.Image
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.tag.entity.Tag
import com.keeply.domain.user.entity.User
import com.keeply.global.aws.s3.S3Service
import com.keeply.global.fcm.service.FcmService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ImageDomainService(
    private val imageRepository: ImageRepository,
    private val s3Service: S3Service,
    private val fcmService: FcmService
) {
    fun saveImage(insight: String?, user: User, folder: Folder?, tag: Tag?, base64Image: String): Image{

        val image = Image.builder()
            .insight(insight)
            .user(user)
            .folder(folder)
            .tag(tag)
            .build()
        imageRepository.save(image)

        val bytes = Base64.getDecoder().decode(base64Image)
        val s3Key = s3Service.uploadBase64Image(user.id, image.id!!, bytes)

        image.s3Key = s3Key
        image.size += bytes.size

        user.usedStorageSize = user.usedStorageSize + bytes.size

        if (user.usedStorageSize >= user.storageLimit*0.8) {
            fcmService.sendStorageLimitNotification(user)
        }

        return image
    }

    fun deleteImage(user: User, image: Image) {
        s3Service.deleteImage(image.id!!, user.id)
        user.usedStorageSize = maxOf(0L, user.usedStorageSize - image.size)
        imageRepository.delete(image)
    }

    fun deleteAllImagesInFolder(folder: Folder) {
        val images = folder.images
        val user = folder.user
        for (image in images) {
            deleteImage(user, image)
        }
    }

}