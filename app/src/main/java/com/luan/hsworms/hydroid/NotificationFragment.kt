package com.luan.hsworms.hydroid

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel

class NotificationFragment : Fragment() {
    companion object {
        fun newInstance() = NotificationFragment()
    }

    private lateinit var notificationViewModel: NotificationViewModel

    private lateinit var switchBtnNotification: Switch
    private lateinit var switchBtnHelpDrink: Switch

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

        // TODO: Use the ViewModel
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

        switchBtnNotification.setOnCheckedChangeListener() { _ , isChecked ->
            notificationViewModel.switchBoolNotification = isChecked
            // Wenn Notification eingeschaltet wird kann HelpDrink benutzt werden
            switchBtnHelpDrink.isEnabled = isChecked
        }

        switchBtnHelpDrink.setOnCheckedChangeListener() { _ , isChecked ->
            // Checkt ob HelpDrink gedrückt wird
            notificationViewModel.switchBoolHelpDrink = isChecked
        }

    }

}