package com.luan.hsworms.hydroid

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import java.util.*

// TODO: Create Documentation
class AlarmService() {

    companion object {

        private val TAG = "AlarmService"

        var alarmManager : AlarmManager? = null
        private lateinit var alarmIntent: PendingIntent

        // Function Template to create an alarmManager and an Alarmintent.
        fun setAlarm(context: Context) {

            // TODO: Add time the user wants to receive repeating notifications as paramater to use in setInexactRepeating()


            Log.d(TAG, "setAlarm called")
            Log.d(TAG, "AlarmManager created")
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.d(TAG, "AlarmIntent created")

            // Müsste geprüft werden ob hier wiederholende Notifications kommen

            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
            Log.d(TAG, "AlarmManager.setInexactRepeating() called")
            alarmManager?.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                // TODO: Add function to calculate Hours and minute into milliseconds and put concatinate them
                // This Parameter lets the notification start upon millisecs after 00:00 (i think this is how it works)
                SystemClock.elapsedRealtime() + 5 * 1000,       // hier muss dann später das TimePicker Objekt hin.
                1000*5,                                          // Hier muss das Intervall hin also ca. alle 3-4 Stunden
                alarmIntent
            )
        }

        fun setNewIntent(context: Context): Intent {
            Log.d(TAG, "setNewIntent() called")
            return Intent(context, AlarmService::class.java)
        }
    }
}
