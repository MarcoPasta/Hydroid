package com.luan.hsworms.hydroid

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel


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
     *  Variable to safe user specified starting and ending time.
     */
    var startHour: Int = 1
    var startMinute: Int = 1
    var endHour: Int = 1
    var endMinute: Int = 1


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
            putInt("STARTHOUR", startHour)
            putInt("STARTMINUTE", startMinute)
            putInt("ENDHOUR", endHour)
            putInt("ENDMINUTE", endMinute)

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
        val savedStartHour = notificationPreference?.getInt("STARTHOUR", 100)
        val savedStartMinute = notificationPreference?.getInt("STARTMINUTE", 100)
        val savedEndingHour = notificationPreference?.getInt("ENDHOUR", 100)
        val savedEndingMinute = notificationPreference?.getInt("ENDMINUTE", 100)


        if (savedNotificationSwitch != null) {
            switchBoolNotification = savedNotificationSwitch
        }

        if (savedHelpDrinkSwitch != null) {
            switchBoolHelpDrink = savedHelpDrinkSwitch
        }

        if(savedStartHour != null){
            startHour = savedStartHour
        }

        if(savedStartMinute != null) {
            startMinute = savedStartMinute
        }

        if(savedEndingHour != null) {
            endHour = savedEndingHour
        }

        if(savedEndingMinute != null) {
            endMinute = savedEndingMinute
        }
        Log.d(TAG, "calling loadData() successful")
    }
}