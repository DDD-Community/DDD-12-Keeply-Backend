package com.keeply.api.login.dto

import java.time.LocalDateTime

class KakaoUserInfoDTO {
    data class kakaoUserInfo(
        val id: Long,
        val connected_at: LocalDateTime,
        val kakao_account: KakaoAccount
    )

    data class KakaoAccount (
        val profile_needs_agreement: Boolean,
        val profile_nickname_needs_agreement: Boolean,
        val profile_image_needs_agreement: Boolean,
        val email_needs_agreement: Boolean,
        val profile: Profile,
        val email: String
    )

    data class Profile(
        val nickname: String,
        val thumbnail_image_url: String,
        val profile_image_url: String
    )
}