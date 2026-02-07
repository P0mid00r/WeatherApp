package ru.pomidorka.weatherapp.util

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.annotation.RequiresPermission
import java.util.Locale

class LocationTracker(val context: Context) {
//    LocationTracker(this).getCurrentLocation { location ->
//        Log.d("TEST", "${location.latitude} ${location.longitude}")
//        val geocoder = Geocoder(this, Locale.getDefault())
//        geocoder.getFromLocation(location.latitude, location.longitude, 1)?.first()?.let {
//            val country = it.countryName
//            val admin1 = it.adminArea
//            val cityName = it.subAdminArea
//            Log.d("TEST", "$country, $admin1, $cityName")
//        }
//    }

    private val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getCurrentLocation(locationChanged: (Location) -> Unit) {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME,
            LOCATION_REFRESH_DISTANCE,
            object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationChanged(location)
                }
            }
        )
    }

    companion object {
        const val LOCATION_REFRESH_TIME = 15000L
        const val LOCATION_REFRESH_DISTANCE = 500f
    }
}