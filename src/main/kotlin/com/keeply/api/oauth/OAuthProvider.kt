package com.keeply.api.oauth

enum class OAuthProvider (
    val userInfoUri: String,
) {
    KAKAO(
        userInfoUri = "https://kapi.kakao.com/v2/user/me"
    )
}