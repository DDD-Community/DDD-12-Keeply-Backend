package com.keeply.global.redis.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class CachedOcrImage @JsonCreator constructor(
    @JsonProperty("base64Image") val base64Image: String,
    @JsonProperty("detectedText") val detectedText: String
)
