package com.keeply.domain.user

import com.keeply.domain.user.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class UserCleanUpScheduler(
    private val userRepository: UserRepository
) {
    private val log = LoggerFactory.getLogger(UserCleanUpScheduler::class.java)

    @Scheduled(cron = "0 0 0 * * *")
    fun deleteUsersScheduledForDeletion() {
        val now = LocalDateTime.now()
        val deletedCount = userRepository.deleteAllScheduledBefore(now)

        log.info("회원 영구삭제 스케줄러 실행됨 - ${deletedCount}명의 회원 삭제 (기준시각: $now)")
    }
}