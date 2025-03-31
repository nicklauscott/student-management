package com.example.student.management.service

import com.example.student.management.*
import com.example.student.management.domain.entities.Course
import com.example.student.management.domain.entities.Gender
import com.example.student.management.domain.entities.Student
import com.example.student.management.domain.mappers.toDTO
import com.example.student.management.domain.mappers.toStudent
import com.example.student.management.repository.CourseRepository
import com.example.student.management.repository.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.DirtiesContext
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // destroy & create new instance of h2 database
class StudentServiceTest  @Autowired constructor (
    private val underTest: StudentServiceImpl,
    private val studentRepository: StudentRepository,
    private val courseRepository: CourseRepository
) {

    private var student: Student? = null
    private var course: Course? = null

    @BeforeEach
    fun setUp() {
        studentRepository.save(testStudentB().toStudent().copy(id = null))
        course = courseRepository.save(testCourseB().copy(id = null))

        val studentWithCourse = testStudentWithCourse()
        courseRepository.save(studentWithCourse.second)
        val savedCourse = courseRepository.findAll().find { it.courseCode == studentWithCourse.second.courseCode }
        if (savedCourse != null) {
            // Use the managed entity from the database
            studentRepository.save(studentWithCourse.first.copy(course = listOf(savedCourse)))
        }


    }

    @AfterEach
    fun tearDown() {
        course = null
        student = null
    }

    // getStudentsByPagingAndSorting
    @Test
    fun `test that getStudentsByPagingAndSorting returns a list of student in desc by their id's`() {
        val page = underTest.getStudentsByPagingAndSorting(0, 5, "id", "desc")
        val students = page.content
        assertThat(students.first().id).isEqualTo(2)
    }

    @Test
    fun `test that getStudentsByPagingAndSorting returns a list of student in asc by their id's`() {
        val page = underTest.getStudentsByPagingAndSorting(0, 5, "id", "asc")
        val students = page.content
        assertThat(students.first().id).isEqualTo(1)
    }

    @Test
    fun `test that getStudentsByPagingAndSorting returns a list of student in desc by their gender`() {
        val page = underTest.getStudentsByPagingAndSorting(0, 5, "gender", "desc")
        val students = page.content
        assertThat(students.first().gender).isEqualTo(Gender.MALE)
    }

    @Test
    fun `test that getStudentsByPagingAndSorting returns a list of student in desc by their last name`() {
        val page = underTest.getStudentsByPagingAndSorting(0, 5, "lastname", "desc")
        val students = page.content
        assertThat(students.first().lastName).isEqualTo("Smith")
    }

    @Test
    fun `test that getStudentsByPagingAndSorting returns a list of student in desc by their first name`() {
        val page = underTest.getStudentsByPagingAndSorting(0, 5, "firstname", "desc")
        val students = page.content
        assertThat(students.first().firstName).isEqualTo("Mark")
    }

    @Test
    fun `test that getStudentsByPagingAndSorting returns the right page data`() {
        val page = underTest.getStudentsByPagingAndSorting(1, 1, "firstname", "desc")
        val students = page.content
        assertThat(page.pageable.pageNumber).isEqualTo(1)
        assertThat(page.pageable.pageSize).isEqualTo(1)
        assertThat(students.first().firstName).isEqualTo("Jane")
    }


    // getAllStudent
    @Test
    fun `test that getAllStudent returns a list of student`() {
        val student = underTest.getAllStudent()
        assertThat(student.size).isEqualTo(2)
    }

    // getStudent
    @Test
    fun `test that getStudent returns a student`() {
        val student = underTest.getStudent(1)
        assertThat(student).isNotNull
    }

    @Test
    fun `test that getStudent returns null if student a student doesn't exist`() {
        val student = underTest.getStudent(3)
        assertThat(student).isNull()
    }



    // getStudentCourse
    @Test
    fun `test that getStudentCourse returns list of course belonging to the student`() {
        val course = underTest.getStudentCourse(2)
        assertThat(course).isNotNull
        assertThat(course!!.first().courseCode).isEqualTo(testStudentWithCourse().second.courseCode)
    }



    // saveStudent
    @Test
    fun `test that saveStudent persist in the database`() {
        val saveStudent = underTest.saveStudent(testStudentA())
        assertThat(saveStudent!!.id).isNotNull()
        val recalledStudent = studentRepository.findByIdOrNull(saveStudent.id)
        assertThat(recalledStudent).isNotNull
        assertThat(recalledStudent?.toDTO()?.validate()).isEqualTo(testStudentA().validate())
    }

    @Test
    fun `test that saveStudent will throw an exception if an exist user is passed in`() {
        assertThrows<IllegalStateException> {
            // testStudentB was inserted on setUp so it now has the id = 1
            underTest.saveStudent(testStudentA().copy(id = 1))
        }
    }



    // enrollStudentToCourse
    @Test
    fun `test that enrollStudentToCourse can persist an new course and assign it to a student`() {
        val updatedStudent = underTest.enrollStudentToCourse(1, listOf(course!!.toDTO()))
        assertThat(updatedStudent).isNotNull
        val studentFromDb = studentRepository.findByIdOrNull(1)
        assertThat(studentFromDb).isNotNull
        assertThat(updatedStudent!!.course.first().courseCode).isEqualTo(studentFromDb!!.course.first().courseCode)

    }

    @Test
    fun `test that enrollStudentToCourse will save a new course and assign it to a student`() {
        val newCourse = testCourseA().toDTO().copy(id = 3) // we already have two courses in the database
        val updateStudent = underTest.enrollStudentToCourse(1, listOf(newCourse))
        val allCourse = courseRepository.findAll()
        assertThat(allCourse.size).isEqualTo(3)
        assertThat(updateStudent).isNotNull
        assertThat(updateStudent!!.course.find { it.courseCode == newCourse.courseCode }).isNotNull
        assertThat(
            updateStudent.course.first { it.courseCode == newCourse.courseCode }.courseCode
        ).isEqualTo(newCourse.courseCode)
    }

    @Test
    fun `test that enrollStudentToCourse will doing nothing id the user id doesn't exist`() {
        val newCourse = testCourseA().toDTO().copy(id = 3)
        val updateStudent = underTest.enrollStudentToCourse(
            3, // we already have two students in the database
            listOf(newCourse)
        )
        assertThat(updateStudent).isNull()
    }


}
