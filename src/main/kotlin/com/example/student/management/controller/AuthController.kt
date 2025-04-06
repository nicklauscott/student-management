package com.example.student.management.controller

import com.example.student.management.domain.dto.LoginRequest
import com.example.student.management.domain.dto.RefreshRequest
import com.example.student.management.domain.dto.RegisterUserRequest
import com.example.student.management.domain.dto.TokenPair
import com.example.student.management.security.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping(path = ["v1/auth"])
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody registerUserRequest: RegisterUserRequest) {
        authService.register(registerUserRequest)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<TokenPair> {
        return ResponseEntity.ok(authService.login(loginRequest))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody refreshRequest: RefreshRequest): ResponseEntity<TokenPair> {
        return ResponseEntity.ok(authService.refreshToken(refreshRequest))
    }

}


















