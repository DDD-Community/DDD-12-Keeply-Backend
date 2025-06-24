package com.keeply.api.oauth.service.dto

import org.springframework.beans.factory.annotation.Value

class KakaoRequestDTO {

    data class KakaoGetAccessTokenDTO(
        val grant_type: String = "authorization_code",
        @Value("kakao.client.id")
        val client_id: String,
        val redirect_uri: String = "http://localhost:8080/auth/kakao/callback",
        var code: String,
    )
}