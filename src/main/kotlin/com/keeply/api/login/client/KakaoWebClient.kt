package com.keeply.api.login.client

import com.keeply.api.login.dto.KakaoUserInfoDTO
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoWebClient(
    private val webClient: WebClient
) {
    fun getUserInfo(accessToken: String): KakaoUserInfoDTO.kakaoUserInfo {
        return webClient.get()
            .uri("https://kapi.kakao.com/v2/user/me")
            .headers {
                it.setBearerAuth(accessToken)
            }
            .retrieve()
            .bodyToMono(KakaoUserInfoDTO.kakaoUserInfo::class.java)
            .block()!!
    }
}