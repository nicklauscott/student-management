package com.example.student.management.domain.mappers

import com.example.student.management.domain.dto.CourseDTO
import com.example.student.management.domain.entities.Course

fun Course.toDTO(): CourseDTO = CourseDTO(
    id = id ?: -1L,
    courseCode = courseCode,
    courseName = courseName,
    description = description,
    creditHours = creditHours,
    department = department,
    prerequisites = prerequisites
)

@JvmName("toDTO_dsl")
fun toDTO(course: Course): CourseDTO = course.toDTO()

fun CourseDTO.toCourse(): Course = Course(
    id = id,
    courseCode = courseCode,
    courseName = courseName,
    description = description,
    creditHours = creditHours,
    department = department,
    prerequisites = prerequisites
)

@JvmName("toCourse_dsl")
fun toCourse(course: CourseDTO): Course = course.toCourse()