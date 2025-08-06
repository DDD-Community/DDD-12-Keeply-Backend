package com.keeply.api.user.controller

import com.keeply.api.user.dto.UserInfoDTO
import com.keeply.api.user.service.UserService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import com.keeply.global.security.CustomUserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
) {
    @GetMapping
    fun getUserInfo(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): ResponseEntity<ApiResponse<UserInfoDTO>> {
        val apiResponse = userService.getUserInfo(userDetails.userId)
        return ResponseEntity.ok(apiResponse)
    }

    @DeleteMapping
    fun deleteUser(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): ResponseEntity<ApiResponse<Message>> {
        val apiResponse = userService.deleteUser(userDetails.userId)
        return ResponseEntity.ok(apiResponse)
    }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
    ): ResponseEntity<ApiResponse<Message>> {
        val apiResponse = userService.logout(userDetails.userId)
        return ResponseEntity.ok(apiResponse)
    }
}