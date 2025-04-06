package com.example.student.management.exception

data class UserNotFoundException(val email: String?):
    Exception("User with the email ${email ?: ""} was not found")

data class UserAlreadyExistException(val email: String?):
    Exception("User with the email ${email ?: ""} already exist")


data class InvalidRefreshTokenException(val errorMessage: String?):
    Exception(errorMessage ?: "Invalid refresh token")

class BadCredentialsException: Exception("Bad credentials")

