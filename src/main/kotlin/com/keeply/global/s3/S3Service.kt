package com.keeply.global.s3

import org.springframework.beans.factory.annotation.Value
import org.springframework.mock.web.MockMultipartFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import java.time.Duration
import java.util.Base64

@Service
class S3Service (
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner,
    @Value("\${cloud.aws.s3.bucket}") private val bucket: String
){
    fun uploadBase64Image(userId: Long, imageId: Long, base64Image: String): String {
        val bytes = Base64.getDecoder().decode(base64Image)
        val key = "$userId/$imageId.jpg"

        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .contentType("image/jpg")
            .build()

        s3Client.putObject(request, RequestBody.fromBytes(bytes))
        return key
    }

    fun generatePresignedUrl(key: String): String {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        val presignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5))
            .getObjectRequest(getObjectRequest)
            .build()

        return s3Presigner.presignGetObject(presignRequest).toString()
    }

    fun getMultipartFileFromS3(
        key: String
    ): MultipartFile {
        val getObjectRequest = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        s3Client.getObject(getObjectRequest).use { inputStream ->
            val fileName = key.substringAfterLast("/")
            return MockMultipartFile("file", fileName, null, inputStream)
        }
    }
}