package com.example.student.management.domain.mappers

import com.example.student.management.domain.dto.StudentDTO
import com.example.student.management.domain.entities.Student

fun Student.toDTO(): StudentDTO = StudentDTO(
    id = id ?: -1,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    email = email,
    guardianMobile = guardianMobile,
    address =  address,
    enrollmentDate = enrollmentDate,
    program = program,
    department = department,
    status = status,
    course = course.map { it.toDTO() }
)

@JvmName("toDTO_dsl")
fun toDTO(student: Student): StudentDTO = student.toDTO()

fun StudentDTO.toStudent(): Student = Student(
    id = id,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    gender = gender,
    email = email,
    guardianMobile = guardianMobile,
    address =  address,
    enrollmentDate = enrollmentDate,
    program = program,
    department = department,
    status = status,
    course = course.map { it.toCourse() }
)

@JvmName("toStudent_dsl")
fun toStudent(student: StudentDTO): Student = student.toStudent()