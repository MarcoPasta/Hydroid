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


class AlarmService: BroadcastReceiver() {

    private val TAG = "alarmServiceTest"

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "AlarmReceiver called", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onReceive: called")
    }

    companion object {

        var alarmManager : AlarmManager? = null
        private lateinit var alarmIntent: PendingIntent


        fun setAlarm(context: Context) {
            alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(context, AlarmService::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }

            alarmManager?.let{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                }else {

                }

            }


            alarmManager?.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10 * 1000,
                alarmIntent
            )
          }


    }



}