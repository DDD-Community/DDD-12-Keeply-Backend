package com.keeply.api.login.controller

import com.keeply.api.login.dto.LoginRequestDTO
import com.keeply.api.login.dto.LoginResponseDTO
import com.keeply.api.login.service.LoginService
import com.keeply.global.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/login")
class LoginController (
    private val loginService: LoginService
) {
    @PostMapping
    fun loginWithKakao(
        @RequestHeader("Authorization") requestDTO: LoginRequestDTO
    ): ResponseEntity<ApiResponse<LoginResponseDTO>>{
        val apiResponse = loginService.loginWithKakao(requestDTO)
        if(apiResponse.success){
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse)
        }
    }
}