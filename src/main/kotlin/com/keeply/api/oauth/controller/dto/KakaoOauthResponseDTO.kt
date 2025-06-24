package com.keeply.api.oauth.controller.dto

class KakaoOauthResponseDTO {
    data class LoginResponse(
        val accessToken: String,
        val refreshToken: String
    )
}