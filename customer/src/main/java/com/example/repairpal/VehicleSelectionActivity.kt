package com.example.repairpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityDrawerBinding
import com.example.repairpal.databinding.ActivityLoginBinding
import com.example.repairpal.databinding.ActivityVehicleSelectionBinding

class VehicleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button7.setOnClickListener {
            startActivity(Intent(this, CarIssueActivity::class.java))
        }

    }
}