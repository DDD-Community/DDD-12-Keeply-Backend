package com.keeply.global.aws.lambda

import org.springframework.stereotype.Component
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.lambda.LambdaClient
import software.amazon.awssdk.services.lambda.model.InvocationType
import software.amazon.awssdk.services.lambda.model.InvokeRequest

@Component
class LambdaService(
    private val lambdaClient: LambdaClient
) {

    fun backupUserImagesBeforeDeletion(userId: Long) {
        val request = InvokeRequest.builder()
            .functionName("Keeply-Lambda-backup-userinfo")
            .invocationType(InvocationType.EVENT) // 비동기 호출
            .payload(
                SdkBytes.fromByteArray(
                """
                { "userId": "$userId" }
                """.trimIndent().toByteArray()
                )
            )
            .build()

        lambdaClient.invoke(request)
    }

    fun restoreDeletedUserImages(userId: Long) {
        val request = InvokeRequest.builder()
            .functionName("Keeply-Lambda-restore-userinfo")
            .invocationType(InvocationType.EVENT)
            .payload(
                SdkBytes.fromByteArray(
                    """
                        { "userId": "$userId" }
                    """.trimIndent().toByteArray()
                )
            )
            .build()

        lambdaClient.invoke(request)
    }

}