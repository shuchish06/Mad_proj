package com.robox.galaxy

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.robox.galaxy.databinding.AssignmentuploadBinding

class Submission : AppCompatActivity() {

    private lateinit var binding: AssignmentuploadBinding
    private var selectedFileUri: Uri? = null
    private val PICK_FILE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AssignmentuploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.chooseFileBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*" // Allow all file types; can be restricted e.g., "application/pdf" for PDFs
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST)
        }

        binding.uploadBtn.setOnClickListener {
            val title = binding.titleEditText.text.toString().trim()
            val comments = binding.commentsBox.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedFileUri == null) {
                Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Simulate successful upload
            Toast.makeText(this, "Assignment uploaded successfully!", Toast.LENGTH_SHORT).show()
            finish()

            // Redirect to FirstActivity and go to Submitted tab (index 2)
            val intent = Intent(this, FirstActivity::class.java)
            intent.putExtra("tabIndex", 2)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            val filename = selectedFileUri?.lastPathSegment?.substringAfterLast("/") ?: "File selected"
            binding.selectedFileText.text = filename
        }
    }
}