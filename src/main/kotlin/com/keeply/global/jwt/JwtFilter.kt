package com.keeply.global.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
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