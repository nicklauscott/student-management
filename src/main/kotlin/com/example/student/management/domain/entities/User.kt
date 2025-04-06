package com.example.student.management.domain.entities

import jakarta.persistence.*

@Entity
@Table(name = "_user")
data class User(
    @Id @GeneratedValue val id: Long? = null,
    val firstName: String,
    val lastName: String,
    @Column(unique = true, nullable = false) val email: String,
    @Column(nullable = false) val hashedPassword: String,
    @Enumerated(EnumType.STRING) val role: Role
)

enum class Role {
    ADMIN, USER
}


