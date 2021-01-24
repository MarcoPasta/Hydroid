package com.luan.hsworms.hydroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationActivity  {

    companion object {
        fun showNotification (
            allowNotification: Boolean,
            CHANNEL_ID: String,
            NOTIFICATION_ID: Int,
            context: Context,
            smallIcon: Int,
            contentTitle: CharSequence,
            contentText: CharSequence,
            contentBigText: CharSequence?,      // ? -> Parameter is nullable
            contentPriority: Int,
            channelName: String,
            channelDescription: String,
            channelImportance: Int,
        )
        {

            if(!allowNotification)
                return

            // Build notification
            // apply allows us to specify more methods within a method
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
                setSmallIcon(smallIcon)
                setContentTitle(contentTitle)
                setContentText(contentText)
                priority = contentPriority
                contentBigText?.let {
                    setStyle(NotificationCompat.BigTextStyle().bigText(it))
                }
            }

            // Create channel (only needed if OS > Oreo/Android 8
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                    description = channelDescription
                }
                val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            // Show notification
            NotificationManagerCompat.from(context).apply {
                notify(NOTIFICATION_ID, builder.build())
            }

        }
    }

}