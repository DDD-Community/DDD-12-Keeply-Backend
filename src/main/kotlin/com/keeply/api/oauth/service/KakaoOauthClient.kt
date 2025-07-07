package com.keeply.api.oauth.service

import com.keeply.api.oauth.OAuthProvider
import com.keeply.api.oauth.service.dto.KakaoResponseDTO
import com.keeply.api.oauth.service.dto.KakaoUserInfoDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.nio.charset.Charset

@Component
class KakaoOauthClient(
    private val webClient: WebClient,
    @Value("\${kakao.client.id}")
    private val kakaoClientId: String
) {
    fun getUserAccessToken(code: String): KakaoResponseDTO.KakaoGetTokenDTO {
        return webClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .acceptCharset(Charset.forName("UTF-8"))
            .body(
                BodyInserters.fromFormData("grant_type", "authorization_code")
                    .with("client_id", kakaoClientId)
                    .with("redirect_uri", "http://localhost:8080/auth/kakao/callback")
                    .with("code", code)
            )
            .retrieve()
            .bodyToMono(KakaoResponseDTO.KakaoGetTokenDTO::class.java)
            .block()!!
    }
    fun getUserInfo(accessToken: String): KakaoUserInfoDTO.kakaoUserInfo {
        return webClient.get()
            .uri(OAuthProvider.KAKAO.userInfoUri)
            .headers {
                it.setBearerAuth(accessToken)
            }
            .retrieve()
            .bodyToMono(KakaoUserInfoDTO.kakaoUserInfo::class.java)
            .block()!!
    }
}