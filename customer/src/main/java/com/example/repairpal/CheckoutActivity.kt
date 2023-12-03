package com.example.repairpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityCheckoutBinding.inflate(layoutInflater) // Inflate the layout using binding
        setContentView(binding.root)

        binding.confirmButton.setOnClickListener {
            startActivity(Intent(this, PayActivity::class.java))
        }
        binding.declineButton.setOnClickListener {
            startActivity(Intent(this, CustomerMapsActivity::class.java))
        }


    }
}