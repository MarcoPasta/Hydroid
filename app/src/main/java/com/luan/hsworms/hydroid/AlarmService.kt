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


class AlarmService() {

    companion object {

        private val TAG = "AlarmService"

        var alarmManager : AlarmManager? = null
        private lateinit var alarmIntent: PendingIntent


        fun setAlarm(context: Context) {
            Log.d(TAG, "setAlarm called")
            Log.d(TAG, "AlarmManager created")
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            Log.d(TAG, "AlarmIntent created")

            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
            Log.d(TAG, "AlarmManager.set() called")

            alarmManager?.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 5 * 1000,
                alarmIntent
            )
        }

        fun setNewIntent(context: Context): Intent {
            Log.d(TAG, "setNewIntent() called")
            return Intent(context, AlarmService::class.java)
        }
    }
}
