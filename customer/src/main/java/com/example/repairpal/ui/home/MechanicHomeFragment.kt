package com.example.repairpal.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.repairpal.CarIssueActivity
import com.example.repairpal.InspectionActivity
import com.example.repairpal.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.repairpal.databinding.FragmentMechanicHomeBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.CameraUpdateFactory

class MechanicHomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mechanicGeoLocationRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMechanicHomeBinding.inflate(inflater, container, false)
        val button7: Button = binding.root.findViewById(R.id.startInspectionButton)

        button7.setOnClickListener {
            val intent = Intent(requireContext(), InspectionActivity::class.java)
            startActivity(intent)
        }


        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mechanicGeoLocationRef = FirebaseDatabase.getInstance().reference.child("mechanic_geo_location")

        fetchAndDisplayMechanicLocation()

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        fetchAndDisplayMechanicLocation()
    }


    private fun fetchAndDisplayMechanicLocation() {
        mechanicGeoLocationRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                googleMap.clear() // Clear existing markers before adding new ones
                for (mechanicSnapshot in dataSnapshot.children) {
                    val latitude = mechanicSnapshot.child("l").child("0").value as Double
                    val longitude = mechanicSnapshot.child("l").child("1").value as Double
                    val mechanicLocation = LatLng(latitude, longitude)
                    placeMechanicMarkerOnMap(mechanicLocation)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    private fun placeMechanicMarkerOnMap(location: LatLng) {
        googleMap.addMarker(MarkerOptions().position(location).title("Mechanic Location"))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}