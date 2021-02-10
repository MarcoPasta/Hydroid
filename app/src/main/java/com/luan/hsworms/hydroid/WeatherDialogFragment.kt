package com.luan.hsworms.hydroid

import android.content.Context
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
    private lateinit var weatherDialogViewModel: WeatherDialogViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_weather_dialog, container, false)

        //Initilizing the ViewModels
        mainViewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        weatherDialogViewModel= WeatherDialogViewModel()


        //Initializing an object with user data with data from a file
        weatherDialogViewModel.weatherData = activity?.getSharedPreferences(
            getString(R.string.preferences_file_weather),
            Context.MODE_PRIVATE
        )

        //Populate variables (last weather conditions) from internal storage (SharedPreferences) and
        // deleate old file, if exists, at the first start
        weatherDialogViewModel.populateVariables()

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        if (weatherDialogViewModel.date != mainViewModel.currentDate()){//It is the first change of weather today
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
            weatherDialogViewModel.date = mainViewModel.currentDate()
            weatherDialogViewModel.waterAddBecauseOfWeather = addWater
            weatherDialogViewModel.saveData(mainViewModel.currentDate(), weatherDialogViewModel.waterAddBecauseOfWeather)
        } else //It is not the first change of weather today
        {
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(-1 * weatherDialogViewModel.waterAddBecauseOfWeather)
            mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
            weatherDialogViewModel.waterAddBecauseOfWeather = addWater
            weatherDialogViewModel.saveData(mainViewModel.currentDate(), weatherDialogViewModel.waterAddBecauseOfWeather)
        }

        dismiss()
    }
}

