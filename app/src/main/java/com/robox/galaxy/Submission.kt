package com.robox.galaxy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.robox.galaxy.databinding.AssignmentuploadBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class Submission : AppCompatActivity() {

    private lateinit var binding: AssignmentuploadBinding
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var enrollmentNumber = ""
    private val TAG = "SubmissionActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AssignmentuploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fetch enrollment number (ID) from Firestore based on the logged-in user's email
        val email = auth.currentUser?.email
        if (email != null) {
            Log.d(TAG, "Fetching user data for email: $email")
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0] // Assuming one user per email
                        Log.d(TAG, "User document found: ${document.data}")
                        enrollmentNumber = document.getString("Eno") ?: ""
                        if (enrollmentNumber.isEmpty()) {
                            Log.w(TAG, "ID field is empty or missing for email: $email")
                            Toast.makeText(this, "Enrollment number not found in profile. Please set it up.", Toast.LENGTH_LONG).show()
                        } else {
                            binding.enrollmentEditText.setText(enrollmentNumber)
                        }
                    } else {
                        Log.w(TAG, "No user document found for email: $email")
                        Toast.makeText(this, "User data not found for email: $email. Please set up your profile.", Toast.LENGTH_LONG).show()
                        // Optionally redirect to a profile setup activity
                        // val intent = Intent(this, ProfileSetupActivity::class.java)
                        // startActivity(intent)
                        // finish()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error fetching user data: ${e.message}", e)
                    Toast.makeText(this, "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.w(TAG, "No user logged in or email not available")
            Toast.makeText(this, "No user logged in or email not available", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Fetch courseId (id) from the tapped course in CourseData
        val tappedCourse = CourseData.UNIcourse
        if (tappedCourse != null) {
            Log.d(TAG, "Tapped course found: ${tappedCourse.id}")
            binding.courseIdEditText.setText(tappedCourse.id)
        } else {
            Log.w(TAG, "No course selected")
            Toast.makeText(this, "No course selected", Toast.LENGTH_SHORT).show()
        }

        // Open Google Drive when the button is clicked

        // Handle upload button click
        binding.uploadBtn.setOnClickListener {
            val assignmentName = binding.titleEditText.text.toString().trim()
            val gdriveLink = binding.gdriveLinkEditText.text.toString().trim()
            enrollmentNumber = binding.enrollmentEditText.text.toString().trim()
            val courseId = binding.courseIdEditText.text.toString().trim()
            val comments = binding.commentsBox.text.toString().trim()

            // Validate inputs
            if (assignmentName.isEmpty()) {
                Toast.makeText(this, "Please enter assignment name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (gdriveLink.isEmpty() || !gdriveLink.startsWith("https://drive.google.com")) {
                Toast.makeText(this, "Please enter a valid Google Drive link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (enrollmentNumber.isEmpty()) {
                Toast.makeText(this, "Please enter enrollment number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (courseId.isEmpty()) {
                Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare submission data
            val submissionId = UUID.randomUUID().toString()
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val submissionData = hashMapOf(
                "submissionId" to submissionId,
                "assignmentName" to assignmentName,
                "gdriveLink" to gdriveLink,
                "enrollmentNumber" to enrollmentNumber,
                "courseId" to courseId,
                "comments" to comments,
                "timestamp" to timestamp
            )

            // Save to Firebase Firestore
            db.collection("submission").document(submissionId)
                .set(submissionData)
                .addOnSuccessListener {
                    Log.d(TAG, "Assignment submitted successfully: $submissionId")
                    Toast.makeText(this, "Assignment submitted successfully!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, FirstActivity::class.java)
                    intent.putExtra("tabIndex", 2)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error submitting assignment: ${e.message}", e)
                    Toast.makeText(this, "Error submitting assignment: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}