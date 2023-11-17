package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.databinding.ActivityRegistrationBinding


class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
    }
}
