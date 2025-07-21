package com.keeply.api.login.controller

import com.keeply.api.login.dto.KakaoUserInfoDTO
import com.keeply.api.login.dto.LoginResponseDTO
import com.keeply.api.login.service.LoginService
import com.keeply.global.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
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
    @Operation(summary = "로그인 API",
        description =
            "Me객체를 JSON형태의 requestBody로 요청"
    )
    fun loginAndRegister(
        @ModelAttribute requestDTO: KakaoUserInfoDTO.kakaoUserInfo
    ) : ResponseEntity<ApiResponse<LoginResponseDTO>> {
        try{
            val apiResponse = loginService.loginAndRegister(requestDTO)

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch(e: Exception){
            val apiResponse = ApiResponse<LoginResponseDTO>(
                success = false,
                message = e.message ?: "Unknown error",
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }
}