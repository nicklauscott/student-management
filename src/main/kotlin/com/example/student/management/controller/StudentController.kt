package com.example.student.management.controller

import com.example.student.management.domain.dto.CourseDTO
import com.example.student.management.domain.dto.StudentDTO
import com.example.student.management.service.StudentService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["*"])
@RequestMapping(path = ["v1/students"])
class StudentController(private val studentService: StudentService) {

    @GetMapping(path = ["/advance"])
    fun getStudentsWithPaging(
        @RequestParam("page", required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam("size", required = false, defaultValue = "10") pageSize: Int,
        @RequestParam("sortBy", required = false, defaultValue = "id") sortBy: String,
        @RequestParam("orderType", required = false, defaultValue = "asc") orderType: String
    ): Page<StudentDTO> {
        return studentService.getStudentsByPagingAndSorting(pageNumber, pageSize, sortBy, orderType)
    }

    @GetMapping
    fun getAllStudents(): ResponseEntity<List<StudentDTO>> {
        return ResponseEntity(studentService.getAllStudent(), HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun getStudentById(@PathVariable("id") id: Int): ResponseEntity<StudentDTO> {
        return ResponseEntity(studentService.getStudent(id.toLong()), HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}/courses"])
    fun getStudentCourses(@PathVariable("id") id: Int): ResponseEntity<List<CourseDTO>> {
        return studentService.getStudentCourse(id.toLong())?.let {
            ResponseEntity(it, HttpStatus.OK)
        } ?: ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping
    fun enrollStudent(@Valid @RequestBody student: StudentDTO): ResponseEntity<StudentDTO> {
        return try {
            val newStudent = studentService.saveStudent(student)
            ResponseEntity(newStudent, HttpStatus.OK)
        } catch (_: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(path = ["/update"])
    fun updateStudent(@Valid @RequestBody student: StudentDTO): ResponseEntity<StudentDTO> {
        return try {
            val newStudent = studentService.updateStudent(student)
            ResponseEntity(newStudent, HttpStatus.OK)
        } catch (_: Exception) {
            ResponseEntity(HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(path = ["/{id}/courses"])
    fun enrollStudentToCourse(@PathVariable("id") id: Int, @RequestBody courses: List<CourseDTO>): ResponseEntity<StudentDTO> {
        return ResponseEntity(studentService.enrollStudentToCourse(id.toLong(), courses), HttpStatus.OK)
    }

}