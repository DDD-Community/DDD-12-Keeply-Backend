package com.keeply.api.login.dto

data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String
)
