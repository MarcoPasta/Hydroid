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
    private var savedHour: Int = 0
    private var savedMinute: Int = 0



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
        Log.d(TAG, "loadData() called")

        // Give switchButtons their ID
        switchBtnNotification = view.findViewById(R.id.switch_allow_notification)
        switchBtnHelpDrink = view.findViewById(R.id.switch_allow_help_drink)

        // Give actionButton his id
        actionButton = view.findViewById(R.id.addTimePicker)

        // Give TextView an ID
        tvTime = view.findViewById(R.id.setTime)


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
            // TODO: Save commited time onto Shared Preference so it stays inside the fragment
        }

    }

    private fun getTime() {
        Log.d(TAG, "getting the actual time...")
        nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        nowMinute = Calendar.getInstance().get(Calendar.MINUTE)
    }


    private fun setTime() {
        // I forgot what this function should do maybe for saving the data

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d(TAG, "Calling onTimeSet() Callback Function")
        savedHour = hourOfDay
        savedMinute = minute

        Log.d(TAG, "changing printed time on layout")
        tvTime.text = formatDate(savedHour, savedMinute)
        Toast.makeText(
            requireContext(),
            "Benachrichtigungen fangen um " + formatDate(savedHour, savedMinute) + " an",
            Toast.LENGTH_SHORT
        ).show()

        Log.d(TAG, "Allowed notification time has been changed.")
    }

    private fun formatDate(hour: Int, minute: Int): String {
       Log.d(TAG, "formatting date...")
        val simpleFormat = SimpleDateFormat("HH:mm")

        val timeNow: Date = Date()
        timeNow.hours = hour
        timeNow.minutes = minute

        return simpleFormat.format(timeNow)
    }
}

