package com.keeply.api.user.controller

import com.keeply.api.user.dto.UserSettingRequestDTO
import com.keeply.api.user.dto.UserSettingResponseDTO
import com.keeply.api.user.service.UserSettingService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user/notification")
class UserSettingController(
    private val userSettingService: UserSettingService
) {
    @GetMapping
    @Operation(summary = "User 알림설정 정보 조회 API")
    fun getUserSetting(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): ResponseEntity<ApiResponse<UserSettingResponseDTO>> {
        try{
            val apiResponse = userSettingService.getUserSetting(userDetails.userId)
            return ResponseEntity.ok(apiResponse)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse<UserSettingResponseDTO>(
                success = false,
                reason = e.message
            ))
        }
    }

    @PostMapping
    @Operation(summary = "User 알림설정 API")
    fun setUserSetting(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody requestDTO: UserSettingRequestDTO
    ): ResponseEntity<ApiResponse<UserSettingResponseDTO>> {
        try{
            val apiReponse = userSettingService.setUserSetting(userDetails.userId, requestDTO)
            return ResponseEntity.ok(apiReponse)
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse<UserSettingResponseDTO>(
                success = false,
                reason = e.message
            ))
        }
    }
}