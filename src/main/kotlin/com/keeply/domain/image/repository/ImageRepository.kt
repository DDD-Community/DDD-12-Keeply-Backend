package com.keeply.domain.image.repository

import com.keeply.domain.image.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ImageRepository: JpaRepository<Image, Long> {
    fun findImageByIdAndUserId(imageId: Long,userId: Long): Image?
    fun findAllByUserIdAndFolderIsNull(userId: Long): List<Image>
    @Modifying
    @Query("""
        DELETE FROM Image i 
        WHERE i.scheduledDeleteAt <= :now 
          AND i.isCategorized = false
    """)
    fun deleteAllScheduledBefore(time: LocalDateTime): Int
}