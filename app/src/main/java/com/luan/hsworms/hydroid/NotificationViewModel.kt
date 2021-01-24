package com.luan.hsworms.hydroid

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

private const val TAG = "NotifivationViewModel"

class NotificationViewModel() : ViewModel() {


    var switchBoolNotification: Boolean = false
    var switchBoolHelpDrink: Boolean = true

    var notificationPreference: SharedPreferences? = null

    fun canSendHelpDrinkNotification(): Boolean {
        // if switchBollNotification is false then
        return if(!switchBoolNotification) false else switchBoolHelpDrink
    }

    fun saveData() {
        notificationPreference?.edit {
            putBoolean("NOTIFICATION", switchBoolNotification)
            putBoolean("HELPDRINK", switchBoolHelpDrink)
            apply()
        }

        Log.d(TAG, "calling saveData() successful")
    }

    fun loadData() {

        val savedNotificationSwitch = notificationPreference?.getBoolean("NOTIFICATION", false)
        val savedHelpDrinkSwitch = notificationPreference?.getBoolean("HELPDRINK", false)

        if (savedNotificationSwitch != null) {
            switchBoolNotification = savedNotificationSwitch
        }

        if (savedHelpDrinkSwitch != null) {
            switchBoolHelpDrink = savedHelpDrinkSwitch
        }

        Log.d(TAG, "calling loadData() successful")
    }
}