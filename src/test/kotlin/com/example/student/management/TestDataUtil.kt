package com.example.student.management

import com.example.student.management.domain.dto.StudentDTO
import com.example.student.management.domain.entities.Course
import com.example.student.management.domain.entities.Gender
import com.example.student.management.domain.entities.Student
import com.example.student.management.domain.entities.StudentStatus
import java.time.LocalDateTime

fun testStudentA(): StudentDTO = StudentDTO(
    firstName = "John",
    lastName = "Doe",
    dateOfBirth = LocalDateTime.now(),
    gender = Gender.OTHER,
    email = "johndoe@example.com",
    guardianMobile = "+12-223-54563",
    address = "Laos",
    enrollmentDate = LocalDateTime.now(),
    program = "History",
    department = "Science",
    status = StudentStatus.ACTIVE,
    course = listOf()
)

fun testStudentB(): StudentDTO = StudentDTO(
    firstName = "Jane",
    lastName = "Smith",
    email = "janesmith@example.com",
    dateOfBirth = LocalDateTime.now(),
    gender = Gender.FEMALE,
    guardianMobile = "+14-432-565",
    address = "Java",
    enrollmentDate = LocalDateTime.now(),
    program = "Art",
    department = "Fine Art",
    status = StudentStatus.ACTIVE,
    course = listOf()
)


fun testCourseA(): Course = Course(
    courseCode = "B-1723", courseName = "Mathematics",
    description = "Calculus", creditHours = 45, department = "Maths",
    prerequisites = listOf()
)

fun testCourseB(): Course = Course(
    courseCode = "B-8793", courseName = "English",
    description = "Phonics", creditHours = 45, department = "Literature",
    prerequisites = listOf()
)

fun testStudentWithCourse(): Pair<Student, Course> {
    return Student(
        firstName = "Mark", lastName = "Anthony", email = "markanthony@example.com", gender = Gender.MALE
    ) to Course(
        courseCode = "SWC-6372", courseName = "Physic", description = "Fluid Dynamics"
    )
}

val constantDateTime: LocalDateTime = LocalDateTime.of(1, 1, 1, 1, 1)

fun StudentDTO.validate(): StudentDTO = this.copy(
    id = -1, dateOfBirth = constantDateTime, enrollmentDate = constantDateTime
)

fun Student.validate(): Student = this.copy(
    id = -1, dateOfBirth = constantDateTime, enrollmentDate = constantDateTime
)