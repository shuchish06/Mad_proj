package com.robox.galaxy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class SignupFragment : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEnrollment: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerDepartment: Spinner
    private lateinit var btnSubmit: Button
    private lateinit var tvSignup: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.signup, container, false)

        // Initialize views
        etName = view.findViewById(R.id.etName)
        etSurname = view.findViewById(R.id.etSurname) // Corrected line
        etEnrollment = view.findViewById(R.id.etEnrollment)
        etEmail = view.findViewById(R.id.etEmail)
        spinnerDepartment = view.findViewById(R.id.spinnerDepartment)
        btnSubmit = view.findViewById(R.id.btnSubmit)
        tvSignup = view.findViewById(R.id.tvSignup)

        // Set click listeners
        btnSubmit.setOnClickListener { handleSubmit() }
        tvSignup.setOnClickListener { handleSignupClick() }

        return view
    }

    private fun handleSubmit() {
        val name = etName.text.toString().trim()
        val surname = etSurname.text.toString().trim()
        val enrollment = etEnrollment.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val department = spinnerDepartment.selectedItem.toString()

        // Basic validation
        if (name.isEmpty() || surname.isEmpty() || enrollment.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Here you would typically send the data to a server or process it
        Toast.makeText(
            context,
            "Submitted: $name $surname\nEnrollment: $enrollment\nEmail: $email\nDepartment: $department",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun handleSignupClick() {
        // Handle navigation to login/signup screen
        Toast.makeText(context, "Navigate to Login Screen", Toast.LENGTH_SHORT).show()
        // You might want to replace this with actual navigation logic
        // For example:
        // findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }

    companion object {
        fun newInstance() = SignupFragment()
    }
}