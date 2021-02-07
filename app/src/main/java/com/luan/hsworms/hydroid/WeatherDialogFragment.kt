package com.luan.hsworms.hydroid

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

/**
 * This class handles events associated with the WeatherDilogFragment dialog box. In this dialog, the user can select one of four preset weather conditions that affect the need for water. In the class, these changes are processed and the results are saved.
 *
 * @author Andrej Alpatov
 */
class WeatherDialogFragment : DialogFragment() {

    private lateinit var rootView: View

    //Views
    private lateinit var btnHeat: Button
    private lateinit var btnCold: Button
    private lateinit var btnNiceWeather: Button
    private lateinit var btnWarmWeather: Button
    private lateinit var btnCancel: Button

    //ViewModel
    private lateinit var mainViewModel: MainViewModel

    private var weatherData: SharedPreferences? = null//To save the last weather conditions
    var date:String
    private var waterAddBecauseOfWeather: Int

    init {
        date = ""
        waterAddBecauseOfWeather = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_weather_dialog, container, false)

        //Initializing an object with user data with data from a file
        weatherData = activity?.getSharedPreferences(
            getString(R.string.preferences_file_weather),
            Context.MODE_PRIVATE
        )

        //Populate variables (last weather conditions) from internal storage (SharedPreferences) and
        // deleate old file, if exists, at the first start
        populateVariables()

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        //Initializing All Dialog Box Buttons. Implementing and setting a click handler
        initButtons()
    }


    /**
     * Initializing All Dialog Box Buttons (Cancel button and four weather selection buttons). Implementing and setting a click handler
     */
    private fun initButtons(){
        btnCancel = rootView.findViewById(R.id.btn_cancel)
        btnHeat = rootView.findViewById(R.id.btn_hitze)
        btnCold = rootView.findViewById(R.id.btn_kaelte)
        btnNiceWeather = rootView.findViewById(R.id.btn_angenemes_wetter)
        btnWarmWeather = rootView.findViewById(R.id.btn_warmes_wettter)

        btnCancel.setOnClickListener { dismiss() }
        btnWarmWeather.setOnClickListener { changeWaterRequirementBecauseOfWeather(100) }
        btnNiceWeather.setOnClickListener { changeWaterRequirementBecauseOfWeather(0) }
        btnCold.setOnClickListener { changeWaterRequirementBecauseOfWeather(200) }
        btnHeat.setOnClickListener { changeWaterRequirementBecauseOfWeather(300) }
    }


    /**
     * Adds to the daily water requirement, an additional amount passed as a parameter. Closes the dialog.
     *
     * @param addWater The amount of water to be added to the standard daily requirement due to weather conditions (type: Int)
     */
    private fun changeWaterRequirementBecauseOfWeather(addWater: Int){
        if (date != mainViewModel.currentDate()){//It is the first change of weather today
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
            date = mainViewModel.currentDate()
            waterAddBecauseOfWeather = addWater
            saveData(mainViewModel.currentDate(), waterAddBecauseOfWeather)
        } else //It is not the first change of weather today
        {
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(-1 * waterAddBecauseOfWeather)
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
            waterAddBecauseOfWeather = addWater
            saveData(mainViewModel.currentDate(), waterAddBecauseOfWeather)
        }

        dismiss()
    }


    //Filling Variables with values from internal storage
    /**
     * Cleaning the local storage (the last weather conditions set for the current day are stored, namely the additional need for water) in the case of the first launch of the application. Writing new data in variables
     */
    private fun populateVariables()
    {
        //for the first start сleaning storage
        if(weatherData?.getString(R.string.saved_date.toString(), null) == null) {
            clearFile()
        }

        date = weatherData!!.getString(R.string.saved_date.toString(), "01.01.1970")!!
        waterAddBecauseOfWeather = weatherData?.getInt(R.string.saved_water_add_because_of_weather.toString(), 0)!!
    }


    /**
     *  Here we change the data (current date and additional amount of water) in the storage, in case of changing the actual data.
     *
     *  @param newDate      date, to be saved (type: String)
     *  @param waterAdd     additional amount of water (type: Int)
     */
    private fun saveData(newDate: String, waterAdd: Int){
        val editor = weatherData?.edit()

        editor?.putString(R.string.saved_date.toString(), newDate)
        editor?.putInt(R.string.saved_water_add_because_of_weather.toString(), waterAdd)

        editor?.apply()
    }


    //Clear Data for debugging
    /**
     * Cleaning up local storage (weather data)
     */
    private fun clearFile(){
        val editor = weatherData?.edit()
        editor?.clear()
        editor?.apply()
    }
}

