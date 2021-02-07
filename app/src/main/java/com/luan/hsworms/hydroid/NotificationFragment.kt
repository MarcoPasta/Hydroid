package com.luan.hsworms.hydroid

import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.luan.hsworms.hydroid.backend.notifications.AlarmService
import java.text.SimpleDateFormat
import java.util.*

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
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private lateinit var tvSetStartTime: TextView
    private lateinit var tvSetEndTime: TextView

    // ActionButton
    private lateinit var actionButton: FloatingActionButton
    private lateinit var startTimeButton: FloatingActionButton
    private lateinit var endTimeButton: FloatingActionButton

    // Animations
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_anim) }

    // Click checker
    private var clicked: Boolean = false


    // Time variables that will be used to show the current time
    private var nowHour: Int = 0
    private var nowMinute: Int = 0


    // It works without creating an object, but i let it stay like this for now...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)
        notificationViewModel.notificationPreference = activity?.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
    }


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.notification_fragment, container, false)
    }

    // @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")

        // Loading Data from Shared Preferences
        notificationViewModel.loadData()
        Log.d(TAG, "Funktioniert nur bei neuinstallation der App")
        Log.d(TAG, "Hour: " + notificationViewModel.startHour + ", minute: " + notificationViewModel.startMinute)


        // Initializing SwitchButtons
        switchBtnNotification = view.findViewById(R.id.switch_allow_notification)
        switchBtnHelpDrink = view.findViewById(R.id.switch_allow_help_drink)

        // Initialize ActionButton
        actionButton = view.findViewById(R.id.addTimePicker)
        startTimeButton = view.findViewById(R.id.addStartTime)
        endTimeButton = view.findViewById(R.id.addEndTime)

        // Initialize TextView
        tvStartTime = view.findViewById(R.id.setStartTime)
        tvEndTime = view.findViewById(R.id.setEndTime)
        tvSetStartTime = view.findViewById(R.id.addStartTimeText)
        tvSetEndTime = view.findViewById(R.id.addEndTimeText)

        // Debug
        Log.d(TAG, "tvTime wird initialisiert")
        Log.d(TAG, "Eingestellt Uhrzeit: " + tvStartTime.text.toString())

        // Update the current TextView with Data that are in textView.
        // CAUTION! After first installation of the app a bug appears in formatting. Check if hour and minute are not hundred.
        Log.d(TAG, "Check if hour and minute is not 100...")
        //updating the notification start time
        if((notificationViewModel.startHour != 100) && (notificationViewModel.startMinute != 100)) {
            updateStartText()
            Log.d(TAG, "updateStartText() called")
            Log.d(TAG, "tvStartTime nach update: " + tvStartTime.text)
        } else {
            Log.d(TAG, "startHour und startMinute == 100, lade Vorlage..")
        }

        // Updating the notification end time
        if((notificationViewModel.endHour != 100) && (notificationViewModel.endMinute != 100)) {
            updateEndText()
            Log.d(TAG, "updateEndText() called")
            Log.d(TAG, "tvEndTime nach update: " + tvStartTime.text)
        } else {
            Log.d(TAG, "endHour und endMinute == 100, lade Vorlage..")
        }

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
                Log.d(TAG, "AlarmService::setHelpDrinkAlarm() called")
                AlarmService.setHelpDrinkAlarm(
                    requireContext(),
                    notificationViewModel.canSendHelpDrinkNotification(),
                    notificationViewModel.startHour,
                    notificationViewModel.startMinute
                )
            } else {
                Toast.makeText(
                    context,
                    "Benachrichtigungen sind nun Deaktiviert.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "Notificaton switch inactive")
                Log.d(TAG, "AlarmService::setHelpDrinkAlarm() called")
                AlarmService.setHelpDrinkAlarm(
                    requireContext(),
                    notificationViewModel.canSendHelpDrinkNotification(),
                    notificationViewModel.startHour,
                    notificationViewModel.startMinute
                )
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
                Log.d(TAG, "AlarmService::setHelpDrinkAlarm() called")
                AlarmService.setHelpDrinkAlarm(
                    requireContext(),
                    notificationViewModel.canSendHelpDrinkNotification(),
                    notificationViewModel.startHour,
                    notificationViewModel.startMinute
                )
            }
            else {
                Toast.makeText(context, "\"Hilf mir trinken!\" ist nun Deaktiviert.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "HelpDrink switch inactive")
                Log.d(TAG, "AlarmService::setHelpDrinkAlarm() called")

                AlarmService.setHelpDrinkAlarm(
                    requireContext(),
                    notificationViewModel.canSendHelpDrinkNotification(),
                    notificationViewModel.startHour,
                    notificationViewModel.startMinute
                )
            }
        }


        // OnClickListener for actionButton that will display the other buttons
        actionButton.setOnClickListener() {
            Log.d(TAG, "ActionButton pressed")
            onAddButtonClicked()
        }

        // OnClickListener for startTimeButton
        startTimeButton.setOnClickListener() {
            Log.d(TAG, "startTimeButton pressed")

            // get the  actual time into nowHour and nowMinute
            getTime()
            Log.d(TAG, "Current time for displayed on TimePickerDialog is beeing fetched")

            // This Listener is used to get the returned TimePickerValues and save them inside the SP
            val timeSetStartListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                Log.d(TAG, "saveHour: $hourOfDay, saveMinute: $minute")
                saveStartTime(hourOfDay, minute)
                updateStartText()
                Toast.makeText(
                    requireContext(),
                    "Benachrichtigungen fangen um " + formatDate(hourOfDay, minute) + " an",
                    Toast.LENGTH_SHORT
                ).show()
                //activity?.recreate()
            }
            // Opens the TimePickerDialog
            TimePickerDialog(
                requireContext(),
                timeSetStartListener,
                nowHour,
                nowMinute,
                true
            ).show()
            Log.d(TAG, "TimePickerDialog has been opened")
            Log.d(TAG, "AlarmService::setHelpDrinkAlarm() called")
            AlarmService.setHelpDrinkAlarm(
                requireContext(),
                notificationViewModel.canSendHelpDrinkNotification(),
                notificationViewModel.startHour,
                notificationViewModel.startMinute
            )
        }

        endTimeButton.setOnClickListener() {
            Log.d(TAG, "endTimeButton pressed")
            getTime()
            // This Listener is used to get the returned TimePickerValues and save them inside the SP
            val timeSetEndListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                // Everything happening inside here got moved from onTimeSet()
                Log.d(TAG, "saveHour: $hourOfDay, saveMinute: $minute")
                saveEndTime(hourOfDay, minute)
                updateEndText()
                Toast.makeText(
                    requireContext(),
                    "Benachrichtigungen enden um " + formatDate(hourOfDay, minute),
                    Toast.LENGTH_SHORT
                ).show()
                // activity?.recreate()
            }
            // Opens the TimePickerDialog
            TimePickerDialog(
                requireContext(),
                timeSetEndListener,
                nowHour,
                nowMinute,
                true
            ).show()
            Log.d(TAG, "TimePickerDialog has been opened")
            Log.d(TAG, "AlarmService::setHelpDrinkAlarm() called")
            AlarmService.setHelpDrinkAlarm(
                requireContext(),
                notificationViewModel.canSendHelpDrinkNotification(),
                notificationViewModel.startHour,
                notificationViewModel.startMinute
            )
        }
    }

    // this function will call the functions needed to make the invisible buttons work
    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    // sets the visibility for the floating ActionButtons
    private fun setVisibility(clicked: Boolean) {
        if(!clicked) {
            startTimeButton.visibility = View.VISIBLE
            endTimeButton.visibility = View.VISIBLE
            tvSetStartTime.visibility = View.VISIBLE
            tvSetEndTime.visibility = View.VISIBLE
        } else {
            startTimeButton.visibility = View.INVISIBLE
            endTimeButton.visibility = View.INVISIBLE
            tvSetStartTime.visibility = View.INVISIBLE
            tvSetEndTime.visibility = View.INVISIBLE
        }
    }

    // sets the animation for the floating ActionButtons
    private fun setAnimation(clicked: Boolean) {
        if(!clicked) {
            startTimeButton.startAnimation(fromBottom)
            endTimeButton.startAnimation(fromBottom)
            tvSetStartTime.startAnimation(fromBottom)
            tvSetEndTime.startAnimation(fromBottom)
            actionButton.startAnimation(rotateOpen)
        } else {
            startTimeButton.startAnimation(toBottom)
            endTimeButton.startAnimation(toBottom)
            tvSetStartTime.startAnimation(toBottom)
            tvSetEndTime.startAnimation(toBottom)
            actionButton.startAnimation(rotateClose)
        }
    }

    // If the buttons are displayed, they will be clickable
    private fun setClickable(clicked: Boolean) {
        if(!clicked) {
            startTimeButton.isClickable = true
            endTimeButton.isClickable = true
        } else {
            startTimeButton.isClickable = false
            endTimeButton.isClickable = false
        }
    }

    // writes the current time inside nowHour and nowMinute. Displayed in TimePickerDialog
     private fun getTime() {
        Log.d(TAG, "getting the actual time...")
        nowHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        nowMinute = Calendar.getInstance().get(Calendar.MINUTE)
    }

    // needs to be here, everyone now works over OnSetTimeListener
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        Log.d(TAG, "Calling onTimeSet() Callback Function")
    }

    // Updates the onscreen time that when notifications shall start
    private fun updateStartText() {
        tvStartTime.text = formatDate(notificationViewModel.startHour, notificationViewModel.startMinute)
    }

    // Updates the onscreen time that when notifications shall end
    private fun updateEndText() {
        tvEndTime.text = formatDate(notificationViewModel.endHour, notificationViewModel.endMinute)
    }

    // Saves the time set with TimePickerDialog into SharedPreferences so it can be called and reload again
    private fun saveStartTime(toSaveStartHour: Int, toSaveStartMinute: Int) {
        Log.d(TAG, "called saveStartTime()")
        notificationViewModel.startHour = toSaveStartHour
        Log.d(TAG, "saving startHour, value: " + notificationViewModel.startHour)
        notificationViewModel.startMinute = toSaveStartMinute
        Log.d(TAG, "saving startMinute, value: " + notificationViewModel.startMinute)
        Log.d(TAG, "calling nVM.saveStartData()")
        notificationViewModel.saveData()
    }

    // Saves the time notifications shall end
    private fun saveEndTime(toSaveEndHour: Int, toSaveEndMinute: Int) {
        Log.d(TAG, "called saveEndTime()")
        notificationViewModel.endHour = toSaveEndHour
        Log.d(TAG, "saving endHour, value: " + notificationViewModel.endHour)
        notificationViewModel.endMinute = toSaveEndMinute
        Log.d(TAG, "saving endMinute, value: " + notificationViewModel.endMinute)
        Log.d(TAG, "calling nVM.saveEndData()")
        notificationViewModel.saveData()
    }

    // Function to format date before output, even if deprecated
    private fun formatDate(hour: Int, minute: Int): String {
        Log.d(TAG, "formatting date...")
        val simpleFormat = SimpleDateFormat("HH:mm")

        val timeNow: Date = Date()
        timeNow.hours = hour
        timeNow.minutes = minute

        return simpleFormat.format(timeNow)
    }
}