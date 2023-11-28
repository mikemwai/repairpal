package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth // Firebase Auth instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Authenticate user with email and password
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        // Show a toast indicating successful login
                        Toast.makeText(
                            baseContext, "Login successful!", Toast.LENGTH_SHORT
                        ).show()
                        // Navigate to MainActivity upon successful login
                        startActivity(Intent(this, DrawerActivity::class.java))
                        finish()
                    } else {
                        // If sign-in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed. Wrong credentials.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.accountLabel.setOnClickListener {
            // Navigate to RegistrationActivity for new user registration
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        binding.forgotLabel.setOnClickListener {
            // Implement the logic for password reset here
            // You can add code to handle password reset functionality
            // For example:
            // startActivity(Intent(this, PasswordResetActivity::class.java))
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}
