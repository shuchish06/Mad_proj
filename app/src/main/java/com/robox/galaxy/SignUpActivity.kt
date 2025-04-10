package com.robox.galaxy

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.robox.galaxy.databinding.SignupBinding
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class AppData (val EnrNo : String ,val courses : List<String> = emptyList())
class SignUpActivity : AppCompatActivity() {
    private lateinit var DataBase :FirebaseFirestore
    private lateinit var binding: SignupBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvSignup.setOnClickListener{
            val intent = Intent(this,FrontPage::class.java)
            startActivity(intent)
        }
        DataBase=FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val dpts = arrayOf("CSE","CSE-AI","ECE")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, dpts)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDepartment.adapter = adapter
        binding.btnSubmit.setOnClickListener{
            val Fname=binding.etName.text.toString().trim()
            val Sname=binding.etSurname.text.toString().trim()
            val Eno=binding.etEnrollment.text.toString().trim()
            val email=binding.etEmail.text.toString().trim()
            val password=binding.etPassword.text.toString().trim()
            val department=binding.spinnerDepartment.selectedItem.toString().trim()
            val selectedGroupId = binding.radioGroup.checkedRadioButtonId
            val group = when (selectedGroupId) {
                com.robox.galaxy.R.id.Group1 -> "Group1"
                com.robox.galaxy.R.id.Group2-> "Group2"
                else -> ""
            }
            if (email.isEmpty() || Fname.isEmpty() || Sname.isEmpty() || Eno.isEmpty() || department.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            if (group.isEmpty()) {
                Toast.makeText(this, "Please select a group", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }
            binding.progressbar.visibility = View.VISIBLE
            val user = hashMapOf(
                "email" to email,
                "Fname" to Fname,
                "Sname" to Sname,
                "Eno"   to Eno,
                "department" to department,
                "password" to password, // Reminder: Hash passwords in production
                "group" to group
            )


            // Add data to Firestore
            DataBase.collection("users")
                .add(user)
                .addOnSuccessListener {
                    createAccount(email,password)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()

                   
                }}


    }
    fun createAccount(email : String ,password :String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    val user=auth.currentUser
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }


    }

}