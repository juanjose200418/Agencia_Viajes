package com.example.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {

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
            val keyBytes: ByteArray = Base64.getDecoder().decode(SECRET_KEY)
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

    companion object {
        private const val SECRET_KEY = "fc93cb07e1ad92898527100e58a1cf1d1e7f65e9a266a6f87f3c84feb541c7b3"
    }
}
