package com.luan.hsworms.hydroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

/**
 * This class handles events associated with the SportDilogFragment dialog box. With this dialog, the user can select one of three possible physical activities and thereby increase the daily water requirement. Additional water demand is preset and cannot be changed by the user.
 *
 * @author Andrej Alpatov
 */
class SportDialogFragment : DialogFragment() {
    private lateinit var rootView: View

    //Views
    private lateinit var btnFitness: Button
    private lateinit var btnRun: Button
    private lateinit var btnWalk: Button
    private lateinit var btnCancel: Button

    //ViewModel
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        rootView = inflater.inflate(R.layout.fragment_sport_dialog, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        //Initializing buttons and click handlers
        initButtons()
    }

    /**
     * Initializing buttons and setting click handlers for the three sports activity select buttons and the cancel button.
     */
    private fun initButtons(){
        btnCancel = rootView.findViewById(R.id.btn_cancel)
        btnFitness = rootView.findViewById(R.id.btn_fitness)
        btnRun = rootView.findViewById(R.id.btn_joggen)
        btnWalk = rootView.findViewById(R.id.btn_spatziergang)

        btnCancel.setOnClickListener { dismiss() }
        btnWalk.setOnClickListener { changeWaterRequirementBecauseOfSport(100) }
        btnRun.setOnClickListener { changeWaterRequirementBecauseOfSport(500) }
        btnFitness.setOnClickListener { changeWaterRequirementBecauseOfSport(300) }
    }

    /**
     * Increase in daily water demand by adding the value received as an input parameter and saving the new value to live date variables, local storage(SharedPreferences) and database.
     *
     * @param addWater The amount of water to add to the current daily requirement (type: Int)
     */
    private fun changeWaterRequirementBecauseOfSport(addWater: Int){
        mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
        dismiss()
    }
}