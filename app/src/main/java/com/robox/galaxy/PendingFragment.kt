package com.robox.galaxy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PendingFragment : Fragment() {
    val TAG = "PENDING FRAGMENT"

    private val viewModel: AssignmentViewModel by activityViewModels()

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

        viewModel.assignments.observe(viewLifecycleOwner, Observer { assignments ->
            val currentCourseName = CourseData.UNIcourse?.name ?: ""

            val filteredAssignments = assignments.filter { assignment ->
                assignment.course.trim().equals(currentCourseName.trim(), ignoreCase = true)
                        && assignment.status.equals("Pending", ignoreCase = true)
            }

            if (filteredAssignments.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                noAssignmentsText.visibility = View.GONE

                val adapter = AssignmentAdapter(filteredAssignments) { assignment ->
                    viewModel.submitAssignment(assignment.title)
                    val intent = Intent(requireContext(), Submission::class.java).apply {
                        putExtra("assignmentTitle", assignment.title)
                        putExtra("courseCode", assignment.courseCode)
                        putExtra("dueDate", assignment.dueDate)
                    }
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            } else {
                recyclerView.visibility = View.GONE
                noAssignmentsText.visibility = View.VISIBLE
                noAssignmentsText.text = "Whoo No assignments Pending!"
            }
        })
    }
}