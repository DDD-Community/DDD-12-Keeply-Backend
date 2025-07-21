package com.keeply.global.redis

import com.keeply.api.ocr.dto.OcrResponseDTO
import com.keeply.global.redis.dto.CachedOcrImage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.Base64

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<String, CachedOcrImage>
) {
    private val imageTTL: Duration = Duration.ofMinutes(10)

    fun cacheImage(imageId: String, imageBytes: ByteArray, detectedText: String) {
        val base64Image = Base64.getEncoder().encodeToString(imageBytes)
        val value = CachedOcrImage(base64Image, detectedText)
        redisTemplate.opsForValue().set("image:$imageId", value, imageTTL)
    }

    fun getCachedImage(imageId: String?): CachedOcrImage {
        val cachedOcrImage = redisTemplate.opsForValue().get("image:$imageId")
            ?: throw(Exception("이미지가 만료되었습니다."))
        return cachedOcrImage
    }
}