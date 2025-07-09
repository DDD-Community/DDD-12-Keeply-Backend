package com.keeply.api.oauth.controller.dto

import com.keeply.domain.user.entity.User

data class JwtLoginResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)