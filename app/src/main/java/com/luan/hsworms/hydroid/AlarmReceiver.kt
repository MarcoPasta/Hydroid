package com.luan.hsworms.hydroid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlarmReceiver: BroadcastReceiver() {
    private val TAG = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "AlarmReceiver::onReceive called")
        if(context != null) {
            Log.d(TAG, "sendAlarmNotification() called")
            sendAlarmNotification(
                "AlarmManagerChannel",
                1,
                context,
                R.drawable.ic_home,
                "Du musst etwas Trinken",
                "Heute schon etwas getrunken?",
                null,
                NotificationCompat.PRIORITY_DEFAULT
            )
            Log.d(TAG, "Notification was send")
        }
    }

    private fun sendAlarmNotification(
        CHANNEL_ID: String,
        NOTIFICATION_ID: Int,
        context: Context,
        smallIcon: Int,
        contentTitle: CharSequence,
        contentText: CharSequence,
        contentBigText: CharSequence?,      // ? -> Parameter is nullable
        contentPriority: Int
    ) {

        var intent: Intent? = AlarmService.setNewIntent(context)
        var pendingIntent: PendingIntent? = PendingIntent.getActivity(context, 0, intent, 0)

        // Build notification
        // apply allows us to specify more methods within a method
        Log.d(TAG, "Building Notification...")
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(smallIcon)
            setContentTitle(contentTitle)
            setContentText(contentText)
            priority = contentPriority
            contentBigText?.let {
                setStyle(NotificationCompat.BigTextStyle().bigText(it))
            }
            setContentIntent(pendingIntent)
             if(pendingIntent != null)
                 setAutoCancel(true)
        }

        // Create channel (only needed if OS > Oreo/Android 8
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName: String = "AlarmManager"
            val channelDescription: String = "AlarmManager Notification System"
            val channelImportance: Int = NotificationManager.IMPORTANCE_DEFAULT
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