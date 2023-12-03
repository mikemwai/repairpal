package com.example.repairpal

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.widget.Button
import com.example.repairpal.databinding.ActivityCustomerMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CustomerMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mechanicsRef: DatabaseReference
    private lateinit var requestButton: Button
    private lateinit var binding: ActivityCustomerMapsBinding // Declare binding at the class level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMapsBinding.inflate(layoutInflater) // Inflate the layout using binding
        setContentView(binding.root) // Set content view using binding.root

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mechanicsRef = FirebaseDatabase.getInstance().reference.child("mechanics")

        // Access views using binding
        binding.payButton.setOnClickListener {
            startActivity(Intent(this, CheckoutActivity::class.java))
        }

        requestButton = binding.requestButton
        requestButton.setOnClickListener {
            requestButton.text = "Searching for Mechanics..."
            saveRequestToFirebase()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        fetchUserLocation()
        fetchMechanicsLocations()
    }

    private fun fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val userLatLng = LatLng(location.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(userLatLng).title("Your location"))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 12f))

                val firebaseRef = FirebaseDatabase.getInstance().reference
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    firebaseRef.child("users").child(userId).child("location")
                        .setValue(location)
                }
            }
        }
    }

    private fun fetchMechanicsLocations() {
        val mechanicGeoRef = FirebaseDatabase.getInstance().reference.child("mechanic_geo_location")

        mechanicGeoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mechanicSnapshot in dataSnapshot.children) {
                    val mechanicId = mechanicSnapshot.key ?: ""

                    // Accessing the 'l' and 'g' keys to retrieve latitude and longitude
                    val latitude = mechanicSnapshot.child("l").child("0").value as Double
                    val longitude = mechanicSnapshot.child("l").child("1").value as Double

                    val mechanicLocation = LatLng(latitude, longitude)
                    val mechanicMarker = mMap.addMarker(
                        MarkerOptions().position(mechanicLocation).title("Mechanic $mechanicId")
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }


    private fun calculateDistance(
        userLat: Double,
        userLng: Double,
        mechanicLat: Double,
        mechanicLng: Double
    ): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(userLat, userLng, mechanicLat, mechanicLng, results)
        return results[0]
    }


    private fun placeMechanicMarkerOnMap(location: LatLng) {
        mMap.addMarker(MarkerOptions().position(location).title("Mechanic"))
    }

    private fun saveRequestToFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val requestRef = FirebaseDatabase.getInstance().reference.child("requests").push()

                val request = HashMap<String, Any>()
                request["userId"] = userId ?: ""
                request["latitude"] = location.latitude
                request["longitude"] = location.longitude

                requestRef.setValue(request)
                    .addOnSuccessListener {
                        // Request saved successfully
                        // Add any additional actions here if needed
                    }
                    .addOnFailureListener {
                        // Failed to save request
                    }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
