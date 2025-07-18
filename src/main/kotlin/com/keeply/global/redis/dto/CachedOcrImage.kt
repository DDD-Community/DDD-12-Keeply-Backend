package com.keeply.global.redis.dto

data class CachedOcrImage(
    val base64Image: String,
    val detectedText: String
)