package com.keeply.domain.image.repository

import com.keeply.domain.image.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository: JpaRepository<Image, Long> {
    fun findImageByIdAndUserId(imageId: Long,userId: Long): Image?
    fun findAllByUserIdAndFolderIsNull(userId: Long): List<Image>?
}