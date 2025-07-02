package com.keeply.api.oauth.controller

import com.keeply.api.oauth.controller.dto.JwtLoginResponseDTO
import com.keeply.api.oauth.controller.dto.KakaoOauthRequestDTO
import com.keeply.api.oauth.service.KakaoOauthService
import lombok.extern.slf4j.Slf4j
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.Console

@RestController
@RequestMapping("/auth")
@Slf4j
class KakaoOauthController(
    private val kakaoOauthService: KakaoOauthService
) {
    @GetMapping("/kakao/callback")
    fun getKakaoOauthCallback(
        @ModelAttribute kakaoOauthCallback: KakaoOauthRequestDTO.KakaoOauthCallbackDTO
    ) : ResponseEntity<JwtLoginResponseDTO> {
        println(kakaoOauthCallback)
        return ResponseEntity.ok(kakaoOauthService.loginWithKakao(kakaoOauthCallback.code))
    }
}