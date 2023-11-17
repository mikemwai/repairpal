package com.example.mechanic

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mechanic.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}