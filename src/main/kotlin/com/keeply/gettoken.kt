import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*

private val key = Keys.hmacShaKeyFor("thisIsASecretKeyWithEnoughLength123!".toByteArray())
//eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzU0MDY2MTgzLCJleHAiOjQ5MDc2NjYxODN9.beKZBO5iyGLcoIxP9-USelJu0mY-NGJiFuTSDhnG5Ak
fun generateAccessToken(): String {
    val claims: Claims = Jwts.claims().setSubject("4356055542")

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