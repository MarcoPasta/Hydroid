package com.luan.hsworms.hydroid

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.PreferenceChangeEvent
import kotlin.properties.Delegates

class NotificationFragment : Fragment() , TimePickerDialog.OnTimeSetListener {
    companion object {
        fun newInstance() = NotificationFragment()
    }

    // Constants for easiert debuggin usage
    private val TAG = "NotificationFragment"

    // Initialization of needed variables for interactive ViewModel usage
    // Switches
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var switchBtnNotification: SwitchCompat
    private lateinit var switchBtnHelpDrink: SwitchCompat

    // TextView
    private lateinit var tvTime: TextView

    // ActionButton
    private lateinit var actionButton: FloatingActionButton

    // Time variables
    private var nowHour: Int = 0
    private var nowMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)
        notificationViewModel.notificationPreference = activity?.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
    }

    // Nutzlos für mich
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")

        // Loading Data from Shared Preferences
        notificationViewModel.loadData()
        Log.d(TAG, "Funktioniert nur bei neuinstallation der App")
        Log.d(TAG, "Hour: " + notificationViewModel.hour + ", minute: " + notificationViewModel.minute)
        /*
         * Only important to debug informations about the TextViewTime
         */
        // Log.d(TAG, "loadData() called, hour: " + notificationViewModel.hour + ", minute: " + notificationViewModel.minute)

        // Initializing SwitchButtons
        switchBtnNotification = view.findViewById(R.id.switch_allow_notification)
        switchBtnHelpDrink = view.findViewById(R.id.switch_allow_help_drink)

        // Initialize ActionButton
        actionButton = view.findViewById(R.id.addTimePicker)

        // Initialize TextView
        tvTime = view.findViewById(R.id.setTime)
        // Debug
        Log.d(TAG, "tvTime wird initialisiert")
        Log.d(TAG, "Eingestellt Uhrzeit: " + tvTime.text.toString())

        // Update the current TextView with Data that are in textView.
        // CAUTION! After first installation of the app a bug appears in formatting. Check if hour and minute are not hundred.
        Log.d(TAG, "Check if hour and minute is not 100...")
        if((notificationViewModel.hour != 100) && (notificationViewModel.minute != 100)) {
            updateText()
            Log.d(TAG, "updateText() called")
            Log.d(TAG, "tvTime nach update: " + tvTime.text)
        } else {
            Log.d(TAG, "Hour und Minute == 100, lade Vorlage..")
        }
        // more Debug
        /*Log.d(TAG, "updateText() called")
        Log.d(TAG, "tvTime nach update: " + tvTime.text)*/


        // When button is pressed get the actual state
        switchBtnNotification.isChecked = notificationViewModel.switchBoolNotification
        switchBtnHelpDrink.isChecked = notificationViewModel.switchBoolHelpDrink

        // Enable switchButton "Hilf mir trinken!" if Notifications are allowed
        switchBtnHelpDrink.isEnabled = notificationViewModel.switchBoolNotification

        // OnClickListener for Notification Button
        switchBtnNotification.setOnCheckedChangeListener() { _ , isChecked ->
            // Checks if Notification button is being pressed
            notificationViewModel.switchBoolNotification = isChecked

            // If Notifications are enabled, the HelpDrink Button is also enabled
            switchBtnHelpDrink.isEnabled = isChecked

            // Saved current state of Notification button
            notificationViewModel.saveData()
            // Debugging
            if(notificationViewModel.switchBoolNotification){
                Toast.makeText(
                    context,
                    "Benachrichtigungen sind nun Aktiviert.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "Notification switch active")
            } else {
                Toast.makeText(
                    context,
                    "Benachrichtigungen sind nun Deaktiviert.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "Notificaton switch inactive")
            }
        }


        // OnClickListener for HelpDrink Button
        switchBtnHelpDrink.setOnCheckedChangeListener() { _ , isChecked ->
            // Checks if HelpDrink button is being pressed
            notificationViewModel.switchBoolHelpDrink = isChecked

            // Saves current state of HelpDrink button
            notificationViewModel.saveData()
            // Debugging
            if(notificationViewModel.switchBoolHelpDrink) {
                Toast.makeText(context, "\"Hilf mir trinken!\" ist nun Aktiviert.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "HelpDrink switch active & restarting MainActivity")
                activity?.recreate()
            }
             else {
                Toast.makeText(context, "\"Hilf mir trinken!\" ist nun Deaktiviert.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "HelpDrink switch inactive")
            }
        }


        // OnClickListener for TimePickerDialog button
        actionButton.setOnClickListener() {
            Log.d(TAG, "ActionButton pressed")

            getTime()
            Log.d(TAG, "Current time for displayed on TimePickerDialog is beeing fetched")
            TimePickerDialog(
                requireContext(),
                this,
                nowHour,
                nowMinute,
                true
            ).show()
            Log.d(TAG, "TimePickerDialog has been opened")
        }
    }

    /*
     * This function gets the current time and saves it into some variables so that when the TimePickerDialog open the user can
     * can perform his actions according to the current time
     */
    private fun getTime() {
        Log.d(TAG, "getting the actual time...")
        nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        nowMinute = Calendar.getInstance().get(Calendar.MINUTE)
    }

    // Overriden TimeSetListener, Callback Function to return the values back into onClickListener
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d(TAG, "Calling onTimeSet() Callback Function")
        /*
        * I wrote this function "saveTime" to save the returned values directly into the SharedPreferences. Otherwise there would be
        * the need of some local variables to store the values, but these wouldn't survive another restart of the app.
        */
        Log.d(TAG, "calling saveTime() in onTimeSet()")
        saveTime(hourOfDay, minute)

        // Updating the onScreen text here for the first time after setting another value
        Log.d(TAG, "changing printed time on layout")
        updateText()
        Log.d(TAG, "updating tvTime, now is: " + tvTime.text)

        // Creating a toast so the user can see the change he made
        Toast.makeText(
            requireContext(),
            "Benachrichtigungen fangen um " + formatDate(notificationViewModel.hour, notificationViewModel.minute) + " an",
            Toast.LENGTH_SHORT
        ).show()

        Log.d(TAG, "Allowed notification time has been changed.")

    }

    // Updates the onscreen Text from NotifcationFragment Layout
    private fun updateText() {
        tvTime.text = formatDate(notificationViewModel.hour, notificationViewModel.minute)

    }

    // Saves the time set with TimePickerDialog into SharedPreferences so it can be called and reload again
    private fun saveTime(toSaveHour: Int, toSaveMinute: Int) {
        Log.d(TAG, "called saveTime()")
        notificationViewModel.hour = toSaveHour
        Log.d(TAG, "saving hour, value: " + notificationViewModel.hour)
        notificationViewModel.minute = toSaveMinute
        Log.d(TAG, "saving minute, value: " + notificationViewModel.minute)
        Log.d(TAG, "calling nVM.saveData()")
        notificationViewModel.saveData()
    }

    // Function to format date before output
    private fun formatDate(hour: Int, minute: Int): String {
       Log.d(TAG, "formatting date...")
        val simpleFormat = SimpleDateFormat("HH:mm")

        val timeNow: Date = Date()
        timeNow.hours = hour
        timeNow.minutes = minute

        return simpleFormat.format(timeNow)
    }
}

