package com.example.student.management.security

import com.example.student.management.domain.dto.LoginRequest
import com.example.student.management.domain.dto.RefreshRequest
import com.example.student.management.domain.dto.RegisterUserRequest
import com.example.student.management.domain.dto.TokenPair
import com.example.student.management.domain.entities.RefreshToken
import com.example.student.management.domain.entities.Role
import com.example.student.management.domain.entities.User
import com.example.student.management.exception.BadCredentialsException
import com.example.student.management.exception.InvalidRefreshTokenException
import com.example.student.management.exception.UserAlreadyExistException
import com.example.student.management.repository.RefreshTokenRepository
import com.example.student.management.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService, private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder, private val refreshTokenRepository: RefreshTokenRepository
) {

    fun register(user: RegisterUserRequest): User {
        if (userRepository.findByEmail(user.email) != null)  throw UserAlreadyExistException(user.email)
        return userRepository.save(User( // register a new user
            firstName = user.firstName, lastName = user.lastName,
            email = user.email, hashedPassword = hashEncoder.encode(user.password),
            role = Role.USER
        ))
    }

    fun login(loginRequest: LoginRequest): TokenPair {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw BadCredentialsException()

        if (!hashEncoder.matches(loginRequest.password, user.hashedPassword)) {
            throw BadCredentialsException()
        }

        // generate both tokens
        val accessToken = jwtService.generateAccessToken(user.email)
        val refreshToken = jwtService.generateRefreshToken(user.email)

        // store the refresh token
        user.id?.let { storeRefreshToken(userId = it, refreshToken) }
        return TokenPair(accessToken, refreshToken)
    }

    /* we refresh the token deletes the old token and save the newly
       generate token to the database and return the new tokens to the client */
    @Transactional
    fun refreshToken(refreshRequest: RefreshRequest): TokenPair {
        if (!jwtService.validateRefreshToken(refreshRequest.refreshToken)) throw InvalidRefreshTokenException(null)

        val userId = jwtService.getUserIdFromToken(refreshRequest.refreshToken) ?: throw InvalidRefreshTokenException(null)
        val user = userRepository.findByEmail(userId) ?: throw InvalidRefreshTokenException(null)

        val hashedToken = hashToken(refreshRequest.refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(userId = user.id!!, hashedToken)
            ?: throw InvalidRefreshTokenException("Refresh token not recognized (maybe used or expired?)")

        // delete the old refresh token
        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id!!, hashedToken)

        // generate new tokens
        val accessToken = jwtService.generateAccessToken(user.email)
        val refreshToken = jwtService.generateRefreshToken(user.email)
        storeRefreshToken(user.id!!, refreshToken) // store the new refresh token
        return TokenPair(accessToken, refreshToken)
    }

    private fun storeRefreshToken(userId: Long, rawRefreshToken: String) {
        refreshTokenRepository.save(RefreshToken(
            userId = userId, hashedToken = hashToken(rawRefreshToken),
            expiresAt = Instant.now().plusMillis(jwtService.refreshTokenValidityInMs)
        ))
    }

    // token hashing is different from password hashing
    private fun hashToken(token: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }


}