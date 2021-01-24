package com.luan.hsworms.hydroid

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


private const val NOTIFY_TAG = "NF_Notify"
private const val HELP_DRINK_TAG = "NF_HelpDrink"

class NotificationFragment : Fragment() {
    companion object {
        fun newInstance() = NotificationFragment()
    }

    private lateinit var notificationViewModel: NotificationViewModel

    private lateinit var switchBtnNotification: SwitchCompat
    private lateinit var switchBtnHelpDrink: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel for data purpose only
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // id von SwitchButton zuweisen
        switchBtnNotification = view.findViewById(R.id.switch_allow_notification)
        switchBtnHelpDrink = view.findViewById(R.id.switch_allow_help_drink)

        // Wenn Button gedrückt wird, übergebe den aktuellen Status
        switchBtnNotification.isChecked = notificationViewModel.switchBoolNotification
        switchBtnHelpDrink.isChecked = notificationViewModel.switchBoolHelpDrink
        // Wenn Notification true ist kann man HelpDrink benutzen!
        switchBtnHelpDrink.isEnabled = notificationViewModel.switchBoolNotification

        // OnClickListener for Notification Button
        switchBtnNotification.setOnCheckedChangeListener() { _ , isChecked ->
            notificationViewModel.switchBoolNotification = isChecked
            // Wenn Notification eingeschaltet wird kann HelpDrink benutzt werden
            switchBtnHelpDrink.isEnabled = isChecked

            Log.d(NOTIFY_TAG, notificationViewModel.canSendNotification().toString())

        }

        // OnClickListener for HelpDrink Button
        switchBtnHelpDrink.setOnCheckedChangeListener() { _ , isChecked ->
            // Checkt ob HelpDrink gedrückt wird
            notificationViewModel.switchBoolHelpDrink = isChecked

            Log.d(HELP_DRINK_TAG, notificationViewModel.switchBoolHelpDrink.toString())
        }

    }

}