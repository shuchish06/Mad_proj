package com.robox.galaxy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PendingFragment : Fragment() {
    val TAG = "PENDING FRAGMENT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragement_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val noAssignmentsText = view.findViewById<TextView>(R.id.noAssignmentsText)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val assignments = listOf(
            Assignment(
                "Android Architecture",
                "MOBILE APPLICATION DEVELOPMENT",
                "BCS104",
                "May 20, 2025, 02:59 PM",
                "Submitted",
                "1 file uploaded"
            ),
            Assignment(
                "Designing a Calculator App",
                "MOBILE APPLICATION DEVELOPMENT",
                "BCS104",
                "Apr 22, 2025, 02:59 PM",
                "Pending",
                ""
            ),
            Assignment(
                "Mobile App UI Design",
                "MOBILE APPLICATION DEVELOPMENT",
                "BCS104",
                "Apr 24, 2025, 04:39 PM",
                "Pending",
                ""
            ),
            Assignment(
                "Pointers",
                "DATA STRUCTURES",
                "BCS103",
                "Apr 30, 2025, 04:39 PM",
                "Pending",
                ""
            ),
            Assignment(
                "Trees",
                "DATA STRUCTURES",
                "BCS103",
                "May 1, 2025, 05:00 PM",
                "Pending",
                ""
            ),
            Assignment(
                "Numpy Array ",
                "INTRODUCTION TO DATA SCIENCE",
                "BCS103",
                "May 5, 2025, 05:00 PM",
                "Pending",
                ""
            ),
        )

        val currentCourseName = CourseData.UNIcourse?.name ?: ""
        Log.d(TAG, "Current Course Name: $currentCourseName, CourseData: $CourseData, Length: ${currentCourseName.length}")
        Log.d(TAG, "Assignments before filter: $assignments")

        val filteredAssignments = assignments.filter { assignment ->
            val courseToCompare = assignment.course.trim()
            val nameToCompare = currentCourseName.trim()
            val statusMatch = assignment.status.trim().equals("Pending", ignoreCase = true)
            val courseMatch = courseToCompare.equals(nameToCompare, ignoreCase = true)
            val matches = courseMatch && statusMatch
            Log.d(TAG, "Filtering: Assignment course='$courseToCompare' (len=${courseToCompare.length}), status='${assignment.status}' (len=${assignment.status.length}), courseMatch=$courseMatch, statusMatch=$statusMatch, matches=$matches")
            matches
        }
        Log.d(TAG, "FILTERED:$filteredAssignments CURCOURSE:$currentCourseName")

        if (filteredAssignments.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            noAssignmentsText.visibility = View.GONE
            recyclerView.adapter = AssignmentAdapter(filteredAssignments)
        } else {
            recyclerView.visibility = View.GONE
            noAssignmentsText.visibility = View.VISIBLE
            noAssignmentsText.text = "Whoo No assignments Pending!"
        }
    }
}