package com.example.student.management.repository

import com.example.student.management.domain.entities.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {

    fun findByUserIdAndHashedToken(userId: Long, hashedToken: String): RefreshToken?

    fun deleteByUserIdAndHashedToken(userId: Long, hashedToken: String)

}