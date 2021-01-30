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

    private val TAG = "alarmServiceTest"

    companion object {

        var alarmManager : AlarmManager? = null
        private lateinit var alarmIntent: PendingIntent


        fun setAlarm(context: Context) {
            Log.d("Alarm", "setAlarm called")
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            alarmManager?.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10 * 1000,
                alarmIntent
            )
        }

        fun setNewIntent(context: Context): Intent {
            return Intent(context, AlarmService::class.java)
        }
    }
}
