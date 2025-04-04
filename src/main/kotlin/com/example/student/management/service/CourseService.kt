package com.example.student.management.service

import com.example.student.management.domain.dto.CourseDTO
import com.example.student.management.domain.dto.StudentDTO
import org.springframework.data.domain.Page

interface StudentService {
    fun getStudentsByPagingAndSorting(
        pageNumber: Int, pageSize: Int, sortBy: String?, orderType: String?
    ): Page<StudentDTO>

    fun getAllStudent(): List<StudentDTO>

    fun getStudent(id: Long): StudentDTO?

    fun getStudentCourse(id: Long): List<CourseDTO>?

    fun saveStudent(studentDTO: StudentDTO): StudentDTO?

    fun updateStudent(studentDTO: StudentDTO): StudentDTO?

    fun enrollStudentToCourse(id: Long, courses: List<CourseDTO>): StudentDTO?

}