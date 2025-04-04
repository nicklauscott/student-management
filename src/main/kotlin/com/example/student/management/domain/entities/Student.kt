package com.example.student.management.domain.entities

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

@Entity
data class Student(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(nullable = false) val firstName: String = "",
    @Column(nullable = false) val lastName: String = "",

    val dateOfBirth: LocalDateTime = LocalDateTime.now(),
    @Enumerated(EnumType.STRING) val gender: Gender = Gender.OTHER,

    val email: String = "",

    @Column(nullable = false) val guardianMobile: String = "",
    val address: String = "",
    val enrollmentDate: LocalDateTime = LocalDateTime.now(),
    val program: String = "",
    val department: String = "",
    @Enumerated(EnumType.STRING) val status: StudentStatus = StudentStatus.ACTIVE,

    @ManyToMany(cascade = [CascadeType.MERGE, CascadeType.PERSIST], fetch = FetchType.EAGER)
    @JoinTable(
        name = "student_courses",
        joinColumns = [ JoinColumn(name = "student_id") ],
        inverseJoinColumns = [ JoinColumn(name = "course_id") ]
    )
    val course: List<Course> = emptyList(),
): BaseEntity()

enum class Gender {
    MALE, FEMALE, OTHER
}

enum class StudentStatus {
    ACTIVE, GRADUATED, SUSPENDED, DROPPED_OUT
}
