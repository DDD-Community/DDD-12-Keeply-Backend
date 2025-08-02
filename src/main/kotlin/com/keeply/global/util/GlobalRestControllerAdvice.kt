package com.keeply.global.util

import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalRestControllerAdvice {
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Message>> {
        val apiResponse = ApiResponse<Message>(
            success = false,
            reason = e.message
        )
        return ResponseEntity.badRequest().body(apiResponse)
    }
}