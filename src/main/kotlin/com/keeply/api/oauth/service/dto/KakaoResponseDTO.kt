package com.keeply.api.oauth.service.dto

class KakaoResponseDTO {
    data class KakaoGetTokenDTO (
        val access_token: String
    )
}