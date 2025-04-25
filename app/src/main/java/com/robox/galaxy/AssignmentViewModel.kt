package com.robox.galaxy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AssignmentViewModel : ViewModel() {

    // Internal MutableLiveData
    private val _assignments = MutableLiveData<List<Assignment>>(
        listOf(
            Assignment("Designing a Calculator App", "MOBILE APPLICATION DEVELOPMENT", "BCS104", "Apr 22, 2025, 02:59 PM", "Pending", ""),
            Assignment("Mobile App UI Design", "MOBILE APPLICATION DEVELOPMENT", "BCS104", "Apr 24, 2025, 04:39 PM", "Pending", ""),
            Assignment("Pointers", "DATA STRUCTURES", "BCS103", "Apr 30, 2025, 04:39 PM", "Pending", ""),
            Assignment("Trees", "DATA STRUCTURES", "BCS103", "May 1, 2025, 05:00 PM", "Pending", ""),
            Assignment("Numpy Array", "INTRODUCTION TO DATA SCIENCE", "BCS103", "May 5, 2025, 05:00 PM", "Pending", ""),
            Assignment("Android Architecture", "MOBILE APPLICATION DEVELOPMENT", "BCS104", "May 20, 2025, 02:59 PM", "Submitted", "1 file uploaded")
        )
    )

    // External immutable LiveData
    val assignments: LiveData<List<Assignment>> = _assignments

    // Function to update an assignment's status to "Submitted"
    fun submitAssignment(assignmentTitle: String) {
        _assignments.value = _assignments.value?.map {
            if (it.title == assignmentTitle) it.copy(status = "Submitted", filesUploaded = "1 file uploaded") else it
        }
    }
}
