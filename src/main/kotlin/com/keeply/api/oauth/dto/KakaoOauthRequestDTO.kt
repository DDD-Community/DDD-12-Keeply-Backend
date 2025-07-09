package com.keeply.api.oauth.controller.dto

import lombok.Getter

@Getter
class KakaoOauthRequestDTO {
    data class KakaoOauthCallbackDTO(
        val code: String //authorization code 인가 코드
    )
}