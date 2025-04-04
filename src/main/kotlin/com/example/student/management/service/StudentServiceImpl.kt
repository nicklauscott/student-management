package com.example.student.management.service

import com.example.student.management.domain.constants.createPageRequest
import com.example.student.management.domain.dto.CourseDTO
import com.example.student.management.domain.dto.StudentDTO
import com.example.student.management.domain.mappers.toCourse
import com.example.student.management.domain.mappers.toDTO
import com.example.student.management.domain.mappers.toStudent
import com.example.student.management.exception.StudentAlreadyExistException
import com.example.student.management.exception.StudentNotFoundException
import com.example.student.management.repository.CourseRepository
import com.example.student.management.repository.StudentRepository
import jakarta.transaction.Transactional
import jakarta.validation.Validator
import org.springframework.boot.CommandLineRunner
import org.springframework.data.domain.Page
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class StudentServiceImpl(
    private val studentRepository: StudentRepository,
    private val courseRepository: CourseRepository,
    private val validator: Validator
): StudentService {

    override fun getStudentsByPagingAndSorting(
        pageNumber: Int, pageSize: Int, sortBy: String?, orderType: String?
    ): Page<StudentDTO> {
        val pageRequest = createPageRequest(pageNumber, pageSize, sortBy, orderType)
        return studentRepository.findAll(pageRequest).map { it.toDTO() }
    }

    override fun getAllStudent(): List<StudentDTO> {
        return studentRepository.findAll().map { it.toDTO() }
    }

    override fun getStudent(id: Long): StudentDTO {
        if (!studentRepository.existsById(id)) {
            throw StudentNotFoundException(id)
        }
        return studentRepository.findById(id).get().toDTO()
    }

    override fun getStudentCourse(id: Long): List<CourseDTO>? {
        val student = studentRepository.findById(id).getOrNull() ?: return null
        return student.course.map { it.toDTO() }
    }

    override fun saveStudent(studentDTO: StudentDTO): StudentDTO? {
        if(studentRepository.existsById(studentDTO.id)) {
            throw StudentAlreadyExistException(studentDTO.id)
        }
        return studentRepository.save(studentDTO.toStudent().copy(id = null)).toDTO()
    }

    override fun updateStudent(studentDTO: StudentDTO): StudentDTO {
        if (!studentRepository.existsById(studentDTO.id)) {
            throw StudentNotFoundException(studentDTO.id)
        }
        return studentRepository.save(studentDTO.toStudent().copy(id = studentDTO.id)).toDTO()
    }

    @Transactional
    override fun enrollStudentToCourse(id: Long, courses: List<CourseDTO>): StudentDTO {
        if (!studentRepository.existsById(id)) {
            throw StudentNotFoundException(id)
        }
        val student = studentRepository.findById(id).get()
        val courseEntities = courses.map { courseDto ->
            courseRepository.findByIdOrNull(courseDto.id)
                ?: courseRepository.save(courseDto.toCourse().copy(id = null)) // Save new courses if not found
        }
        val updatedCourses = student.course + courseEntities
        return studentRepository.save(student.copy(id = id, course = updatedCourses)).toDTO()
    }

}


//@Component
class Runner(
    private val studentRepository: StudentRepository,
): CommandLineRunner {
    override fun run(vararg args: String?) {

        val pageRequest = createPageRequest(195, 5, "lastname", "desc")
        studentRepository.findAll(pageRequest).map { it.toDTO() }.forEach {
            println("id: ${it.id} -> fullName: ${it.firstName} ${it.lastName} -> gender: ${it.gender}")
        }

    }

}