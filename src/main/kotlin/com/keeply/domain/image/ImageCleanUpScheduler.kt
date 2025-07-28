package com.keeply.domain.image

import com.keeply.domain.image.repository.ImageRepository
import com.keeply.domain.user.UserCleanUpScheduler
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ImageCleanUpScheduler(
    private val imageRepository: ImageRepository
) {
    private val log = LoggerFactory.getLogger(ImageCleanUpScheduler::class.java)

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteUncategorizedImages() {
        val now = LocalDateTime.now()
        val deletedCount = imageRepository.deleteAllScheduledBefore(now)

        log.info("미분류 이미지 삭제 스케줄러 실행됨 - ${deletedCount}개의 이미지 삭제 (기준시각: $now)")
    }
}