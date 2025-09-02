package com.keeply.global.redis.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.keeply.global.redis.dto.CachedOcrImage
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun objectMapper(): ObjectMapper =
        jacksonObjectMapper()
            .findAndRegisterModules()

    @Bean
    fun redisTemplate(
        factory: RedisConnectionFactory
    ): RedisTemplate<String, CachedOcrImage> {
        val template = RedisTemplate<String, CachedOcrImage>()
        template.setConnectionFactory(factory)
        template.keySerializer = StringRedisSerializer()

        val serializer = Jackson2JsonRedisSerializer(CachedOcrImage::class.java)
        template.valueSerializer = serializer

        template.afterPropertiesSet()
        return template
    }
}
