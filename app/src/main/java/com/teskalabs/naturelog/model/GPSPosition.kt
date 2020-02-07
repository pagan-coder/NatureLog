package com.teskalabs.naturelog.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.support.v4.app.ActivityCompat
import com.google.android.gms.location.*

class GPSPosition(activityForContext: Activity, activityCallback: GPSInterface) {
    private val _INTERVAL: Long = 2000
    private val _FASTESTINTERVAL: Long = 1000

    lateinit var lastLocation: Location
    private lateinit var locationRequest: LocationRequest
    private var fusedLocationProviderClient: FusedLocationProviderClient ?= null

    private val activity = activityForContext
    private val callback = activityCallback

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }

    fun onLocationChanged(location: Location) {
        lastLocation = location
        callback.onLocationChanged(location)
    }

    fun startLocationUpdates() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.setInterval(_INTERVAL)
        locationRequest.setFastestInterval(_FASTESTINTERVAL)

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        } else {
            fusedLocationProviderClient!!.lastLocation.addOnCompleteListener(activity) { task ->
                if (task.isSuccessful && task.result != null) {
                    onLocationChanged((Location(task.result)))
                }
            }
        }

        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun stoplocationUpdates() {
        fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
    }
}
