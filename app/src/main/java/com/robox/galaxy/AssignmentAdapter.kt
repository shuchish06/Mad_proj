package com.robox.galaxy



import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AssignmentAdapter(private val items: List<Assignment>) :
    RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val course: TextView = view.findViewById(R.id.course)
        val dueDate: TextView = view.findViewById(R.id.dueDate)
        val status: TextView = view.findViewById(R.id.status)
        val actionBtn: Button = view.findViewById(R.id.actionBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = items[position]
        holder.title.text = assignment.title
        holder.course.text = assignment.courseCode
        holder.dueDate.text = "Due: ${assignment.dueDate}"
        holder.status.text = assignment.status

        // Status background and button color
        when (assignment.status) {
            "Submitted" -> {
                holder.status.setBackgroundResource(R.drawable.status_bg_green)
                holder.actionBtn.text = "View Submission"
                holder.actionBtn.setBackgroundColor(Color.parseColor("#28a745"))
            }
            "Overdue" -> {
                holder.status.setBackgroundResource(R.drawable.status_bg_red)
                holder.actionBtn.text = "Upload Assignment"
                holder.actionBtn.setBackgroundColor(Color.parseColor("#dc3545"))
            }
            "Due Soon" -> {
                holder.status.setBackgroundResource(R.drawable.status_bg_yellow)
                holder.actionBtn.text = "Upload Assignment"
                holder.actionBtn.setBackgroundColor(Color.parseColor("#007bff"))
            }
            else -> {
                holder.status.setBackgroundResource(R.drawable.status_bg_grey)
                holder.actionBtn.text = "Upload Assignment"
                holder.actionBtn.setBackgroundColor(Color.parseColor("#007bff"))
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
