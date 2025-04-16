package com.robox.galaxy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val assignments = listOf(
            Assignment(
                "Android Architecture",
                "BCS104: Mobile Application Development",
                "May 20, 2025, 02:59 PM",
                "Submitted",
                "1 file uploaded"
            ),
            Assignment(
                "Introduction To Data Science",
                "BAI103: Data Visualisation ",
                "Apr 22, 2025, 02:59 PM",
                "Overdue"
            ),
            Assignment(
                "Mobile App UI Design",
                "BAS104: Mobile Application Development",
                "Apr 24, 2025, 02:59 PM",
                "Due Soon"
            ),
            Assignment(
                "Environmental Sciences",
                "BAS104: Water Quality",
                "Apr 30, 2025, 02:59 PM",
                "Pending"
            )
        )

        recyclerView.adapter = AssignmentAdapter(assignments)
    }
}

