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

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: MainViewModel

    private lateinit var rgGender: RadioGroup
    private lateinit var etWeight: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = getActivity();
    }

    // lateinit var  view: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_settings, container, false)

        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        initWidgets()

//        Log.i(sharedPreferences.getInt(R.string.saved_weight_of_user.toString(), 0).toString(), "Schared preference")
//        Log.i(viewModel.userGenderIsFemale.value.toString(), "User gender")
        if(viewModel.userGenderIsFemale.value == true){
            rgGender.check(R.id.rb_female)
        } else {
            rgGender.check(R.id.rb_male)
        }

        etWeight.editText?.setText(viewModel.weightOfUser.value.toString())

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
        //Save Settings
        //If gender == woman =>true else false
        val genderSelected = view?.findViewById<RadioGroup>(R.id.rg_gender)?.checkedRadioButtonId
        viewModel.userGenderIsFemale.value = (genderSelected == R.id.rb_female)
        val gender = viewModel.userGenderIsFemale.value!!
        viewModel.saveGender(gender)

        val weight = view?.findViewById<TextInputLayout>(R.id.et_weight)?.editText?.text.toString().toInt()
        viewModel.weightOfUser.value = weight
        viewModel.saveWeight(weight)
    }

    private fun initWidgets(){
        rgGender = rootView.findViewById(R.id.rg_gender)
        etWeight = rootView.findViewById(R.id.et_weight)
    }
}