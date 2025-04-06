package com.example.student.management.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class HashEncoder {

    private val bCrypt = BCryptPasswordEncoder()

    // hashes the raw password string from the client
    fun encode(raw: String): String = bCrypt.encode(raw)

    // verify that the client provided password matches the one in the database
    fun matches(raw: String, hashed: String): Boolean {
        return bCrypt.matches(raw, hashed)
    }

}

