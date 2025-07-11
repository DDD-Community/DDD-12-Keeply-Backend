package com.keeply.api.login.dto

data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val userId: Long? = null,
    val username: String,
    val email: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String
)
