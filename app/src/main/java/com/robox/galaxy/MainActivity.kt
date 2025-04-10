package com.robox.galaxy


import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.robox.galaxy.databinding.MainactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth








class MainActivity : AppCompatActivity()
{
    private lateinit var binding: MainactivityBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
        binding=  MainactivityBinding.inflate(layoutInflater)
        val auth = Firebase.auth
        setContentView(binding.root)
        binding.logouticon.setOnClickListener{
            Toast.makeText(this, "Signing Out  ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
            val intent=Intent(this,FrontPage::class.java)
            startActivity(intent)
            auth.signOut()



        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

}