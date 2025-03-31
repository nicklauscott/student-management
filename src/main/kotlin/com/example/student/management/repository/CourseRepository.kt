package com.example.student.management.repository

import com.example.student.management.domain.entities.Course
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface CourseRepository: JpaRepository<Course, Long> {

    @Modifying
    @Transactional
    fun editByQuery(id: Long, newValue: Int)
}