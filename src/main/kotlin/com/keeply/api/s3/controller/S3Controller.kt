package com.keeply.api.s3.controller

import com.keeply.api.s3.controller.dto.S3RequestDTO
import com.keeply.api.s3.controller.dto.S3ResponseDTO
import com.keeply.api.s3.service.S3Service
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/s3")
class S3Controller(
    private val s3Service: S3Service
) {
    @PostMapping("/upload")
    fun uploadImage(
        @ModelAttribute requestDTO: S3RequestDTO.upload
    ) :ResponseEntity<*> {
        if(requestDTO.file.size > 1_048_576) {
            return ResponseEntity.badRequest().body(
                "파일 크기는 1MB 이하로 제한됩니다."
            )
        }
        return ResponseEntity.status(HttpStatus.OK).body(s3Service.uploadImage(requestDto))
    }
}