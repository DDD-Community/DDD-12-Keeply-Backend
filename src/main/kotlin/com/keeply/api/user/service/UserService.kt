package com.keeply.api.user.service

import com.keeply.api.user.dto.UserInfoDTO
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.aws.lambda.LambdaService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val lambdaService: LambdaService

) {
    fun getUserInfo(userId: Long): ApiResponse<UserInfoDTO> {
        val user = getUser(userId)
        return ApiResponse<UserInfoDTO> (
            success = true,
            response = UserInfoDTO(
                profileImageUrl = user.profileImageUrl,
                nickname = user.nickname,
                email = user.email
            )
        )
    }

    fun deleteUser(userId: Long): ApiResponse<Message> {
        val user = getUser(userId)
        lambdaService.backupUserImagesBeforeDeletion(user.id)
        updateUserStatusToDeleted(user)

        return ApiResponse<Message>(
            success = true,
            response = Message(
                message = "휴면계정으로 변환되었습니다. (유저 ID = ${user.id})"
            )
        )
    }

    private fun updateUserStatusToDeleted(user: User) {
        user.isDeleted = true
        user.deletedAt = LocalDateTime.now()
        user.scheduledDeleteAt = LocalDateTime.now() + Duration.ofDays(30)
    }

    private fun getUser(userId: Long): User = (userRepository.findUserById(userId)
        ?: throw Exception("유저가 존재하지 않습니다."))
}