package com.keeply.global.dto

data class ApiResponse<T>(
    val success: Boolean,
    val reason: String? = null,
    val response: T? = null
)
