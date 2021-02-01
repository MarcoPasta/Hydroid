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

/*
 *  TODO: When the User archieves his Day goal the repeating notificaion shall be cancelled
 *  TODO: Add time the user wants to archieve repeating messages to AlarmReceiver
 *  TODO: Device if there should be a TimePickerDialog on when notification should end or if we
 *          should set a constant time for the end of repeating notification
 *
 */


class AlarmReceiver: BroadcastReceiver() {
    private val TAG = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        // Debug
        Log.d(TAG, "AlarmReceiver::onReceive called")

        // Calling the HelpDrink Notification after receiving a signal
        if(context != null) {
            Log.d(TAG, "HelpDrinkNotification() called")
            NotificationActivity.HelpDrinkNotification (
                "HelpDrinkNotificationChannel",
                2,
                context,
                R.drawable.ic_home,
                "Du musst etwas Trinken!",
                "Heute schon etwas getrunken?",
                null,
                NotificationCompat.PRIORITY_DEFAULT
            )
            // Debug
            Log.d(TAG, "Notification was send")
        }
    }

<<<<<<< HEAD
    /*
     * HelpDrinkNotification moved into NotificationActivity.kt
     */
=======

    /*
     * HelpDrinkNotification moved into NotificationActivity.kt
     */





>>>>>>> ac5f00f70feb36172d4e833df5f428713c01a46e
}