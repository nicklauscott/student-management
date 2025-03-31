package com.example.student.management.domain.constants

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

fun createSort(sortBy: String, orderType: String): Sort.Order? {
    if (!listOf("firstname", "lastName", "dateOfBirth", "gender", "id").contains(sortBy.lowercase())) return null
    val column = when (sortBy.lowercase()) {
        "firstname" -> "firstName"
        "lastname" -> "lastName"
        in listOf("age", "dob", "birthdate", "dateofbirth") -> "dateOfBirth"
        "gender" -> "gender"
        "id" -> "id"
        else -> return null
    }
    return when (orderType.lowercase()) {
        "ascending" -> Sort.Order.asc(column)
        "asc" -> Sort.Order.asc(column)
        "descending" -> Sort.Order.desc(column)
        "desc" -> Sort.Order.desc(column)
        else -> null
    }
}

fun createPageRequest(pageNumber: Int, pageSize: Int, sortBy: String?, orderType: String?): PageRequest {
    return if (sortBy == null || orderType == null) PageRequest.of(pageNumber, pageSize)
    else createSort(sortBy, orderType)?.let { PageRequest.of(pageNumber, pageSize, Sort.by(it)) }
        ?: PageRequest.of(pageNumber, pageSize)
}

