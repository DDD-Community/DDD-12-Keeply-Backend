package com.keeply.api.s3.controller.dto

import org.springframework.web.multipart.MultipartFile

class S3RequestDTO {
    data class upload(
        var file: MultipartFile,
        var userId: String? = null
    )
}
