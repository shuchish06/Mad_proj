package com.robox.galaxy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.robox.galaxy.databinding.FrontpageBinding


public class FrontPage : AppCompatActivity() {
    private lateinit var binding: FrontpageBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FrontpageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.tvSignUP.setOnClickListener {
            signUpUser()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            binding.progressBar.visibility = View.VISIBLE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Log.d("Auth", "signInWithEmail:success")
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        val intent =Intent(this , MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w("Auth", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Email/Password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUpUser() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
//            Toast.makeText(this, "Logging as  ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        finish()}
    }
}