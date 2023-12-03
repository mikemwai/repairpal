package com.example.repairpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*

class InspectionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var requestRef: DatabaseReference
    private lateinit var mechanicGeoRef: DatabaseReference
    private lateinit var btnProceedToPayment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspection)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        requestRef = FirebaseDatabase.getInstance().reference.child("requests")
        mechanicGeoRef = FirebaseDatabase.getInstance().reference.child("mechanic_geo_location")

        btnProceedToPayment = findViewById(R.id.btnProceedToPayment)

        btnProceedToPayment.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        // Fetch the latest request location
        requestRef.limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (requestSnapshot in dataSnapshot.children) {
                    val requestLatitude = requestSnapshot.child("latitude").value as Double
                    val requestLongitude = requestSnapshot.child("longitude").value as Double

                    val requestLocation = LatLng(requestLatitude, requestLongitude)
                    mMap.addMarker(MarkerOptions().position(requestLocation).title("Request Location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(requestLocation, 12f))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })

        // Fetch the mechanic's location
        mechanicGeoRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (mechanicSnapshot in dataSnapshot.children) {
                    val latitude = mechanicSnapshot.child("l").child("0").value as Double
                    val longitude = mechanicSnapshot.child("l").child("1").value as Double

                    val mechanicLocation = LatLng(latitude, longitude)
                    mMap.addMarker(MarkerOptions().position(mechanicLocation).title("Mechanic Location"))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    // Required for MapView lifecycle
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
