package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.databinding.ActivityMechanicLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MechanicLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMechanicLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var usersRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMechanicLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        usersRef = FirebaseDatabase.getInstance().reference.child("users")

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let { firebaseUser ->
                            val userId = firebaseUser.uid
                            val userRef = usersRef.child(userId)

                            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        val isMechanic = dataSnapshot.child("isMechanic").getValue(Boolean::class.java) ?: false

                                        if (isMechanic) {
                                            // User is a mechanic, proceed to mechanic activity
                                            Toast.makeText(
                                                this@MechanicLoginActivity, "Login successful!", Toast.LENGTH_SHORT
                                            ).show()

                                            val intent = Intent(this@MechanicLoginActivity, MechDrawer::class.java)
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            // User is not a mechanic
                                            Toast.makeText(
                                                this@MechanicLoginActivity,
                                                "You are not authorized as a mechanic.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            // Logout the user since they are not a mechanic
                                            FirebaseAuth.getInstance().signOut()
                                        }
                                    } else {
                                        // Handle when user data doesn't exist or role information is not available
                                        // For instance, show an error message or take appropriate action
                                        Log.e(TAG, "User data not found")
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle database error
                                    Log.e(TAG, "onCancelled: ${databaseError.message}")
                                    // You can add code here to handle the error condition appropriately
                                }
                            })
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed. Wrong credentials.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        binding.accountLabel.setOnClickListener {
            startActivity(Intent(this, MechanicRegistrationActivity::class.java))
            finish()
        }

        binding.forgotLabel.setOnClickListener {
            // Implement the logic for password reset here
            // For example:
            // startActivity(Intent(this, PasswordResetActivity::class.java))
        }
    }

    companion object {
        private const val TAG = "MechanicLoginActivity"
    }
}
