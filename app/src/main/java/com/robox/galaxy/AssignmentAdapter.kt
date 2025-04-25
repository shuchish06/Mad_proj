package com.robox.galaxy

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AssignmentAdapter(
    private val items: List<Assignment>,
    private val onUploadClick: ((Assignment) -> Unit)? = null
) : RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val course: TextView = view.findViewById(R.id.course)
        val dueDate: TextView = view.findViewById(R.id.dueDate)
        val status: TextView = view.findViewById(R.id.status)
        val actionBtn: Button = view.findViewById(R.id.actionBtn)

        fun bind(assignment: Assignment, onUploadClick: ((Assignment) -> Unit)?) {
            title.text = assignment.title
            course.text = assignment.courseCode
            dueDate.text = "Due: ${assignment.dueDate}"
            status.text = assignment.status

            when (assignment.status) {
                "Submitted" -> {
                    status.setBackgroundResource(R.drawable.status_bg_green)
                    actionBtn.text = "View Submission"
                    actionBtn.setBackgroundColor(Color.parseColor("#28a745"))
                }
                "Pending" -> {
                    status.setBackgroundResource(R.drawable.status_bg_red)
                    actionBtn.text = "Upload Assignment"
                    actionBtn.setBackgroundColor(Color.parseColor("#ff6f61"))
                }
                "Due Soon" -> {
                    status.setBackgroundResource(R.drawable.status_bg_yellow)
                    actionBtn.text = "Upload Assignment"
                    actionBtn.setBackgroundColor(Color.parseColor("#007bff"))
                }
                else -> {
                    status.setBackgroundResource(R.drawable.status_bg_grey)
                    actionBtn.text = "Upload Assignment"
                    actionBtn.setBackgroundColor(Color.parseColor("#007bff"))
                }
            }

            actionBtn.setOnClickListener {
                if (assignment.status == "Submitted") {
                    val context = itemView.context
                    val intent = Intent(context, ViewSubmission::class.java).apply {
                        putExtra("assignmentTitle", assignment.title)
                        putExtra("courseCode", assignment.courseCode)
                        putExtra("dueDate", assignment.dueDate)
                    }
                    context.startActivity(intent)
                } else {
                    onUploadClick?.invoke(assignment) ?: run {
                        val context = itemView.context
                        val intent = Intent(context, Submission::class.java).apply {
                            putExtra("assignmentTitle", assignment.title)
                            putExtra("courseCode", assignment.courseCode)
                            putExtra("dueDate", assignment.dueDate)
                        }
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        holder.bind(items[position], onUploadClick)

    }

    override fun getItemCount(): Int = items.size
}






