package com.example.student.management.advice

import com.example.student.management.exception.StudentAlreadyExistException
import com.example.student.management.exception.StudentNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StudentExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class) // This method will handle this kind of exception
    fun handleInvalidArgumentException(ex: MethodArgumentNotValidException): Map<String, String> {
        return buildMap {
            ex.bindingResult.fieldErrors.forEach { error -> // we then get the fields with the error
                put(error.field, error.defaultMessage ?: "Unknown error")
            }
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(ex: StudentNotFoundException): Map<String, String> {
        return mapOf("error message" to "${ex.message}")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StudentAlreadyExistException::class)
    fun handleStudentAlreadyExist(ex: StudentAlreadyExistException): Map<String, String> {
        return mapOf("error message" to "${ex.message}")
    }

}

