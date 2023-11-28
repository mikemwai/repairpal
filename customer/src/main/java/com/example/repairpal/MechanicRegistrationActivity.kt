package com.example.repairpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repairpal.databinding.ActivityMechanicRegistrationBinding

class MechanicRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMechanicRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic_registration)
    }
}


