package com.example.student.management.controller

import com.example.student.management.domain.dto.CourseDTO
import com.example.student.management.domain.dto.StudentDTO
import com.example.student.management.service.StudentService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest  @Autowired constructor(
    private val mockMvc: MockMvc, @MockkBean val studentService: StudentService
) {

    val objectMapper = ObjectMapper()

    @Test
    fun `test that getStudentById returns HTTP 200 on valid id`() {
        every { studentService.getStudent(any()) } answers { StudentDTO() }
        mockMvc.get("/v1/students/1").andExpect { status { isOk() } }
    }

    @Test
    fun `test that getAllStudents returns HTTP 200 on valid id`() {
        every { studentService.getAllStudent() } answers { emptyList() }
        mockMvc.get("/v1/students")
            .andExpect { status { isOk() } }
            .andExpect { content { emptyList<StudentDTO>() } }
    }

    @Test
    fun `test that getStudentCourses returns HTTP 200 on valid id`() {
        every { studentService.getStudentCourse(1) } answers { listOf(CourseDTO()) }
        mockMvc.get("/v1/students/1/courses")
            .andExpect { status { isOk() } }
            .andExpect { content { emptyList<CourseDTO>() } }
    }

    @Test
    fun `test that enrollStudent returns HTTP 200 on a valid request body`() {
        every { studentService.saveStudent(any()) } answers { StudentDTO() }
        mockMvc.post("/v1/students") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = """
        {
            "id": 1255,
            "firstName": "Victoria",
            "lastName": "Miller",
            "dateOfBirth": "1991-01-01T08:00:00",
            "gender": "FEMALE",
            "email": "greengreen@outlook.com",
            "guardianMobile": "1234567890",
            "address": "",
            "enrollmentDate": "2025-03-29T21:37:15.693715",
            "program": "Fine Arts",
            "department": "Literature",
            "status": "ACTIVE",
            "course": []
        }
            """.trimIndent()
        }   .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun `test that enrollStudent returns HTTP 400 on an invalid field in request body`() {
        every { studentService.saveStudent(any()) } answers { StudentDTO() }
        mockMvc.post("/v1/students") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = """
        {
            "id": 1255,
            "firstName": "Victoria",
            "lastName": "Miller",
            "dateOfBirth": "1991-01-01T08:00:00",
            "gender": "FEMALE",
            "email": "greengreen@outlook.com",
            "guardianMobile": "invalid",
            "address": "",
            "enrollmentDate": "2025-03-29T21:37:15.693715",
            "program": "Fine Arts",
            "department": "Literature",
            "status": "ACTIVE",
            "course": []
        }
            """.trimIndent()
        }   .andExpect { status { isBadRequest() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun `test that updateStudent returns HTTP 200 on a valid request body`() {
        every { studentService.updateStudent(any()) } answers { StudentDTO() }
        mockMvc.post("/v1/students/update") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = """
        {
            "id": 1255,
            "firstName": "Victoria",
            "lastName": "Miller",
            "dateOfBirth": "1991-01-01T08:00:00",
            "gender": "FEMALE",
            "email": "greengreen@outlook.com",
            "guardianMobile": "1234567890",
            "address": "",
            "enrollmentDate": "2025-03-29T21:37:15.693715",
            "program": "Fine Arts",
            "department": "Literature",
            "status": "ACTIVE",
            "course": []
        }
            """.trimIndent()
        }   .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun `test that updateStudent returns HTTP 400 on an invalid request body`() {
        every { studentService.saveStudent(any()) } answers { StudentDTO() }
        mockMvc.post("/v1/students/update") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }   .andExpect { status { isBadRequest() } }
    }

    @Test
    fun `test that enrollStudentToCourse returns HTTP 200 on valid id`() {
        // Note: We're using toLong() on the ID as shown in your controller
        every { studentService.enrollStudentToCourse(1, any()) } answers {
            StudentDTO(course = listOf(
                CourseDTO(courseCode = "2838", courseName = "Mathematics", description = "Calculus")
            ))
        }
        mockMvc.post("/v1/students/1/courses") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(listOf(
                CourseDTO(courseCode = "2838", courseName = "Mathematics", description = "Calculus")
            ))
        }   .andExpect { status { isOk() } }
            .andExpect { content { contentType(MediaType.APPLICATION_JSON) } }
    }

    @Test
    fun `test that enrollStudentToCourse returns HTTP 400 on if request body is not present`() {
        mockMvc.post("/v1/students/1/courses") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        } .andExpect { status { isBadRequest() } }
    }

}