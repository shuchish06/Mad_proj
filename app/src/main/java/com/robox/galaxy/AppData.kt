package com.robox.galaxy
data class Student(
    var id: String = "",
    var email: String = "",
    var Eno: String = "",
    var Fname: String = "",
    var Sname: String = ""
)

data class Course(
    var id: String = "",
    var name: String = "",
    var description: String = ""
)

data class Enrollment(
    val id: String = "",
    val enrollmentNumber: String = "",
    val courseId: String = ""
)