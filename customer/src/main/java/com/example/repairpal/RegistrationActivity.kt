package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth // Firebase Auth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.registerButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.password.text.toString().trim()

            // Register user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        // Show a toast indicating successful registration
                        Toast.makeText(
                            baseContext, "User registered successfully!", Toast.LENGTH_SHORT
                        ).show()
                        // Navigate to LoginActivity upon successful registration
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        // If registration fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Registration failed. ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.accountLabel.setOnClickListener {
            // Navigate to LoginActivity if the user already has an account
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "RegistrationActivity"
    }
}
