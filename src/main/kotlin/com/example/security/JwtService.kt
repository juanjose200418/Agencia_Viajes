package com.example.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String
) {

    init {
        require(secret.isNotBlank()) { "jwt.secret must not be empty. Set the JWT_SECRET environment variable." }
    }


    fun getToken(user: UserDetails): String {
        return getToken(HashMap(), user)
    }

    private fun getToken(extraClaims: Map<String, Any>, user: UserDetails): String {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    private val key: SecretKey
        get() {
            val keyBytes: ByteArray = Base64.getDecoder().decode(secret)
            return Keys.hmacShaKeyFor(keyBytes)
        }

    fun getUsernameFromToken(token: String?): String {
        return getClaim(token) { claims -> claims.subject }
    }

    fun isTokenValid(token: String?, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    private fun getAllClaims(token: String?): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun <T> getClaim(token: String?, claimsResolver: (Claims) -> T): T {
        val claims: Claims = getAllClaims(token)
        return claimsResolver(claims)
    }

    private fun getExpiration(token: String?): Date {
        return getClaim(token) { claims -> claims.expiration }
    }

    private fun isTokenExpired(token: String?): Boolean {
        return getExpiration(token).before(Date())
    }
}
