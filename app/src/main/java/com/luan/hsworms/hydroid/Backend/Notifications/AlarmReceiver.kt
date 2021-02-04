package com.luan.hsworms.hydroid.Backend.Notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.luan.hsworms.hydroid.R
import java.util.*

/*
 *  TODO: When the User archieves his Day goal the repeating notificaion shall be cancelled
 */


class AlarmReceiver: BroadcastReceiver() {

    private val TAG = "AlarmReceiver"

    // Create an Calendar object that represents the current time
    private val currentTime: Calendar = Calendar.getInstance().apply {
        AlarmService.timeinMillis = System.currentTimeMillis()
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "AlarmReceiver::onReceive called")

        val setEndingTime: Calendar = loadEndTimer(context!!)
        val setStartTime: Calendar = loadStartTimer(context)


        // Calling the HelpDrink Notification after receiving a signal
        Log.d(TAG, "|setStartTime: " + setStartTime.time + " | setEndingTime: " + setEndingTime.time + " | currentTime: " + currentTime.time)
        Log.d(TAG, "|setStartTime: " + setStartTime.timeInMillis + "| setEndingTime: " + setEndingTime.timeInMillis + "| currentTime: " + currentTime.timeInMillis)

        // TODO: Get referece from shared preference for starting time.
        // got rid of the context != null here, i don't know why...
        if ((setEndingTime.timeInMillis > currentTime.timeInMillis) && (currentTime.timeInMillis > setStartTime.timeInMillis)) {
            Log.d(TAG, "Zeitüberprüfung war erfolgreich")
            Log.d(TAG, "HelpDrinkNotification() called")
            NotificationActivity.HelpDrinkNotification(
                "HelpDrinkNotificationChannel",
                2,
                context,
                R.drawable.ic_drop_48,
                "Du musst etwas Trinken!",
                "Heute schon etwas getrunken?",
                null,
                NotificationCompat.PRIORITY_DEFAULT
            )
            // Debug
            Log.d(TAG, "Notification was send")
        } else {
            Log.d(TAG, "Zeitüberprüfung war nicht erfolgreich")
        }
    }

    private fun loadEndTimer(context: Context): Calendar {
        // getting a reference to the the sharedPreference (TEHE)
        val sp = context.applicationContext.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)

        // Set values into a Calendarobject and return as inline Object
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, sp.getInt("ENDHOUR", 100))
            set(Calendar.MINUTE, sp.getInt("ENDMINUTE", 100))
            set(Calendar.SECOND, 0)
        }
    }

    private fun loadStartTimer(context: Context): Calendar {
        // getting a reference to the the sharedPreference (TEHE)
        val sp = context.applicationContext.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)

        // Set values into a Calendarobject and return as inline Object
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, sp.getInt("STARTHOUR", 100))
            set(Calendar.MINUTE, sp.getInt("STARTMINUTE", 100))
            set(Calendar.SECOND, 0)
        }
    }

    /*
     * HelpDrinkNotification moved into NotificationActivity.kt
     */
}