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

    var weatherData: SharedPreferences? = null//To save user data in internal storage
    var date:String
    var waterAddBecauseOfWeather: Int

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

        populateVariables()

        println("TEST     ${date}")

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        initButtons()
    }

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

    private fun changeWaterRequirementBecauseOfWeather(addWater: Int){
        if (date != mainViewModel.currentDate()){
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
            date = mainViewModel.currentDate()
            waterAddBecauseOfWeather = addWater
            saveData(mainViewModel.currentDate(), waterAddBecauseOfWeather!!)
        } else
        {
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(-1 * waterAddBecauseOfWeather!!)
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
            waterAddBecauseOfWeather = addWater
            saveData(mainViewModel.currentDate(), waterAddBecauseOfWeather!!)
        }

        dismiss()
    }

    //Filling Variables with values from internal storage
    fun populateVariables()
    {
//        println("TEST5 ${weatherData?.getString(R.string.saved_gender_of_user.toString(), "01.01.1970").toString()}" +
//                "  ${weatherData?.getInt(R.string.saved_weight_of_user.toString(), 0).toString()}")

        //for the first start make klean up
        if(weatherData?.getString(R.string.saved_date.toString(), null) == null) {
            clearFile()
        }

        date = weatherData!!.getString(R.string.saved_date.toString(), "01.01.1970")!!
        waterAddBecauseOfWeather = weatherData?.getInt(R.string.saved_water_add_because_of_weather.toString(), 0)!!
    }

    //Here we change the data in the storage, in case of changing the actual data
    fun saveData(newDate: String, waterAdd: Int){
        val editor = weatherData?.edit()

        editor?.putString(R.string.saved_date.toString(), newDate)
        editor?.putInt(R.string.saved_water_add_because_of_weather.toString(), waterAdd)

        editor?.apply()
    }

    //Clear Data for testing
    fun clearFile(){
        val editor = weatherData?.edit()
        editor?.clear()
        editor?.apply()

    }
}

