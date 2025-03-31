package com.example.student.management

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StudentManagementApplication

fun main(args: Array<String>) {
	runApplication<StudentManagementApplication>(*args)
}