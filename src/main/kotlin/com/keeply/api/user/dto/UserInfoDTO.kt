package com.keeply.api.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class UserInfoDTO(
    @Schema(description = "프로필 이미지 URL")
    val profileImageUrl: String,
    @Schema(description = "닉네임")
    val nickname: String,
    @Schema(description = "이메일")
    val email: String
)
