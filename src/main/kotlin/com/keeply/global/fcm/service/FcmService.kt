package com.keeply.global.fcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.keeply.domain.image.entity.Image
import com.keeply.domain.user.entity.User
import org.springframework.stereotype.Service

@Service
class FcmService(
    private val firebaseMessaging: FirebaseMessaging
) {
    fun sendStorageLimitNotification(user: User) {
        if(user.userSetting!!.storageNotificationEnabled) {
            val message = Message.builder()
                .setToken(user.fcmToken)  // 유저의 FCM 토큰 필요
                .putData("title", "저장 용량 초과 주의")
                .putData("body", "저장 공간의 80%에 도달했습니다. 필요 없는 이미지를 삭제해주세요.")
                .build()

            firebaseMessaging.send(message)
        }
    }

    fun sendScheduledToDeleteImageNotification(user: User, scheduledToDeleteImages: List<Image>) {
        if(user.userSetting!!.storageNotificationEnabled) {
            val message = Message.builder()
                .setToken(user.fcmToken)  // 유저의 FCM 토큰 필요
                .putData("title", "미분류 이미지 삭제 예정 알림")
                .putData("body", "1일이내에 삭제될 이미지가 ${scheduledToDeleteImages.size}개 있습니다.")
                .build()

            firebaseMessaging.send(message)
        }
    }
}
