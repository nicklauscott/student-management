package com.example.student.management.exception

class StudentNotFoundException(val id: Long): Exception("No student with the id $id")

