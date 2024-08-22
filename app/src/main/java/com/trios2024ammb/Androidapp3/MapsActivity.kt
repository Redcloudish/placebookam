package com.trios2024ammb.Androidapp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.trios2024ammb.Androidapp3.databinding.ActivityMapsBinding
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    //private var locationRequest: LocationRequest? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupLocationClient()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocation()

        // Added marker on the spa and move camera + address (3275 Hwy 7 Unit 2, Markham, ON L3R 3P9)
        val spa = LatLng(-79.0, 43.0)
        mMap.addMarker(MarkerOptions().position(spa).title("Marker on Spa"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(spa))
    }

    private fun setupLocationClient() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_LOCATION
        )
    }

    companion object {
        private const val REQUEST_LOCATION = 1
        private const val TAG = "MapsActivity"
    }


    private fun getCurrentLocation() {

        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
                    PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {

            requestLocationPermissions()
        }
        else
        {
//
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnCompleteListener {
                val location = it.result
                if (location != null) {

                    val latLng = LatLng(location.latitude, location.longitude)


                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)

                    mMap.moveCamera(update)
                } else {

                    Log.e(TAG, "No location found")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.size == 2 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED && grantResults [1] ==
                PackageManager.PERMISSION_GRANTED ) {
                getCurrentLocation()
            }
            else {
                Log.e(TAG, "Location permission denied")
            }
        }
    }
}