package com.keeply.global.fcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.entity.UserSetting
import com.keeply.domain.user.repository.UserRepository
import com.keeply.domain.user.repository.UserSettingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import kotlin.time.Duration

@Service
class FcmService(
    private val firebaseMessaging: FirebaseMessaging,
    private val userSettingRepository: UserSettingRepository,
    private val userRepository: UserRepository,
) {
    fun sendStorageLimitNotification(user: User) {
        val userSetting: UserSetting = userSettingRepository.findByUser(user)!!
        if(userSetting.storageNotificationEnabled) {
            val message = Message.builder()
                .setToken(user.fcmToken)  // 유저의 FCM 토큰 필요
                .setNotification(
                    Notification.builder()
                        .setTitle("저장 용량 초과 주의")
                        .setBody("저장 공간의 80%에 도달했습니다. 필요 없는 이미지를 삭제해주세요.")
                        .build()
                )
                .build()

            firebaseMessaging.send(message)
        }
    }

    fun sendScheduledToDeleteImageNotification(user: User) {
        val imageList = userRepository.findUserById(user.id)!!
            .folders
            .map {
                folder -> folder.images
                .filter { it.isCategorized == false }
                .map { image ->
                    if(image.scheduledDeleteAt!!.toLocalDate().isBefore(LocalDate.now().plusDays(1)) ) {
                        image
                    }
                }
            }
        val message = Message.builder()
            .setToken(user.fcmToken)  // 유저의 FCM 토큰 필요
            .setNotification(
                Notification.builder()
                    .setTitle("미분류 이미지 삭제 예정 알림")
                    .setBody("1일이내에 삭제될 이미지가 ${imageList.size}개 있습니다.")
                    .build()
            )
            .build()
    }
}
