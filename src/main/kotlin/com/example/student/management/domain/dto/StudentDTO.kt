package com.example.student.management.domain.dto

import com.example.student.management.domain.entities.Gender
import com.example.student.management.domain.entities.StudentStatus
import java.time.LocalDateTime

data class StudentDTO(
    val id: Long = -1L,
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: LocalDateTime = LocalDateTime.now(),
    val gender: Gender = Gender.OTHER,
    val email: String = "",
    val guardianMobile: String = "",
    val address: String = "",
    val enrollmentDate: LocalDateTime = LocalDateTime.now(),
    val program: String = "",
    val department: String = "",
    val status: StudentStatus = StudentStatus.ACTIVE,
    val course: List<CourseDTO> = emptyList(),
)

