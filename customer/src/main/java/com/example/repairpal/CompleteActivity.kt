package com.example.repairpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityCompleteBinding

class CompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityCompleteBinding.inflate(layoutInflater) // Inflate the layout using binding
        setContentView(binding.root)

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, DrawerActivity::class.java))
        }
    }
}