package com.keeply.global.security

import com.keeply.domain.user.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider (
    @Value("\${jwt.secret}")
    private val secret: String,
    @Value("\${jwt.access-token.expiration}")
    private val accessTokenExpiration: Long,
    @Value("\${jwt.refresh-token.expiration}")
    private val refreshTokenExpiration: Long

) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateAccessToken(user: User): String {
        val claims: Claims = Jwts.claims().setSubject(user.id.toString())

        val now = Date()
        val validity = Date(now.time + accessTokenExpiration)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun generateRefreshToken(user: User): String {
        val claims: Claims = Jwts.claims().setSubject(user.id.toString())

        val now = Date()
        val validity = Date(now.time + refreshTokenExpiration)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun getUserId(accessToken: String): Long {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(accessToken)
            .body.subject.toLong()
    }

    fun validateToken(accessToken: String): Boolean {
        return try{
            val claims = Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
            !claims.body.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun getAuthentication(accessToken: String): Authentication {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)

        val userId = claims.body.subject.toLongOrNull()
            ?: throw RuntimeException("잘못된 토큰입니다.")

        val userDetails = CustomUserDetails(userId = userId)
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
}