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

            Log.d(TAG, "HelpDrinkNotification() called")
            NotificationActivity.HelpDrinkNotification(
                "HelpDrinkNotificationChannel",
                2,
                context,
                R.drawable.ic_home,
                "Du musst etwas Trinken!",
                "Heute schon etwas getrunken?",
                null,
                NotificationCompat.PRIORITY_DEFAULT
            )
            Log.d(TAG, "Notification was send")
        }
    }

    // HelpDrinkNotification moved into NotificationActivity.kt

    /* TODO: When the User archieves his Day goal the repeating notificaion shall be cancelled
    */



}