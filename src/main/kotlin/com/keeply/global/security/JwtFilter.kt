package com.keeply.global.security

import com.keeply.domain.user.repository.UserRepository
import com.keeply.global.common.Constants
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.GenericFilterBean

class JwtFilter(
    private val jwtProvider: JwtProvider,
    private val userRepository: UserRepository
) : GenericFilterBean() {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        filterChain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val requestURI = httpRequest.requestURI
        val pathMatcher = AntPathMatcher()

        if (Constants.WhiteList.ALL.any { pathMatcher.match(it, requestURI) }) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtAccessToken = resolveToken(httpRequest)

        if (jwtAccessToken != null && jwtProvider.validateToken(jwtAccessToken)) {
            val authentication = jwtProvider.getAuthentication(jwtAccessToken)
            SecurityContextHolder.getContext().authentication = authentication
            if(userRepository.findUserById((authentication.principal as CustomUserDetails).userId)==null) {
                val httpResponse = response as HttpServletResponse
                httpResponse.status = 401
                httpResponse.contentType = "application/json"
                httpResponse.characterEncoding = "UTF-8"
                httpResponse.writer.write(
                    """
                        {
                            "success": false,
                            "reason": "존재하지 않는 유저입니다. 다시 로그인 해주세요",
                            "response": null
                        }
                    """.trimIndent()
                )
                return
            }
        } else {
            val httpResponse = response as HttpServletResponse
            httpResponse.status = 401
            httpResponse.contentType = "application/json"
            httpResponse.characterEncoding = "UTF-8"
            httpResponse.writer.write(
                """
                {
                    "success": false,
                    "reason": "토큰이 없거나 유효하지 않습니다. 다시 로그인해주세요.",
                    "response": null
                }
            """.trimIndent()
            )
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