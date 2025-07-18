package com.keeply.global.security.config

import com.keeply.global.security.JwtFilter
import com.keeply.global.security.JwtProvider
import com.keeply.global.security.WhiteList
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig (
    private val jwtProvider: JwtProvider
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .httpBasic { it.disable() }
            .headers {
                it.frameOptions { it.disable() }
            }
            .csrf {
                it.disable()
                it.ignoringRequestMatchers(
                    *WhiteList.ALL
                )
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    *WhiteList.ALL
                ).permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

}