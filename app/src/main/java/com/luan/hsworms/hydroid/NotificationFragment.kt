package com.luan.hsworms.hydroid

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import java.util.prefs.PreferenceChangeEvent

class NotificationFragment : Fragment() {
    companion object {
        fun newInstance() = NotificationFragment()
    }

    // Constants for easiert debuggin usage
    private val TAG = "NotificationFragment"

    // Initialization of needed variables for interactive ViewModel usage
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var switchBtnNotification: SwitchCompat
    private lateinit var switchBtnHelpDrink: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)

        notificationViewModel.notificationPreference = activity?.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")
        notificationViewModel.loadData()
        Log.d(TAG, "loadData() called")

        // Give switchButtons their ID
        switchBtnNotification = view.findViewById(R.id.switch_allow_notification)
        switchBtnHelpDrink = view.findViewById(R.id.switch_allow_help_drink)

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
    }
}