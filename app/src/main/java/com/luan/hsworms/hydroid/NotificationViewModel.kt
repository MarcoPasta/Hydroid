package com.luan.hsworms.hydroid

import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NotificationViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var switchBoolNotification: Boolean = false
    var switchBoolHelpDrink: Boolean = true

    // TODO: Implement function model for return switch status
    fun canNotifyHelpDrink():Boolean {
        return if(!switchBoolNotification) false else switchBoolHelpDrink
    }
}