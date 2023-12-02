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
import com.google.android.gms.maps.MapView
import androidx.fragment.app.Fragment
import com.example.repairpal.InspectionActivity
import com.example.repairpal.databinding.FragmentMechanicHomeBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
class MechanicHomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentMechanicHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMechanicHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        database = FirebaseDatabase.getInstance().reference

        val button7: Button = binding.startInspectionButton
        button7.setOnClickListener {
            fetchAndSaveMechanicLocation()
            showDataSavedAlert()
            val intent = Intent(requireContext(), InspectionActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

        // Add a default marker (San Francisco, for example)
        val defaultLocation = LatLng(37.7749, -122.4194)
        val markerOptions = MarkerOptions().position(defaultLocation).title("Mechanic's Location")
        googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))

        fetchAndSaveMechanicLocation()
    }

    private fun fetchAndSaveMechanicLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                database.child("mechanicLocation").setValue("${location.latitude}, ${location.longitude}")

                val mechanicLocation = LatLng(location.latitude, location.longitude)
                val markerOptions = MarkerOptions().position(mechanicLocation).title("Mechanic's Location")
                googleMap.clear()
                googleMap.addMarker(markerOptions)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mechanicLocation, 12f))
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to fetch location: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showDataSavedAlert() {
        val message = "The data has been saved."
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
