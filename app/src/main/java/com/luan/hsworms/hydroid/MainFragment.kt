package com.luan.hsworms.hydroid

import android.app.AlertDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import com.luan.hsworms.hydroid.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Moved the initialization of view from onActivityCreated to onCreateView, since
        // onCreateView occurs earlier and in it you can already work with variables from the view
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Initializing an object with user data with data from a file
        viewModel.ourUserData = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<MainFragmentBinding>(inflater,
            R.layout.main_fragment, container, false)
        
        binding.changeWeather.setOnClickListener{
            val newFragment = WetterDialogFragment()
            newFragment.show(parentFragmentManager,"wetter")
        }

        binding.addSport.setOnClickListener{
            val newFragment = SportDialogFragment()
            newFragment.show(parentFragmentManager, "sport")
        }

        //The user input dialog is launched at the start of the application,
        // only if the default value of the weight ("0") have not changed
        //todo: Change the if-condition to ==
        //if(viewModel.weightOfUser.value != 0)
        showUserInputDialog()

        Log.i("on create view", viewModel.ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 0).toString())
        return  binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i("onActivityCreated", "onActivityCreated")
        super.onActivityCreated(savedInstanceState)
        //Calling the function of initializing variables with values from internal storage
        viewModel.clearFile()
        viewModel.populateViewModel()

        //TODO: Add implementation of observer methods
        viewModel.weightOfUser.observe(viewLifecycleOwner, { newWeight ->
            binding.textView.text = newWeight.toString()
        })

        viewModel.dailyLiquidRequirement.observe(viewLifecycleOwner, { newLiquedRequirement ->

        })
    }


    //dialogFragment for entering user data
    fun showUserInputDialog() {

        //Set Dialog variable and inflate ViewModel
        val dialogView = layoutInflater.inflate(R.layout.user_data_dialog_fragment, null)
        val dialogBuilder = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setTitle(getString(R.string.user_input_dialog_titel))
        val dialog = dialogBuilder.show()


        //Cancel button -> leave the dialog
        val cancelButton = dialogView.findViewById<Button>(R.id.imageButtonUserCancel)
        cancelButton.setOnClickListener{
            dialog.dismiss()
        }


        //Ok Button -> saving the entered parameters in the internal storage
        val okButton = dialogView.findViewById<Button>(R.id.imageButtonUserOk)
        okButton.setOnClickListener{
            val genderSelected = dialogView.findViewById<RadioGroup>(R.id.radioGroupGender).checkedRadioButtonId
            //If gender == woman =>true else false
            viewModel.userGenderIsFemale.value = genderSelected.equals(R.id.radioButtonWoman)

            //Checking in case of not entered value for weight
            if (dialogView.findViewById<TextView>(R.id.editTextUserWeight).text.toString() != ""){
                viewModel.weightOfUser.value = dialogView.findViewById<TextView>(R.id.editTextUserWeight).text.toString().toInt()
                dialog.dismiss()
                viewModel.saveData(viewModel.userGenderIsFemale.value!!, viewModel.weightOfUser.value!!,
                    viewModel.dailyLiquidRequirement.value!!, viewModel.currentlyDrunkLiquid.value!!)
                Toast.makeText(activity, getString(R.string.toast_daten_gesichert), Toast.LENGTH_SHORT).show()
            }else{
                //Toast with a suggestion to enter weight
                Toast.makeText(activity, getString(R.string.toast_gewicht_eingeben), Toast.LENGTH_SHORT).show()
            }

            //Logs for checking the correctness of processing the entered values
            Log.i("inputDialog", viewModel.weightOfUser.value.toString())
            Log.i("inputDialog", viewModel.ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 0).toString())
            Log.i("inputDialog", viewModel.userGenderIsFemale.value.toString())
        }
    }
}


//val newFragment = UserDataDialogFragment()
//newFragment.show(childFragmentManager, "userdata")