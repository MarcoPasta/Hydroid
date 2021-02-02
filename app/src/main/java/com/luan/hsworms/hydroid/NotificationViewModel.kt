package com.luan.hsworms.hydroid

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*


/**
 * Class for storing the switch button statements after the app was closed
 */
class NotificationViewModel() : ViewModel() {

    private val TAG = "NotifivationViewModel"

    /**
     * Boolean for the NotificationFragment switch button that asks if the app is allowed to send the user some notifications
     */
    var switchBoolNotification: Boolean = false

    /**
     * Boolean for the button "Help me Drink!" that asks if the app is allowed to recommend the user when he should drink something
     */
    var switchBoolHelpDrink: Boolean = true

    /**
     *  Variable to safe specified time
     */
    var hour: Int = 1
    var minute: Int = 1


    /**
     * Object needed to safe the state of the switches locally
     */
    var notificationPreference: SharedPreferences? = null

    /**
     * customized getter method for switchBoolHelpDrink
     * @return Always false when switchBoolNotification is false
     */
    fun canSendHelpDrinkNotification(): Boolean {
        // if switchBollNotification is false then
        return if(!switchBoolNotification) false else switchBoolHelpDrink
    }

    /**
     * Saves the state of the switch buttons as sharedPrefences
     */
    fun saveData() {
        notificationPreference?.edit {
            putBoolean("NOTIFICATION", switchBoolNotification)
            putBoolean("HELPDRINK", switchBoolHelpDrink)
            putInt("HOUR", hour)
            putInt("MINUTE", minute)
            apply()
        }

        Log.d(TAG, "calling saveData() successful")
    }

    /**
     * loads the saved buttons when loading a fragment
     */
    fun loadData() {

        val savedNotificationSwitch = notificationPreference?.getBoolean("NOTIFICATION", false)
        val savedHelpDrinkSwitch = notificationPreference?.getBoolean("HELPDRINK", false)
        val savedHour = notificationPreference?.getInt("HOUR", 100)
        val savedMinute = notificationPreference?.getInt("MINUTE", 100)


        if (savedNotificationSwitch != null) {
            switchBoolNotification = savedNotificationSwitch
        }

        if (savedHelpDrinkSwitch != null) {
            switchBoolHelpDrink = savedHelpDrinkSwitch
        }

        if(savedHour != null){
            hour = savedHour
        }

        if(savedMinute != null) {
            minute = savedMinute
        }

        Log.d(TAG, "calling loadData() successful")
    }
}