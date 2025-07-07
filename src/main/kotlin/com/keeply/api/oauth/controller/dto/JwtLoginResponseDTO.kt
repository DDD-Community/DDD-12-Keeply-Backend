package com.keeply.api.oauth.controller.dto

data class JwtLoginResponseDTO(
    val accessToken: String,
    val refreshToken: String
)