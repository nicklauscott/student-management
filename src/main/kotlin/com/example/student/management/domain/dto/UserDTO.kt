package com.example.student.management.domain.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @field:NotNull(message = "First name can't be null")
    @field:Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters")
    val firstName: String,

    @field:NotNull(message = "last name can't be null")
    @field:Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters")
    val lastName: String,

    @field:Email(message = "Invalid email format.") val email: String,

    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
        message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val password: String
)

data class LoginRequest(
    @field:Email(message = "Invalid email format.") val email: String,
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{9,}\$",
        message = "Password must be at least 9 characters long and contain at least one digit, uppercase and lowercase character."
    )
    val password: String
)

data class TokenPair(val accessToken: String, val refreshToken: String)

data class RefreshRequest(val refreshToken: String)

