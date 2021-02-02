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

        var timeinMillis: Long = 0
        private val TAG = "AlarmService"

        var alarmManager : AlarmManager? = null
        private lateinit var alarmIntent: PendingIntent

        // Function Template to create an alarmManager and an Alarmintent.
        fun setHelpDrinkAlarm(context: Context, switchState: Boolean, hour: Int, minute: Int) {

            // TODO: Add time the user wants to receive repeating notifications as paramater to use in setInexactRepeating()


            Log.d(TAG, "setAlarm called")
            Log.d(TAG, "AlarmManager created")
            // Creating an AlarmManager
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.d(TAG, "AlarmIntent created")

            // Creating an AlarmIntent
            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            // This Calendar objects converts the user desired notification time into a Calendar object.
            val setExactTime: Calendar = Calendar.getInstance().apply {
                timeinMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

            /*
             * switchState is a variable that holds the state of the switch. If the switch is enabled, an alarm will be send to the AlarmReceiver.
             * If the switch is getting disabled while running, any oncoming alarm to AlarmReceiver will be cancelled.
             * If mainActivity::onCreate() with switch being disabled, any alarm will be cancelled, but since there is no alarm, nothing is going to happen.
             * */
            if(switchState) {
                Log.d(TAG, "AlarmManager.setInexactRepeating() called")
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    // TODO: Add function to calculate Hours and minute into milliseconds and put concatinate them
                    // This Parameter lets the notification start upon millisecs after 00:00 (i think this is how it works)
                    setExactTime.timeInMillis,                                      // Vom TimePicker konvertierte Zeit
                    1000 * 10,                                  // Hier muss das Intervall hin also ca. alle 3-4 Stunden
                    alarmIntent
                ).apply {
                    Log.d(TAG, "Alarm has been send")
                }

            } else {
                Log.d(TAG, "cancel Alarm called")
                alarmManager?.cancel(alarmIntent)
            }
        }

        fun setNewIntent(context: Context): Intent {
            Log.d(TAG, "setNewIntent() called")
            return Intent(context, AlarmService::class.java)
        }
    }
}