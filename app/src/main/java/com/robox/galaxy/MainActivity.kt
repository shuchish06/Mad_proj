package com.robox.galaxy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.robox.galaxy.databinding.MainactivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainactivityBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CourseDisplay // Assuming this is defined elsewhere
    private val courses = mutableListOf<Course>()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "MainActivity" // Define TAG for Logcat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Starting MainActivity")

        // Use View Binding
        binding = MainactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.coursesRecyclerView)
        if (recyclerView == null) {
            Log.e(TAG, "onCreate: coursesRecyclerView not found in layout")
        } else {
            Log.d(TAG, "onCreate: RecyclerView initialized")
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = CourseDisplay(courses) // Ensure CourseDisplay is defined
            recyclerView.adapter = adapter
            Log.d(TAG, "onCreate: Adapter set to RecyclerView")
        }

        Log.d(TAG, "onCreate: Fetching enrolled courses")
        fetchEnrolledCourses()

        // Logout button click listener
        binding.logouticon.setOnClickListener {
            val currentUserEmail = auth.currentUser?.email ?: "Unknown"
            Log.d(TAG, "logouticon clicked: Signing out user: $currentUserEmail")
            Toast.makeText(this, "Signing Out $currentUserEmail", Toast.LENGTH_SHORT).show()
            auth.signOut()
            val intent = Intent(this, FrontPage::class.java)
            startActivity(intent)
            Log.d(TAG, "logouticon clicked: Sign out completed, navigating to FrontPage")
        }
    }

    private fun fetchEnrolledCourses() {
        Log.d(TAG, "fetchEnrolledCourses: Starting fetch process")
        val user = auth.currentUser
        if (user == null) {
            Log.w(TAG, "fetchEnrolledCourses: No user logged in")
            Toast.makeText(this@MainActivity, "Please log in to see your courses", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d(TAG, "fetchEnrolledCourses: User logged in with email: ${user.email}")

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                Log.d(TAG, "fetchEnrolledCourses: Querying users collection for email: ${user.email}")
                db.collection("users")
                    .whereEqualTo("email", user.email)
                    .get()
                    .addOnSuccessListener { studentResult ->
                        Log.d(TAG, "fetchEnrolledCourses: User query succeeded, result size: ${studentResult.size()}")
                        if (studentResult.isEmpty) {
                            Log.i(TAG, "fetchEnrolledCourses: No user found for email: ${user.email}")
                            Toast.makeText(this@MainActivity, "No student found", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }

                        val document = studentResult.documents[0]
                        Log.d(TAG, "fetchEnrolledCourses: Raw document data: ${document.data}")
                        val student = document.toObject(Student::class.java)
                        if (student == null) {
                            Log.e(TAG, "fetchEnrolledCourses: Failed to convert user data")
                            Toast.makeText(this@MainActivity, "Error getting student data", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        Log.d(TAG, "fetchEnrolledCourses: Student fetched: $student")

                        val enrollmentNumber = student.Eno
                        Log.d(TAG, "fetchEnrolledCourses: Fetched enrollment number (Eno): $enrollmentNumber")
                        if (enrollmentNumber.isEmpty()) {
                            Log.w(TAG, "fetchEnrolledCourses: No Eno found, value: '$enrollmentNumber'")
                            Toast.makeText(this@MainActivity, "No enrollment number found", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        Log.d(TAG, "fetchEnrolledCourses: Valid Eno found: $enrollmentNumber")

                        db.collection("Enrollments")
                            .whereEqualTo("Eno", enrollmentNumber)
                            .get()
                            .addOnSuccessListener { enrollmentResult ->
                                Log.d(TAG, "fetchEnrolledCourses: Enrollment query succeeded, result size: ${enrollmentResult.size()}")
                                val courseIds = mutableListOf<String>()
                                for (doc in enrollmentResult) {
                                    val courseId = doc.getString("courseID") // Note: courseID (case-sensitive)
                                    if (courseId != null) {
                                        courseIds.add(courseId)
                                        Log.d(TAG, "fetchEnrolledCourses: Found course ID: $courseId")
                                    } else {
                                        Log.w(TAG, "fetchEnrolledCourses: courseId is null in document: ${doc.id}")
                                    }
                                }

                                if (courseIds.isEmpty()) {
                                    Log.w(TAG, "fetchEnrolledCourses: No course IDs found in enrollments")
                                    adapter.notifyDataSetChanged()
                                    return@addOnSuccessListener
                                }

                                courses.clear()
                                Log.d(TAG, "fetchEnrolledCourses: Cleared courses list, new size: ${courses.size}")
                                var coursesFetched = 0
                                for (courseId in courseIds) {
                                    db.collection("Courses")
                                        .document(courseId)
                                        .get()
                                        .addOnSuccessListener { courseDoc ->
                                            Log.d(TAG, "fetchEnrolledCourses: Course document fetched for ID: $courseId")
                                            val course = courseDoc.toObject(Course::class.java)
                                            Log.d(TAG, "fetchEnrolledCourses: Course document fetched for ID: $course")
                                            if (course != null) {
                                                course.id = courseDoc.id
                                                courses.add(course)
                                                Log.d(TAG, "fetchEnrolledCourses: Added course: $course")
                                            } else {
                                                Log.e(TAG, "fetchEnrolledCourses: Failed to convert course data for ID: $courseId")
                                            }
                                            Log.d(TAG, "Courses: $courses")

                                            coursesFetched++
                                            if (coursesFetched == courseIds.size) {
                                                Log.d(TAG, "Coursesqq: $courses")
                                                Log.d(TAG, "fetchEnrolledCourses: Notifying adapter of data change, courses size: ${courses.size}")
                                                adapter.notifyDataSetChanged()
                                            }
                                        }
                                        .addOnFailureListener { error ->
                                            Log.e(TAG, "fetchEnrolledCourses: Error loading course for ID $courseId: ${error.message}")
                                            Toast.makeText(this@MainActivity, "Error loading a course: ${error.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                            .addOnFailureListener { error ->
                                Log.e(TAG, "fetchEnrolledCourses: Error loading enrollments: ${error.message}")
                                Toast.makeText(this@MainActivity, "Error loading enrollments: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener { error ->
                        Log.e(TAG, "fetchEnrolledCourses: Error finding user: ${error.message}")
                        Toast.makeText(this@MainActivity, "Error finding user: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Log.e(TAG, "fetchEnrolledCourses: Unexpected error: ${e.message}")
                Toast.makeText(this@MainActivity, "Something went wrong: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed: Navigating back")
        super.onBackPressed()
        moveTaskToBack(true)
    }
}