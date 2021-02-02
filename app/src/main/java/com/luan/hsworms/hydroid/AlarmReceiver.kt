package com.luan.hsworms.hydroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import java.util.*

/*
 *  TODO: When the User archieves his Day goal the repeating notificaion shall be cancelled
 *  TODO: Add time the user wants to archieve repeating messages to AlarmReceiver
 *  TODO: Device if there should be a TimePickerDialog on when notification should end or if we
 *          should set a constant time for the end of repeating notification
 *
 */


class AlarmReceiver: BroadcastReceiver() {

    private val TAG = "AlarmReceiver"

    // Create an Calendar object that represents the current time
    private val currentTime: Calendar = Calendar.getInstance().apply {
        AlarmService.timeinMillis = System.currentTimeMillis()
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "AlarmReceiver::onReceive called")

        val setEndingTime: Calendar = loadDataAndCreateCalendar(context!!)


        // Calling the HelpDrink Notification after receiving a signal
        Log.d(TAG, "setEndingTime: " + setEndingTime.time + ", currentTime: " + currentTime.time)
        Log.d(TAG, "setEndingTime: " + setEndingTime.timeInMillis + ", currentTime: " + currentTime.timeInMillis)

        // TODO: Get referece from shared preference for starting time.
        // got rid of the context != null here, i don't know why...
        if ((setEndingTime.timeInMillis > currentTime.timeInMillis) /*&& (currentTime.timeInMillis < startingTime)*/) {
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
            // Debug
            Log.d(TAG, "Notification was send")
        }

    }

    private fun loadDataAndCreateCalendar(context: Context): Calendar {
        // getting a reference to the the sharedPreference (TEHE)
        val sp = context.applicationContext.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)

        // Set values into a Calendarobject and return as inline Object
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, sp.getInt("HOUR", 100))
            set(Calendar.MINUTE, sp.getInt("MINUTE", 100))
            set(Calendar.SECOND, 0)
        }
    }


    /*
     * HelpDrinkNotification moved into NotificationActivity.kt
     */
}