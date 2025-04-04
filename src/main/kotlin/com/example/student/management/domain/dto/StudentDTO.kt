package com.example.student.management.domain.dto

import com.example.student.management.domain.entities.Gender
import com.example.student.management.domain.entities.StudentStatus
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class StudentDTO(
    val id: Long = -1L,

    @field:NotNull(message = "first name can't be null")
    @field:Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    val firstName: String = "",

    @field:NotNull(message = "last name can't be null")
    @field:Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters")
    val lastName: String = "",

    val dateOfBirth: LocalDateTime = LocalDateTime.now(),
    val gender: Gender = Gender.OTHER,

    @field:Email(message = "invalid email address")
    val email: String = "",

    @field:Pattern(regexp = "^\\d{10}$", message = "invalid mobile")
    val guardianMobile: String = "",

    val address: String = "",
    val enrollmentDate: LocalDateTime = LocalDateTime.now(),
    val program: String = "",
    val department: String = "",
    val status: StudentStatus = StudentStatus.ACTIVE,
    val course: List<CourseDTO> = emptyList(),
)

