package com.keeply.api.oauth.controller

import com.keeply.api.oauth.controller.dto.KakaoOauthRequestDTO
import com.keeply.api.oauth.controller.dto.KakaoOauthResponseDTO
import com.keeply.api.oauth.service.KakaoOauthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class KakaoOauthController(
    private val kakaoOauthService: KakaoOauthService
) {
    @GetMapping("/kakao/callback")
    fun getKakaoOauthCallback(
        @ModelAttribute kakaoOauthCallback: KakaoOauthRequestDTO.KakaoOauthCallbackDTO
    ) : ResponseEntity<KakaoOauthResponseDTO.LoginResponse> {
        return ResponseEntity.ok(kakaoOauthService.loginWithKakao(kakaoOauthCallback.code))
    }
}