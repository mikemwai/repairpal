package com.example.repairpal.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.repairpal.R
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.repairpal.InspectionActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.example.repairpal.databinding.FragmentMechanicHomeBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MechanicHomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap
    private lateinit var customerLocationRef: DatabaseReference
    private lateinit var mechanicLocationRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMechanicHomeBinding.inflate(inflater, container, false)

        // Find the MapView from the layout
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        customerLocationRef = FirebaseDatabase.getInstance().reference.child("customer_location")
        mechanicLocationRef = FirebaseDatabase.getInstance().reference.child("mechanic_location")

        fetchMechanicLocation()

        val startInspectionButton: Button = binding.root.findViewById(R.id.startInspectionButton)
        startInspectionButton.setOnClickListener {
            val intent = Intent(requireActivity(), InspectionActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        displayCustomerLocation()
    }

    private fun fetchMechanicLocation() {
        // Check location permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        // Fetch mechanic's current location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    val mechanicId = FirebaseAuth.getInstance().currentUser?.uid
                    val mechanicLocation = LocationData(location.latitude, location.longitude)
                    mechanicId?.let {
                        mechanicLocationRef.child(mechanicId).setValue(mechanicLocation)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    fetchCustomerLocation()
                                } else {
                                    // Handle failure to save mechanic location
                                }
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                // Handle failure to retrieve the location
                Toast.makeText(requireContext(), "Failed to get location: $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchCustomerLocation() {
        val mechanicId = FirebaseAuth.getInstance().currentUser?.uid
        mechanicId?.let { mechId ->
            customerLocationRef.child(mechId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val customerLocation = dataSnapshot.getValue(LocationData::class.java)
                    customerLocation?.let {
                        val customerLatLng = LatLng(it.latitude, it.longitude)
                        val markerOptions = MarkerOptions().position(customerLatLng)
                            .title("Customer's Location")
                        googleMap.addMarker(markerOptions)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLatLng, 12f))
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
    private fun displayCustomerLocation() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { mechanicId ->
            customerLocationRef.child(mechanicId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val customerLocation = dataSnapshot.getValue(LocationData::class.java)
                        customerLocation?.let {
                            val customerLatLng = LatLng(customerLocation.latitude, customerLocation.longitude)
                            val markerOptions = MarkerOptions().position(customerLatLng).title("Customer's Location")
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(customerLatLng, 12f))
                            googleMap.addMarker(markerOptions)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
        }
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}

data class LocationData(val latitude: Double = 0.0, val longitude: Double = 0.0)
