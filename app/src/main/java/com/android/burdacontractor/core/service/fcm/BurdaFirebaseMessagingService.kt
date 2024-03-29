package com.android.burdacontractor.core.service.fcm

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
import com.android.burdacontractor.core.data.source.local.storage.SessionManager
import com.android.burdacontractor.core.domain.model.Constant
import com.android.burdacontractor.core.utils.getPhotoUrl
import com.android.burdacontractor.core.utils.toIntegerNumber
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class BurdaFirebaseMessagingService: FirebaseMessagingService() {

    @Inject lateinit var storageRepository: StorageRepository
    private var intentId: String? = null
    private var clickAction: String? = null
    private var title: String? = null
    private var messageBody: String? = null
    private var imageUrl: String? = null
    private var channelId: String? = null

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG ,"Refreshed token $token")
        storageRepository.setPreferences(SessionManager.KEY_DEVICE_TOKEN, token)
    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        imageUrl = message.notification?.imageUrl.toString()
        messageBody = message.notification?.body
        title = message.notification?.title
        clickAction = message.notification?.clickAction
        channelId = getString(R.string.default_notification_channel_id)
        intentId = message.data["intentId"]
        sendNotification()
    }

    private fun sendNotification() {
        val channelName = getString(R.string.default_notification_channel_name)
        val intent = Intent(clickAction).putExtra(Constant.INTENT_ID, intentId)
        val contentPendingIntent = PendingIntent.getActivity(
            this, messageBody.toString().toIntegerNumber(), intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notificationBuilder = NotificationCompat.Builder(this, channelId.toString())
            .setSmallIcon(R.drawable.logo_burda)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(title)
            .setContentText(messageBody)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        val notificationSummaryBuilder = NotificationCompat.Builder(this, channelId.toString())
            .setSmallIcon(R.drawable.logo_burda)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(title).setGroupSummary(true)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        imageUrl?.let{
            applyImageUrl(notificationBuilder, getPhotoUrl(it))
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationBuilder.setChannelId(channelId.toString())
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.apply {
            notify(
                intentId!!,
                messageBody.toString().toIntegerNumber(),
                notificationBuilder.build()
            )
            notify(title!!.toIntegerNumber(), notificationSummaryBuilder.build())
        }
    }

    private fun applyImageUrl(
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
        private const val TAG = "BurdaFirebaseMessagingService"
    }

}