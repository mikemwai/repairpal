package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.databinding.ActivityMechanicRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MechanicRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMechanicRegistrationBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMechanicRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            val fullName = binding.fullNameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val isMechanic = binding.mechanicCheckbox.isChecked

            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register the mechanic using Firebase Auth
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Registration success
                        val user = auth.currentUser
                        val userId = user?.uid ?: ""

                        // Create a reference to the "users" collection in Realtime Database
                        val usersRef = FirebaseDatabase.getInstance().reference.child("users")

                        // Create a user object with email, phone, and role
                        val userMap = HashMap<String, Any>()
                        userMap["fullName"] = fullName
                        userMap["email"] = email
                        userMap["phone"] = phone
                        userMap["isMechanic"] = isMechanic // Store mechanic role

                        // Store user information in the database under the user's ID
                        usersRef.child(userId).setValue(userMap)

                        // Display appropriate message based on role
                        if (isMechanic) {
                            Toast.makeText(this, "Mechanic registration successful!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "User registration successful!", Toast.LENGTH_SHORT).show()
                        }

                        navigateToLoginScreen() // Navigate back to the login screen after successful registration
                    } else {
                        // Registration failed
                        Toast.makeText(
                            baseContext, "Registration failed. Please try again later.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, MechanicLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
