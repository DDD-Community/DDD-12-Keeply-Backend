import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*

private val key = Keys.hmacShaKeyFor("thisIsASecretKeyWithEnoughLength123!".toByteArray())

fun generateAccessToken(): String {
    val claims: Claims = Jwts.claims().setSubject("1")

    val now = Date()
    val validity = Date(now.time + 100L * 365 * 24 * 60 * 60 * 1000)

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact()
}

fun main() {
    println(generateAccessToken())
}