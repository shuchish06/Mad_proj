package com.robox.galaxy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ViewSubmission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_submission)

        // 🔗 Replace this with your actual Drive file URL
        val fileUrl = "https://docs.google.com/document/d/1NrtjTzXoPqupQ7qpLgc0nmDJbuWlbLUt1OB_GeneUsE/edit?usp=drive_link"

        val downloadButton = findViewById<Button>(R.id.fileDownloadButton)
        downloadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(fileUrl)
            startActivity(intent)
        }
    }
}
