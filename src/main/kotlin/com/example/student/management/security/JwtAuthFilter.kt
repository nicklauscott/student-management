package com.example.student.management.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(private val jwtService: JwtService): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        /* we extract the authorization header
           extract validates and extracts the userId */
        val authHeader = request.getHeader("Authorization")
        log("1: $authHeader")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            if (jwtService.validateAccessToken(authHeader)) {
                val userId = jwtService.getUserIdFromToken(authHeader)
                log("2: $userId")

                /* we can now change the GlobalContextHolder to the current client,
                 this will allow us to retrieve the details of an authenticated user
                 from anywhere in our code */
                val auth = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    emptyList() // this is very important to allow authenticated user to access protected routes
                )
                SecurityContextHolder.getContext().authentication = auth
                log("3: ${SecurityContextHolder.getContext().authentication.principal}")
            }
        }
        // we then forward the request to the next filter
        filterChain.doFilter(request, response)
    }

}

fun log(string: String) {
    println("----------------------------------------------\n$string\n----------------------------------------------")
}

