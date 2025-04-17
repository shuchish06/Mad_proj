package com.robox.galaxy

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent

// Define the CourseData singleton
object CourseData {
    var UNIcourse: Course? = null
}

class CourseDisplay(private val courses: MutableList<Course>) :
    RecyclerView.Adapter<CourseDisplay.ViewHolder>() {

    private val TAG = "CourseDisplay"

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.courseName)
        val description: TextView = itemView.findViewById(R.id.courseDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course, parent, false)
        Log.d(TAG, "onCreateViewHolder: Inflated view for position")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        Log.d(TAG, "onBindViewHolder: Binding course at position $position - Name: ${course?.name}, Description: ${course?.description}")
        holder.name.text = course?.name ?: "No Name"
        holder.description.text = course?.description ?: "No Description"

        // Set click listener to update CourseData and start FirstActivity
        holder.itemView.setOnClickListener {
            // Update the singleton with the tapped course
            CourseData.UNIcourse = course
            Log.d(TAG, "Item clicked: Set UNIcourse to ${CourseData.UNIcourse?.name}, Full course: $course")

            val context = holder.itemView.context
            val intent = Intent(context, FirstActivity::class.java)
            // Pass tabIndex as extra (example: 0 for the selected tab)
            intent.putExtra("tabIndex", 0)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = courses.size
}