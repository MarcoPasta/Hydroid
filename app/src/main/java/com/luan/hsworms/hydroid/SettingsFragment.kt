package com.luan.hsworms.hydroid

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.luan.hsworms.hydroid.databinding.FragmentSettingsBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = getActivity();
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        settingsViewModel = ViewModelProvider(requireActivity(),SettingsViewModelFactory(requireActivity().application)
        ).get(SettingsViewModel::class.java)

        settingsViewModel.glasses = activity?.getSharedPreferences(
            getString(R.string.preferences_glasses),
            Context.MODE_PRIVATE
        )

        //Filling temporary variables with values from internal storage
        settingsViewModel.populateViewModel()
        println("TEST")
        println("TEST              ${settingsViewModel.glassBig.value}")

        initWidgets()
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
        saveSettings()
    }


    private fun initWidgets(){
        rgGender = rootView.findViewById(R.id.rg_gender)
        etWeight = rootView.findViewById(R.id.et_weight)
        etGlassSmall = rootView.findViewById(R.id.et_glass_small)
        etGlassMiddle = rootView.findViewById(R.id.et_glass_middle)
        etGlassBig = rootView.findViewById(R.id.et_glass_big)
        etGlassHuge = rootView.findViewById(R.id.et_glass_huge)
    }

    private fun fillingOfTheFragmentFields(){
        if(viewModel.userGenderIsFemale.value == 1){
            rgGender.check(R.id.rb_female)
        } else {
            rgGender.check(R.id.rb_male)
        }

        etWeight.editText?.setText(viewModel.weightOfUser.value.toString())
        etGlassSmall.editText?.setText(settingsViewModel.glassSmall.value.toString())
        etGlassMiddle.editText?.setText(settingsViewModel.glassMiddle.value.toString())
        etGlassBig.editText?.setText(settingsViewModel.glassBig.value.toString())
        etGlassHuge.editText?.setText(settingsViewModel.glassHuge.value.toString())
    }

    private fun saveSettings(){
        //Gender settings
        //If gender == woman =>1 else 0
        val genderSelected = view?.findViewById<RadioGroup>(R.id.rg_gender)?.checkedRadioButtonId
        if(genderSelected == R.id.rb_female){
            viewModel.userGenderIsFemale.value = 1
        }else{
            viewModel.userGenderIsFemale.value = 0
        }
        val gender = viewModel.userGenderIsFemale.value!!
        viewModel.saveGender(gender)

        //Weight settings
        val weight = view?.findViewById<TextInputLayout>(R.id.et_weight)?.editText?.text.toString().toInt()
        viewModel.weightOfUser.value = weight
        viewModel.saveWeight(weight)

        //Glass volumes
        settingsViewModel.saveData(etGlassSmall.editText?.text.toString().toInt(),
            etGlassMiddle.editText?.text.toString().toInt(),
            etGlassBig.editText?.text.toString().toInt(),
            etGlassHuge.editText?.text.toString().toInt())
    }
}

//        Log.i(sharedPreferences.getInt(R.string.saved_weight_of_user.toString(), 0).toString(), "Schared preference")
//        Log.i(viewModel.userGenderIsFemale.value.toString(), "User gender")