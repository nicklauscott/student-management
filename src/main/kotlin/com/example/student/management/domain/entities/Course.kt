package com.example.student.management.domain.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.NamedQuery

@Entity
@NamedQuery(
    query = "UPDATE Course c SET c.creditHours = :newValue WHERE c.id = :id",
    name = "Course.editByQuery"
)
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    val courseCode: String = "",
    val courseName: String = "",
    val description: String?,
    val creditHours: Int = 80,
    val department: String = "",
    val prerequisites: List<String> =  emptyList(), // List of course codes that are required
): BaseEntity()