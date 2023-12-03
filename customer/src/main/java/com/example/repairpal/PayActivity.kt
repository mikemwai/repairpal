package com.example.repairpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityPayBinding

class PayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityPayBinding.inflate(layoutInflater) // Inflate the layout using binding
        setContentView(binding.root)

        binding.payNow.setOnClickListener {
            startActivity(Intent(this, CompleteActivity::class.java))
        }


    }
}