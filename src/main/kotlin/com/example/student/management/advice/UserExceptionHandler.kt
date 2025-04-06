package com.example.student.management.advice

import com.example.student.management.exception.BadCredentialsException
import com.example.student.management.exception.InvalidRefreshTokenException
import com.example.student.management.exception.UserAlreadyExistException
import com.example.student.management.exception.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class UserExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException): Map<String, String> {
        return mapOf("error message" to "${ex.message}")
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException::class)
    fun handleUserAlreadyExist(ex: UserAlreadyExistException): Map<String, String> {
        return mapOf("error message" to "${ex.message}")
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidRefreshTokenException::class)
    fun handleInvalidRefreshToken(ex: InvalidRefreshTokenException): Map<String, String> {
        return mapOf("error message" to "${ex.message}")
    }

    // BadCredentialsException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): Map<String, String> {
        return mapOf("error message" to "${ex.message}")
    }
}

