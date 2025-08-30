package com.keeply.domain.image

import com.keeply.domain.image.repository.ImageRepository
import com.keeply.global.fcm.service.FcmService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime

@Component
class ImageCleanUpAndNotificationScheduler(
    private val imageRepository: ImageRepository,
    private val fcmService: FcmService
) {
    private val log = LoggerFactory.getLogger(ImageCleanUpAndNotificationScheduler::class.java)

    @Scheduled(cron = "0 0 0 * * *")
    fun scheduledToDeleteImageNotification() {
        val targetTimeStart = LocalDate.now().plusDays(1).atStartOfDay()
        val targetTimeEnd = targetTimeStart.plusDays(1).minusNanos(1)

        val scheduledToDeleteImages = imageRepository.findAllByScheduledDeleteAtBetween(targetTimeStart, targetTimeEnd)

        val userToImages = scheduledToDeleteImages.groupBy { it.user }

        userToImages.forEach { (user,images) ->
            fcmService.sendScheduledToDeleteImageNotification(user, images)
        }

    }

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteUncategorizedImages() {
        val now = LocalDateTime.now()
        val deletedCount = imageRepository.deleteAllScheduledBefore(now)

        log.info("미분류 이미지 삭제 스케줄러 실행됨 - ${deletedCount}개의 이미지 삭제 (기준시각: $now)")
    }
}