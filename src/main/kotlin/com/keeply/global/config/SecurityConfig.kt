package com.keeply.global.config

import com.keeply.global.jwt.JwtFilter
import com.keeply.global.jwt.JwtProvider
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
                it.frameOptions {
                    it.disable()
                }
            }
            .csrf {
                it.disable()
                it.ignoringRequestMatchers("/h2-console/**")
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it.requestMatchers("**").permitAll()
                it.requestMatchers("/h2-console/**").permitAll()
                it.anyRequest().authenticated()
            }
            .addFilterBefore(
                JwtFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

}