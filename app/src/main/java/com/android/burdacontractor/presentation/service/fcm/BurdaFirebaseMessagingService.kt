package com.android.burdacontractor.presentation.service.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.burdacontractor.R
import com.android.burdacontractor.core.data.StorageRepository
import com.android.burdacontractor.presentation.splashscreen.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class BurdaFirebaseMessagingService: FirebaseMessagingService() {

    @Inject lateinit var storageRepository: StorageRepository

    override fun onNewToken(token: String) {
//        super.onNewToken(token)
        Log.d(TAG ,"Refreshed token $token")
        storageRepository.setDeviceToken(token)
    }
    override fun onMessageReceived(message: RemoteMessage) {
//        super.onMessageReceived(message)
        if(message.data.isNotEmpty()){
            Log.d(TAG, "onMessageReceived: "+ message.data.toString())
            sendNotification(message.notification?.title, message.notification?.body, message.notification?.imageUrl.toString())
        }
    }

    private fun sendNotification(title: String?, messageBody: String?, imageUrl: String?) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)
        val contentIntent = Intent(this, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        if (imageUrl!=null) applyImageUrl(notificationBuilder, imageUrl)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationBuilder.setChannelId(channelId)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(Calendar.getInstance().timeInMillis.toInt(), notificationBuilder.build())
    }

    fun applyImageUrl(
        builder: NotificationCompat.Builder,
        imageUrl: String
    ) = runBlocking {
        val url = URL(imageUrl)

        withContext(Dispatchers.IO) {
            try {
                val input = url.openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
        }?.let { bitmap ->
            builder.setLargeIcon(bitmap)
        }
    }
    companion object {
        private val TAG = "BurdaFirebaseMessagingService"
        private const val NOTIFICATION_ID = 0
    }

}