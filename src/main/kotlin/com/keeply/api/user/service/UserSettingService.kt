package com.keeply.api.user.service

import com.keeply.api.user.dto.UserSettingRequestDTO
import com.keeply.api.user.dto.UserSettingResponseDTO
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.entity.UserSetting
import com.keeply.domain.user.repository.UserRepository
import com.keeply.domain.user.repository.UserSettingRepository
import com.keeply.global.dto.ApiResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserSettingService(
    private val userRepository: UserRepository,
    private val userSettingRepository: UserSettingRepository
) {
    fun getUserSetting(userId: Long) : ApiResponse<UserSettingResponseDTO> {
        val user = getUser(userId)
        val userSetting = getUserSetting(user)
        return ApiResponse<UserSettingResponseDTO>(
            success = true,
            response = UserSettingResponseDTO(
                allowStorageNotification = userSetting.storageNotificationEnabled,
                allowMarketingNotification = userSetting.marketingNotificationEnabled
            )
        )
    }

    fun setUserSetting(userId: Long, requestDTO: UserSettingRequestDTO):  ApiResponse<UserSettingResponseDTO> {
        val user = getUser(userId)
        val userSetting = getUserSetting(user)
        if (requestDTO.allowStorageNotification != null) {
            userSetting.storageNotificationEnabled = requestDTO.allowStorageNotification
        }
        if (requestDTO.allowMarketingNotification != null) {
            userSetting.marketingNotificationEnabled = requestDTO.allowMarketingNotification
        }
        return ApiResponse<UserSettingResponseDTO>(
            success = true,
            response = UserSettingResponseDTO(
                allowStorageNotification = userSetting.storageNotificationEnabled,
                allowMarketingNotification = userSetting.marketingNotificationEnabled
            )
        )
    }

    private fun getUserSetting(user: User): UserSetting = (userSettingRepository.findByUser(user)
        ?: throw Exception("유저의 설정 정보가 존재하지 않습니다."))

    private fun getUser(userId: Long): User = (userRepository.findUserById(userId)
        ?: throw Exception("존재하지 않는 유저입니다."))

}