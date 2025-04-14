package com.robox.galaxy


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SubmittedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_submitted, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val assignments = listOf(
            Assignment(
                "Literature Review: Modern Novels",
                "LIT303: Contemporary Literature",
                "Apr 11, 2025, 04:40 PM",
                "Submitted",
                "2 files uploaded"
            )
        )

        recyclerView.adapter = AssignmentAdapter(assignments)
    }
}
