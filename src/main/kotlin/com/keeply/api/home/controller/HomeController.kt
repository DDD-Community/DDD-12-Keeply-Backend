package com.keeply.api.home.controller

import com.keeply.api.home.dto.HomeResponseDTO
import com.keeply.api.home.service.HomeService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/home")
class HomeController(
    private val homeService: HomeService,
) {
    @GetMapping
    @Operation(summary = "홈화면 요청 API")
    fun getHome(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<ApiResponse<HomeResponseDTO>>{
        try{
            val apiResponse = homeService.getHome(userDetails.userId)
            return ResponseEntity.ok(apiResponse)
        } catch(e: Exception){
            val apiResponse = ApiResponse<HomeResponseDTO>(
                success = false,
                reason = e.message
            )
            return ResponseEntity.badRequest().body(apiResponse)
        }
    }
}