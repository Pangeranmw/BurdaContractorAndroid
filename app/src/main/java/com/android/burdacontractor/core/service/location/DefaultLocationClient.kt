package com.android.burdacontractor.core.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import com.android.burdacontractor.core.utils.hasLocationPermission
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
): LocationClient {

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if(!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Missing location permission")
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if(!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("GPS is disabled")
            }

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                .setMinUpdateDistanceMeters(1f)
                .build()

//            val builder = LocationSettingsRequest.Builder()
//                .addLocationRequest(request)
//            val clientSetting = LocationServices.getSettingsClient(context)
//            clientSetting.checkLocationSettings(builder.build())
//                .addOnSuccessListener {
////                    getMyLastLocation()
//                }
//                .addOnFailureListener { exception ->
//                    if (exception is ResolvableApiException) {
//                        try {
//                            IntentSenderRequest.Builder(exception.resolution).build()
//                        } catch (sendEx: IntentSender.SendIntentException) {
//                            Toast.makeText(context, sendEx.message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}