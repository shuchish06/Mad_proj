package com.robox.galaxy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class signupPage : Fragment() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEnrollment: EditText
    private lateinit var etEmail: EditText
    private lateinit var rgDepartment: RadioGroup
    private lateinit var llGroupSelection: LinearLayout
    private lateinit var rgGroup: RadioGroup
    private lateinit var btnSubmit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.signuppage, container, false)

        // Initialize views
        etName = view.findViewById(R.id.etName)
        etSurname = view.findViewById(R.id.etSurname)
        etEnrollment = view.findViewById(R.id.etEnrollment)
        etEmail = view.findViewById(R.id.etEmail)
        rgDepartment = view.findViewById(R.id.rgDepartment)
        llGroupSelection = view.findViewById(R.id.llGroupSelection)
        rgGroup = view.findViewById(R.id.rgGroup)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        // Department selection listener
        rgDepartment.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCSE || checkedId == R.id.rbCSE_AI) {
                llGroupSelection.visibility = View.VISIBLE
            } else {
                llGroupSelection.visibility = View.GONE
            }
        }

        // Submit button click listener
        btnSubmit.setOnClickListener {
            if (validateInputs()) {
                // Process the signup data
                val name = etName.text.toString()
                val surname = etSurname.text.toString()
                val enrollment = etEnrollment.text.toString()
                val email = etEmail.text.toString()

                val department = when (rgDepartment.checkedRadioButtonId) {
                    R.id.rbCSE -> "CSE"
                    R.id.rbCSE_AI -> "CSE-AI"
                    else -> ""
                }

                val group = when (rgGroup.checkedRadioButtonId) {
                    R.id.rbGroup1 -> "Group 1"
                    R.id.rbGroup2 -> "Group 2"
                    else -> ""
                }

                // Show confirmation (you can replace this with your actual signup logic)
                Toast.makeText(
                    requireContext(),
                    "Signup successful!\nName: $name $surname\nEnrollment: $enrollment\nEmail: $email\nDepartment: $department\nGroup: $group",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return view
    }

    private fun validateInputs(): Boolean {
        if (etName.text.isNullOrEmpty()) {
            etName.error = "Please enter your name"
            return false
        }

        if (etSurname.text.isNullOrEmpty()) {
            etSurname.error = "Please enter your surname"
            return false
        }

        if (etEnrollment.text.isNullOrEmpty()) {
            etEnrollment.error = "Please enter your enrollment number"
            return false
        }

        if (etEmail.text.isNullOrEmpty()) {
            etEmail.error = "Please enter your email"
            return false
        }

        if (rgDepartment.checkedRadioButtonId == -1) {
            Toast.makeText(requireContext(), "Please select a department", Toast.LENGTH_SHORT).show()
            return false
        }

        if (llGroupSelection.visibility == View.VISIBLE && rgGroup.checkedRadioButtonId == -1) {
            Toast.makeText(requireContext(), "Please select a group", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            signupPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
    }
}