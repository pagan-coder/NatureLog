package com.teskalabs.naturelog.model

import android.location.Location

interface GPSInterface {
    fun onLocationChanged(location: Location)
}
