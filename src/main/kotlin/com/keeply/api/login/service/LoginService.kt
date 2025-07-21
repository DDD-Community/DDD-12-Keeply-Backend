package com.keeply.api.login.service

import com.keeply.api.login.dto.KakaoUserInfoDTO
import com.keeply.api.login.dto.LoginResponseDTO
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.security.JwtProvider
import org.springframework.stereotype.Service

@Service
class LoginService (
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider
) {
    fun loginAndRegister(requestDTO: KakaoUserInfoDTO.kakaoUserInfo): ApiResponse<LoginResponseDTO> {
        val user: User = findOrSaveUserWithKakao(requestDTO)
        val jwtAccessToken = jwtProvider.generateAccessToken(user)
        val jwtRefreshToken = jwtProvider.generateRefreshToken(user)
        return ApiResponse<LoginResponseDTO> (
            success = true,
            data = LoginResponseDTO(
                accessToken = jwtAccessToken,
                refreshToken = jwtRefreshToken
            )
        )
    }

    private fun findOrSaveUserWithKakao(userInfo: KakaoUserInfoDTO.kakaoUserInfo): User = (
            userRepository.findUserById(userInfo.id)
                ?: userRepository.save(
                    User(
                        id = userInfo.id,
                        nickname = userInfo.kakao_account.profile.nickname,
                        email = userInfo.kakao_account.email,
                        profileImageUrl = userInfo.kakao_account.profile.profile_image_url,
                        thumbnailImageUrl = userInfo.kakao_account.profile.thumbnail_image_url
                    )
                )
        )
}