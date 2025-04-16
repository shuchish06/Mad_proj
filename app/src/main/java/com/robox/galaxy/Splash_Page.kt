package com.robox.galaxy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.jvm.java


class Splash_Page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_page)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@Splash_Page, FrontPage::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }
}