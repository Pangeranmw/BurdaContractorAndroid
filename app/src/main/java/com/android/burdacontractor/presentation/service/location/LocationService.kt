package com.android.burdacontractor.presentation.service.location

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import com.android.burdacontractor.R
import com.android.burdacontractor.core.data.LogisticRepository
import com.android.burdacontractor.core.data.StorageRepository
import com.android.burdacontractor.core.domain.model.LogisticCoordinate
import com.android.burdacontractor.core.domain.model.enums.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var locationClient: LocationClient

    @Inject
    lateinit var logisticRepository: LogisticRepository

    @Inject
    lateinit var storageRepository: StorageRepository

    private lateinit var userId: String
    private lateinit var userRole: String

//    private val binder = LocalBinder()
//
//    var currentLocation: MutableSharedFlow<Location> = MutableSharedFlow()
//
    override fun onBind(intent: Intent): IBinder? {
        return null
//        return binder
    }
//
//    inner class LocalBinder : Binder() {
//        fun getService(): LocationService = this@LocationService
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("NotificationPermission")
    private fun start() {
        userRole = storageRepository.getRole()
        userId = storageRepository.getUserId()
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking Your location:")
            .setContentText("Current Location: ...")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient
            .getLocationUpdates(UPDATE_INTERVAL)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
//                currentLocation.emit(location)
                val lat = location.latitude.toString()
                val long = location.longitude.toString()
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                if(userRole==UserRole.LOGISTIC.name){
                    val logisticCoordinate = LogisticCoordinate(lat.toDouble(),long.toDouble())
                    logisticRepository.setCoordinate(userId, logisticCoordinate)
                }
                storageRepository.setCoordinate(lat,long)
                notificationManager.notify(1000, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1000, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val UPDATE_INTERVAL = 1000L
    }
}