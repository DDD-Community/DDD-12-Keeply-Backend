package com.keeply.global.fcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.entity.UserSetting
import com.keeply.domain.user.repository.UserSettingRepository
import org.springframework.stereotype.Service

@Service
class FcmService(
    private val firebaseMessaging: FirebaseMessaging,
    private val userSettingRepository: UserSettingRepository
) {
    fun sendStorageLimitNotification(user: User) {
        val userSetting: UserSetting = userSettingRepository.findByUser(user)!!
        if(userSetting.storageNotificationEnabled) {
            val message = Message.builder()
                .setToken(user.fcmToken)  // 유저의 FCM 토큰 필요
                .setNotification(
                    Notification.builder()
                        .setTitle("저장 용량 초과")
                        .setBody("저장 공간이 가득 찼습니다. 필요 없는 이미지를 삭제해주세요.")
                        .build()
                )
                .build()

            firebaseMessaging.send(message)
        }
    }
}
