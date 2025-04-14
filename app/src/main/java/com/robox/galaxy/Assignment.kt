package com.robox.galaxy

data class Assignment(
    val title: String,
    val courseCode: String,
    val dueDate: String,
    val status: String,
    val filesUploaded: String = ""
)
