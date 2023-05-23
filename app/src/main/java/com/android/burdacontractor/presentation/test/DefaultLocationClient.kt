//package com.android.burdacontractor.presentation.test
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.location.Location
//import android.location.LocationManager
//import android.os.Looper
//import android.util.Log
//import com.google.android.gms.location.*
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.launch
//
//class DefaultLocationClient(
//    private val context: Context,
//    private val client: FusedLocationProviderClient
//): LocationClient {
//
//    @SuppressLint("MissingPermission")
//    override fun getLocationUpdates(interval: Long): Flow<Location> {
//        return callbackFlow {
//            if(!context.hasLocationPermission()) {
//                throw LocationClient.LocationException("Missing location permission")
//            }
//
//            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//            if(!isGpsEnabled && !isNetworkEnabled) {
//                throw LocationClient.LocationException("GPS is disabled")
//            }
//
//            val request = LocationRequest.create()
//                .setInterval(interval)
//                .setFastestInterval(interval)
//                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
//
//            val locationCallback = object : LocationCallback() {
//                override fun onLocationResult(result: LocationResult) {
//                    result.lastLocation?.let { location ->
//                        launch { send(location)
//                            Log.d("ServiceLocation", "onLocationResult: $location")
//                        }
//                    }
//                }
//            }
//
//            client.requestLocationUpdates(
//                request,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//
//            awaitClose {
//                client.removeLocationUpdates(locationCallback)
//            }
//        }
//    }
//}