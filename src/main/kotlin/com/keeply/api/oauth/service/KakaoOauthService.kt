package com.keeply.api.oauth.service

import com.keeply.api.oauth.controller.dto.JwtLoginResponseDTO
import com.keeply.domain.user.entity.User
import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.jwt.JwtProvider
import org.springframework.stereotype.Service

@Service
class KakaoOauthService (
    private val kakaoOauthClient: KakaoOauthClient,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider

) {
    fun loginWithKakao(code: String) : JwtLoginResponseDTO {
        val accesstoken = kakaoOauthClient.getUserAccessToken(code).access_token
        val userInfo = kakaoOauthClient.getUserInfo(accesstoken)
        val user: User = userRepository.findByKakaoId(userInfo.id)
            ?: userRepository.save(
                User(
                    kakaoId = userInfo.id,
                    nickname = userInfo.kakao_account.profile.nickname,
                    email = userInfo.kakao_account.email,
                    profileImageUrl = userInfo.kakao_account.profile.profile_image_url,
                    thumbnailImageUrl = userInfo.kakao_account.profile.thumbnail_image_url
                )
            )

        return JwtLoginResponseDTO(
            jwtProvider.generateAccessToken(user),
            jwtProvider.generateRefreshToken(user),
            user
        )
    }
}