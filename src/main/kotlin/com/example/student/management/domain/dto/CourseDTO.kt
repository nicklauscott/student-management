package com.example.student.management.domain.dto

data class CourseDTO(
    val id: Long? = -1L,
    val courseCode: String = "",
    val courseName: String = "",
    val description: String? = "",
    val creditHours: Int = 80,
    val department: String = "",
    val prerequisites: List<String> =  emptyList()
)
