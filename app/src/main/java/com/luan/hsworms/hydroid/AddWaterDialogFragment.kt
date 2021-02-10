package com.luan.hsworms.hydroid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

/**
 * Handling user actions when invoking a AddWaterDialog. In this dialog, the user has the opportunity to choose from four different volumes of liquid to be added to the already drunk.
 *
 */
class AddWaterDialogFragment: DialogFragment() {

    private lateinit var rootView: View

    //Views
    private lateinit var btnSave: Button
    private lateinit var btnAbort: Button
    private lateinit var imgSmall: ImageView
    private lateinit var imgMiddle: ImageView
    private lateinit var imgBig: ImageView
    private lateinit var imgHuge: ImageView
    private lateinit var tvQuantity:TextView
    private lateinit var tvQuantitySmall:TextView
    private lateinit var tvQuantityMiddle:TextView
    private lateinit var tvQuantityBig:TextView
    private lateinit var tvQuantityHuge:TextView

    //ViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var notificationViewModel: NotificationViewModel  // Create a NotificationViewModel object to get access to Notification data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)

        // Instantiate a NotificationViewModel object to get access to notification switch button states
        // and load them
        notificationViewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)
        notificationViewModel.notificationPreference = activity?.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
        notificationViewModel.loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        //inflate the layout
        rootView = inflater.inflate(R.layout.add_water_dialog, container, false)

        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Log.d(TAG, "onViewCreated() called")

        //Initializing of MainViewModel
        mainViewModel = ViewModelProvider(requireActivity(),
        MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        //Initializing of SettingsViewModel
        settingsViewModel = ViewModelProvider(requireActivity(),
            SettingsViewModelFactory(requireActivity().application)).get(SettingsViewModel::class.java)

        //Initializing an object with user data with data from a file
        settingsViewModel.glasses = activity?.getSharedPreferences(
            getString(R.string.preferences_glasses),
            Context.MODE_PRIVATE
        )

        //Filling temporary variables with values from internal storage
        settingsViewModel.populateViewModel()

        //Initializing of Buttons, TextViews and ImageButtons(also use as Buttons)
        initButtons()
        initTextView()
        initImageViews()

        //Initializing textView with values
        initTextViewsWithValues()

        btnSave.setOnClickListener{
            //Storing data in variables and local storage (SharedPreferences)
            saveData(tvQuantity.text.toString().toInt())
        }
        //Log.d(TAG, "End of onViewCreated")
    }

    /**
     * Initializing of ok and Cancel buttons and implementation of clickLisener for the cancel button
     */
    private fun initButtons(){
        btnSave = rootView.findViewById(R.id.btn_add_water_ok)
        btnAbort = rootView.findViewById(R.id.btn_add_water_cancel)

        btnAbort.setOnClickListener{ dismiss() }
    }

    /**
     * Initializing of imageViews and implementation of clicksListener for them
     */
    private fun initImageViews(){
        //Initialization of imageViews
        imgSmall = rootView.findViewById(R.id.iv_glass_small)
        imgMiddle = rootView.findViewById(R.id.iv_glass_middle)
        imgBig = rootView.findViewById(R.id.iv_glass_big)
        imgHuge = rootView.findViewById(R.id.iv_glass_huge)

        //implementation of clicksListener for imageViews
        imgSmall.setOnClickListener{ tvQuantity.text = tvQuantitySmall.text }
        imgMiddle.setOnClickListener{ tvQuantity.text = tvQuantityMiddle.text }
        imgBig.setOnClickListener{ tvQuantity.text = tvQuantityBig.text }
        imgHuge.setOnClickListener{ tvQuantity.text = tvQuantityHuge.text }
    }

    /**
     * Initializing of TextViews and set value on tvQuantity
     */
    private fun initTextView(){
        tvQuantitySmall = rootView.findViewById(R.id.tv_volume_small)
        tvQuantityMiddle = rootView.findViewById(R.id.tv_volume_middle)
        tvQuantityBig = rootView.findViewById(R.id.tv_volume_big)
        tvQuantityHuge = rootView.findViewById(R.id.tv_volume_huge)
        tvQuantity = rootView.findViewById(R.id.tv_quantity)
        tvQuantity.text = "0"
    }

    /**
     * Adding the user-selected amount of water to the amount already drunk and saving the data in variables, local storage (shaoedPreferences) and in the database. Closing the dialog.
     *
     * @param waterIn The amount of added (drunk) water (type: Int)
     */
    private fun saveData(waterIn:Int){
        mainViewModel.addDrunkWater(waterIn)
        dismiss()
    }

    /**
     * Initializing of TextViews with values
     */
    private fun initTextViewsWithValues(){
        tvQuantitySmall.text = settingsViewModel.glassSmall.value.toString()
        tvQuantityMiddle.text = settingsViewModel.glassMiddle.value.toString()
        tvQuantityBig.text = settingsViewModel.glassBig.value.toString()
        tvQuantityHuge.text = settingsViewModel.glassHuge.value.toString()
    }
}