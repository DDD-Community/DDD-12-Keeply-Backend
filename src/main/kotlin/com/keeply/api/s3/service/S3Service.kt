package com.keeply.api.s3.service

import com.keeply.api.s3.controller.dto.S3RequestDTO
import com.keeply.api.s3.controller.dto.S3ResponseDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration
import java.util.UUID

@Service
class S3Service (
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    @Value("\${cloud.aws.bucketName}") val bucketName: String
) {
    fun uploadImage(
        requestDTO: S3RequestDTO.upload
    ): S3ResponseDTO.upload {
        val key = "users/${requestDTO.userId}/image-${UUID.randomUUID()}.jpg"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType(requestDTO.file.contentType)
            .build()

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(requestDTO.file.bytes))

        var presingedUrl = generatePresignedUrl(key)

        return S3ResponseDTO.upload(presingedUrl)
    }

    private fun generatePresignedUrl(key: String): String {
        val presignedUrl = s3Presigner.presignGetObject(
            GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1440))
                .getObjectRequest {
                    it.bucket(bucketName)
                        .key(key)
                }
                .build()
        ).url()

        return presignedUrl.toString()
    }
}