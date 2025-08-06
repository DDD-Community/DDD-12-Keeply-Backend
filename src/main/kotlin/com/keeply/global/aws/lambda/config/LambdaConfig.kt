package com.keeply.global.aws.lambda.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.lambda.LambdaClient

@Configuration
class LambdaConfig {
    @Bean
    fun LambdaClient(): LambdaClient {
        val client = LambdaClient.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build()
        return client
    }
}