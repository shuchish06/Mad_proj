package com.robox.galaxy

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class ViewSubmission : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var fileNameTextView: TextView
    private lateinit var assignmentNameTextView: TextView
    private lateinit var timestampTextView: TextView
    private lateinit var timeTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_submission)

        // Initialize TextViews
        fileNameTextView = findViewById(R.id.fileName)
        assignmentNameTextView = findViewById(R.id.assignmentName)
        timestampTextView = findViewById(R.id.timestampTextView)
        timeTextView = findViewById(R.id.timeTextView)

        // Initialize Firestore
        db = Firebase.firestore

        // Fetch the data from Firestore
        fetchSubmissionData()
    }

    private fun fetchSubmissionData() {
        // Get submissionId from Intent (this should match the Firestore document ID)
        val submissionId = intent.getStringExtra("SUBMISSION_ID") ?: "default_submission_id"

        // Query Firestore for the document with the given submissionId
        db.collection("submission").document(submissionId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Retrieve the fields
                    val gdriveLink = document.getString("https://drive.google.com/drive/folders/1ub7LZMvur63uShQzmU3ubQ55FVI8mjkH")
                    val assignmentName = document.getString("assignmentName")
                    val timestamp = document.getString("timestamp")

                    // Update TextViews
                    fileNameTextView.text = gdriveLink ?: "No file link found"
                    assignmentNameTextView.text = assignmentName ?: "Unknown Assignment"

                    // Handle timestamp and time
                    if (timestamp != null) {
                        try {
                            // Parse the timestamp (assuming format like "2025-04-05 23:23:22")
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val parsedDate = dateFormat.parse(timestamp)

                            // Format for date (e.g., "4/5/2025")
                            val dateOutputFormat = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
                            timestampTextView.text = parsedDate?.let { dateOutputFormat.format(it) } ?: "No date"

                            // Format for time (e.g., "11:23:22 PM")
                            val timeOutputFormat = SimpleDateFormat("h:mm:ss a", Locale.getDefault())
                            timeTextView.text = parsedDate?.let { timeOutputFormat.format(it) } ?: "No time"
                        } catch (e: Exception) {
                            // Fallback if timestamp format is unexpected
                            timestampTextView.text = timestamp
                            timeTextView.text = "Unknown time"
                            Toast.makeText(this, "Error parsing timestamp", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        timestampTextView.text = "No timestamp available"
                        timeTextView.text = "No time available"
                    }

                    // Show Toast if any field is missing
                    if (gdriveLink == null || assignmentName == null || timestamp == null) {
                        Toast.makeText(this, "Some data not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    fileNameTextView.text = "Submission not found"
                    assignmentNameTextView.text = "Submission not found"
                    timestampTextView.text = "Submission not found"
                    timeTextView.text = "Submission not found"
                    Toast.makeText(this, "Submission not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                fileNameTextView.text = "Error loading data"
                assignmentNameTextView.text = "Error loading data"
                timestampTextView.text = "Error loading data"
                timeTextView.text = "Error loading data"
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}