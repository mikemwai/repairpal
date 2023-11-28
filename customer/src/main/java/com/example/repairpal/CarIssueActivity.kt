package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.databinding.ActivityCarIssueBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CarIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarIssueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextButton.setOnClickListener {
            // Retrieve selected issues
            val selectedIssues = ArrayList<String>()

            // Retrieve checkbox states and add selected issues to the list
            if (binding.batteryCheckbox.isChecked) {
                selectedIssues.add("Battery")
            }
            if (binding.keylockCheckbox.isChecked) {
                selectedIssues.add("Key Lock")
            }
            if (binding.flatTireCheckbox.isChecked) {
                selectedIssues.add("Flat Tire")
            }
            if (binding.fuelCheckbox.isChecked) {
                selectedIssues.add("Fuel")
            }
            if (binding.towingCheckbox.isChecked) {
                selectedIssues.add("Towing")
            }
            if (binding.otherCheckbox.isChecked) {
                selectedIssues.add("Other")
            }
            // Add other checkbox conditions for different issues

            // Handle when no issue is selected
            if (selectedIssues.isEmpty()) {
                Toast.makeText(this, "Please select at least one issue!", Toast.LENGTH_SHORT).show()
            } else {
                // Pass the selected issues to the next activity or perform desired actions
                // For example, pass it to CustomerMapsActivity
                val intent = Intent(this, CustomerMapsActivity::class.java)
                intent.putStringArrayListExtra("selectedIssues", selectedIssues)
                startActivity(intent)
            }
        }
    }
}

