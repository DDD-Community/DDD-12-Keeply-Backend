package com.keeply.global.aws.s3.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class S3Config (
    @Value("\${cloud.aws.credentials.accessKey:}")
    private val accessKey: String,

    @Value("\${cloud.aws.credentials.secretKey:}")
    private val secretKey: String,

    @Value("\${cloud.aws.region.static}")
    private val region: String,
) {
    @Bean
    fun s3Client(): S3Client {
        val credentialsProvider = if (accessKey.isNotBlank() && secretKey.isNotBlank()) {
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        } else {
            DefaultCredentialsProvider.create()
        }
        return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(credentialsProvider)
            .build()
    }
}