package com.example.repairpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityIssuesListBinding

class IssuesList : AppCompatActivity() {
    private lateinit var binding: ActivityIssuesListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIssuesListBinding.inflate(layoutInflater) // Inflate the layout using binding
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            startActivity(Intent(this, CompleteActivity::class.java))
        }
    }
}