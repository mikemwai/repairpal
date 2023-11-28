package com.example.repairpal

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.repairpal.R
import android.widget.TextView
import com.example.repairpal.databinding.ActivityDrawerBinding
import com.google.firebase.auth.FirebaseAuth

class DrawerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarDrawer.toolbar)

        // Initialize Firebase Auth
        val mAuth = FirebaseAuth.getInstance()

        // Get the current user
        val currentUser = mAuth.currentUser

        currentUser?.let {
            val userEmail = it.email

            // Get the NavigationView and header view
            val navView: NavigationView = binding.navView
            val headerView = navView.getHeaderView(0)

            // Find the TextView in the header layout
            val textView = headerView.findViewById<TextView>(R.id.textView)

            // Set the user's email to the TextView in the header
            textView.text = userEmail
        }

        binding.appBarDrawer.fab.setOnClickListener { view ->
            // ... Your existing FAB logic
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_drawer)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
