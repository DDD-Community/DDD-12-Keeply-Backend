package com.keeply.api.s3.controller.dto

class S3ResponseDTO {
    data class upload(
//        var ocrText: String? = null,
        var presignedUrl: String? = null
    )
}