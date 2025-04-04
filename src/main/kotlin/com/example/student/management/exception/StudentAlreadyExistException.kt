package com.example.student.management.exception


class StudentAlreadyExistException(val id: Long): Exception("Student with the id $id already exist")