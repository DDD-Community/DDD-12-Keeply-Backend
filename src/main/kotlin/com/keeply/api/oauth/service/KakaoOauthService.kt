package com.keeply.api.oauth.service

import com.keeply.api.oauth.controller.dto.KakaoOauthResponseDTO
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
    fun loginWithKakao(code: String) : KakaoOauthResponseDTO.LoginResponse {
        val accesstoken = kakaoOauthClient.getUserAccessToken(code).accessToken
        val userInfo = kakaoOauthClient.getUserInfo(accesstoken)
        val user: User = userRepository.findByKakaoId(userInfo.id)
            ?: userRepository.save(
                User(
                    kakaoId = userInfo.id,
                    nickname = userInfo.kakao_account.profile.nickName,
                    email = userInfo.kakao_account.email,
                    profileImageUrl = userInfo.kakao_account.profile.profile_image_url,
                    thumbnailImageUrl = userInfo.kakao_account.profile.thumbnail_image_url
                )
            )

        return KakaoOauthResponseDTO.LoginResponse(
            jwtProvider.generateAccessToken(user),
            jwtProvider.generateRefreshToken(user)
        )
    }
}