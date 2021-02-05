package com.luan.hsworms.hydroid

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import kotlin.math.roundToInt

/**
 * Class for handling user actions in the "User settings" section of the menu. The user gets a clickable link to the WaterRequirementTable, the ability to change user weight and gender, as well as the standard volumes of liquid that appear in the AddWaterDialog.
 */
class SettingsFragment : Fragment() {
    private lateinit var rootView: View

    private lateinit var viewModel: MainViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var rgGender: RadioGroup
    private lateinit var etWeight: TextInputLayout
    private lateinit var etGlassSmall: TextInputLayout
    private lateinit var etGlassMiddle: TextInputLayout
    private lateinit var etGlassBig: TextInputLayout
    private lateinit var etGlassHuge: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Layout inflate
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        //Initializing of MainViewModel
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        //Initializing of SettingsViewModel
        settingsViewModel = ViewModelProvider(requireActivity(),SettingsViewModelFactory(requireActivity().application)
        ).get(SettingsViewModel::class.java)


        //Initializing of SharedPreferences
        settingsViewModel.glasses = activity?.getSharedPreferences(
            getString(R.string.preferences_glasses),
            Context.MODE_PRIVATE
        )

        // Filling temporary variables with values from internal storage (SharedPreferences)
        settingsViewModel.populateViewModel()

        //Initializing of widgets
        initWidgets()
        //Populating Menu Fields with Saved Values
        fillingOfTheFragmentFields()

        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            view.findViewById<TextView>(R.id.text_view_water_requirement_table)?.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_settingsFragment_to_waterRequirementTableFragment, null)
        )
    }


    override fun onStop() {
        super.onStop()
        //Saving data when the user leaves this menu section
        saveSettings()
    }

    /**
     * Initialising of widgets
     */
    private fun initWidgets(){
        rgGender = rootView.findViewById(R.id.rg_gender)
        etWeight = rootView.findViewById(R.id.et_weight)
        etGlassSmall = rootView.findViewById(R.id.et_glass_small)
        etGlassMiddle = rootView.findViewById(R.id.et_glass_middle)
        etGlassBig = rootView.findViewById(R.id.et_glass_big)
        etGlassHuge = rootView.findViewById(R.id.et_glass_huge)
    }

    /**
     * Filling of the fragment fields
     */
    private fun fillingOfTheFragmentFields(){

        //Filling of Gender-field
        if(viewModel.userGenderIsFemale.value == 1){
            rgGender.check(R.id.rb_female)
        } else {
            rgGender.check(R.id.rb_male)
        }

        //Filling of Weight fields
        etWeight.editText?.setText(viewModel.weightOfUser.value.toString())

        //Filling of Water-portions
        etGlassSmall.editText?.setText(settingsViewModel.glassSmall.value.toString())
        etGlassMiddle.editText?.setText(settingsViewModel.glassMiddle.value.toString())
        etGlassBig.editText?.setText(settingsViewModel.glassBig.value.toString())
        etGlassHuge.editText?.setText(settingsViewModel.glassHuge.value.toString())
    }

    /**
     * Saving data specified in the "User Settings" menu section in variables and in local storage (SharedPreferences)
     */
    private fun saveSettings(){
        //Gender settings
        val genderSelected = view?.findViewById<RadioGroup>(R.id.rg_gender)?.checkedRadioButtonId
        if(genderSelected == R.id.rb_female){         //If gender == woman =>1 else 0
            viewModel.userGenderIsFemale.value = 1
        }else{
            viewModel.userGenderIsFemale.value = 0
        }
        val gender = viewModel.userGenderIsFemale.value!!
        viewModel.saveGender(gender)

        //Weight settings
        val weight = view?.findViewById<TextInputLayout>(R.id.et_weight)?.editText?.text.toString().toFloat().roundToInt()
        viewModel.weightOfUser.value = weight
        viewModel.saveWeight(weight)

        //Glass volumes
        settingsViewModel.saveData(etGlassSmall.editText?.text.toString().toFloat().roundToInt(),
            etGlassMiddle.editText?.text.toString().toFloat().roundToInt(),
            etGlassBig.editText?.text.toString().toFloat().roundToInt(),
            etGlassHuge.editText?.text.toString().toFloat().roundToInt())
    }
}