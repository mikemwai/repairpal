package com.example.mechanic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mechanic.VehicleSelectionActivity
import com.example.mechanic.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, VehicleSelectionActivity::class.java))
        }
    }
}