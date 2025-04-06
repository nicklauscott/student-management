package com.example.student.management.security

import com.example.student.management.exception.UserNotFoundException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService( // the jwtSecret base64 secret from environment variable
    @Value("\${jwt.secret}") private val jwtSecret: String
) {

    // we decode and sign create a key with the jwtSecret
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))

    private val accessTokenValidityInMs = 24 * 60 * 60 * 1000L // 24 hrs
    val refreshTokenValidityInMs = 30L * 24 * 60 * 60 * 1000L // 30 days

    // we expose functions to generate access & refresh tokens
    fun generateAccessToken(userId: String): String = generateToken(userId, "access", accessTokenValidityInMs)

    fun generateRefreshToken(userId: String): String = generateToken(userId, "refresh", refreshTokenValidityInMs)


    // this will generate a token for a given client
    private fun generateToken(userId: String, type: String, expiry: Long): String {
        val now = Date()
        val expiresAt = Date(now.time + expiry)
        return Jwts.builder()
            .claim("type", type) //type of token [access, refresh]
            .subject(userId) // client that this token will belong to
            .issuedAt(now).expiration(expiresAt) // issued & expiration date
            .signWith(secretKey, SIG.HS256) // sign it with our secret key
            .compact()
    }


    // function to extract userId from the token
    fun getUserIdFromToken(token: String): String? {
        val claims = parseAllClaims(token) ?: throw UserNotFoundException(null) // throw custom exception
        return claims.subject
    }

    // functions to validate access & refresh tokens
    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return claims["type"] as String == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return claims["type"] as String == "refresh"
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = if (token.startsWith("Bearer ")) token.removePrefix("Bearer ") else token
        return try {
            Jwts.parser()
                .verifyWith(secretKey).build() // we provide our secret key
                .parseSignedClaims(rawToken).payload // then provide the client token
        } catch (ex: Exception) { null } // returns null is something went wrong
    }

}

