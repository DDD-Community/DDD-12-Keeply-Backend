package com.keeply.api.login.service

import com.keeply.api.login.dto.KakaoUserInfoDTO
import com.keeply.api.login.dto.LoginRequestDTO
import com.keeply.api.login.dto.LoginResponseDTO
import com.keeply.api.login.client.KakaoWebClient
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.jwt.JwtProvider
import org.springframework.stereotype.Service

@Service
class LoginService (
    private val userRepository: UserRepository,
    private val kakaoWebClient: KakaoWebClient,
    private val jwtProvider: JwtProvider
) {
    fun loginWithKakao(requestDTO: LoginRequestDTO): ApiResponse<LoginResponseDTO> {
        val userInfo = kakaoWebClient.getUserInfo(requestDTO.accessToken)
        val user: User = findOrSaveUserWithKakao(userInfo)
        val jwtAccessToken = jwtProvider.generateAccessToken(user)
        val jwtRefreshToken = jwtProvider.generateRefreshToken(user)
        return ApiResponse<LoginResponseDTO> (
            success = true,
            data = LoginResponseDTO(
                accessToken = jwtAccessToken,
                refreshToken = jwtRefreshToken,
                userId = user.id,
                username = user.nickname,
                email = user.email,
                profileImageUrl = user.profileImageUrl,
                thumbnailImageUrl = user.thumbnailImageUrl
            )
        )
    }

    private fun findOrSaveUserWithKakao(userInfo: KakaoUserInfoDTO.kakaoUserInfo): User = (userRepository.findByKakaoId(userInfo.id)
        ?: userRepository.save(
            User(
                kakaoId = userInfo.id,
                nickname = userInfo.kakao_account.profile.nickname,
                email = userInfo.kakao_account.email,
                profileImageUrl = userInfo.kakao_account.profile.profile_image_url,
                thumbnailImageUrl = userInfo.kakao_account.profile.thumbnail_image_url
            )
        ))
}