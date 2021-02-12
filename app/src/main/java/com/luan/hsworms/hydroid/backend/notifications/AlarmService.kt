package com.luan.hsworms.hydroid.backend.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

/**
 * Debugging tag for Logcat.
 */
private const val TAG = "AlarmService"

/**
 * Designed to create functions designed for specific alarms.
 */
class AlarmService {

    companion object {

        /**
         * predefined variable to convert time objects in milliseconds
         */
        var timeinMillis: Long = 0

        /**
         * Predefinition for an AlarmManager object
         */
        private var alarmManager : AlarmManager? = null

        /**
         * Predefinition Intent object
         */
        private lateinit var alarmIntent: PendingIntent

        /**
         * Creates an alarm for the function "Hilf mir trinken!", so it can send notification even when the app is not active or the phone is in idle.
         * Here we create an AlarmManager and AlarmIntent
         *
         * @param context                   Get the context of the broadcast receiver so you can provide it to the intent.
         * @param helpDrinkSwitchState      Representation of the NotifactionFragment switchbutton to allow notifications. If false this function will send an alarmCancel instead.
         * @param hour                      User defined value. States when notifications shall start
         * @param minute                    User defined value. States when notifications shall start
         */
        fun setHelpDrinkAlarm(context: Context, helpDrinkSwitchState: Boolean) {

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
            }

            /*
             * switchState is a variable that holds the state of the switch. If the switch is enabled, an alarm will be send to the AlarmReceiver.
             * If the switch is getting disabled while running, any oncoming alarm to AlarmReceiver will be cancelled.
             * If mainActivity::onCreate() with switch being disabled, any alarm will be cancelled, but since there is no alarm, nothing is going to happen.
             * */
            if(helpDrinkSwitchState) {
                Log.d(TAG, "AlarmManager.setInexactRepeating() called")
                alarmManager?.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    setExactTime.timeInMillis + AlarmManager.INTERVAL_HOUR * 3,         // Puts saved integer into an calender object and recalculates them into milliseconds
                    AlarmManager.INTERVAL_HOUR * 4,                                      // Choosing the interval, 10 seconds for testing purposes
                    alarmIntent
                ).apply {
                    Log.d(TAG, "Alarm has been send")
                }

            } else {
                Log.d(TAG, "cancel Alarm called")
                alarmManager?.cancel(alarmIntent)
            }
        }

        /* Maybe used in the future
        fun setNewIntent(context: Context): Intent {
            Log.d(TAG, "setNewIntent() called")
            return Intent(context, AlarmService::class.java)
        }*/
    }
}