package com.keeply.global.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import kotlin.text.startsWith
import kotlin.text.substring

class JwtFilter(
    private val jwtProvider: JwtProvider
) : GenericFilterBean() {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        filterChain: FilterChain
    ) {

        val jwtAccessToken = resolveToken(request as HttpServletRequest)

        if(jwtAccessToken != null && jwtProvider.validateToken(jwtAccessToken)) {
            val authentication = jwtProvider.getAuthentication(jwtAccessToken)
            SecurityContextHolder.getContext().authentication = authentication
        } else {
            var httpResponse = response as HttpServletResponse
            httpResponse.status = 401
            httpResponse.contentType = "application/json"
            httpResponse.characterEncoding = "UTF-8"
            httpResponse.writer.write("""
                {
                    "success": false,
                    "message": "토큰이 없거나 유효하지 않습니다. 다시 로그인해주세요.",
                    "data": null
                }
            """.trimIndent())
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}