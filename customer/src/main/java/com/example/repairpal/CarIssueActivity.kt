package com.example.repairpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityCarIssueBinding


class CarIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarIssueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.nextButton.setOnClickListener {
            startActivity(Intent(this, CustomerMapsActivity::class.java))
        }
    }
}