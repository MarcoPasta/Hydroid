package com.luan.hsworms.hydroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

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

        initButtons()
    }

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

    private fun changeWaterRequirementBecauseOfSport(addWater: Int){
        mainViewModel.addWaterRequirementBecauseOfSportOrWeather(addWater)
        dismiss()
    }
}