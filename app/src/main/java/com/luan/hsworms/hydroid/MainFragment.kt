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
    ): View
    {
        //Initializing of ViewModel
        viewModel = ViewModelProvider(requireActivity(),
        MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        //Initializing an object with user data with data from a file
        viewModel.firstStart = activity?.getSharedPreferences(
            getString(R.string.preferences_file_first_start),
            Context.MODE_PRIVATE
        )

        //Initializing an object with data for first start with data from a file
        viewModel.ourUserData = activity?.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )

        activity?.actionBar?.setTitle("Hydroid")

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<MainFragmentBinding>(
            inflater,
            R.layout.main_fragment, container, false
        )

        binding.changeWeather.setOnClickListener {
            val newFragment = WeatherDialogFragment()
            newFragment.show(parentFragmentManager, "wetter")
        }

        binding.addSport.setOnClickListener {
            val newFragment = SportDialogFragment()
            newFragment.show(parentFragmentManager, "sport")
        }

        binding.addWater.setOnClickListener{
            val newFragment = AddWaterDialogFragment()
            newFragment.show(parentFragmentManager, "add water")
        }


        /////for debugging///////////////////
        //viewModel.clearFile()
        //viewModel.saveFirstStart(1)
        /////////////////////////////////////

        //Populate ScharedPreferences to check if the start is first
        viewModel.populateFirstStart()

        //Calling the function of initializing variables with values from internal storage
        viewModel.populateViewModel()

        //Check if the Strat is first
        if(viewModel.isFirstStart == 1){//first start
            viewModel.clearFile()
            viewModel.saveFirstStart(0)//From now is not first start
            showUserInputDialog()
        }else{//not first start
            viewModel.updateDataByStartActivity(viewModel.weightOfUser.value!!.toLong(),
                viewModel.userGenderIsFemale.value!!)
        }

        Log.i("on create view", viewModel.ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 0).toString())
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i("onActivityCreated", "onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        viewModel.dailyLiquidRequirement.observe(viewLifecycleOwner, { newLiquedRequirement ->
            binding.tvDailyRequirement.text = newLiquedRequirement.toString()
            binding.tvFulfillmentPercents.text = (viewModel.currentlyDrunkLiquid.value!!.times(100) / newLiquedRequirement).toString()
            binding.progressBarFulfillment.setProgress(viewModel.currentlyDrunkLiquid.value!!.times(100) / newLiquedRequirement)
        })

        viewModel.currentlyDrunkLiquid.observe(viewLifecycleOwner, { newDrunkWater ->
            binding.tvDrunk.text = newDrunkWater.toString()
            binding.tvFulfillmentPercents.text = (newDrunkWater * 100 / viewModel.dailyLiquidRequirement.value!!).toString()
            binding.progressBarFulfillment.setProgress(binding.tvFulfillmentPercents.text.toString().toInt())
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.addEntityInHistory()
    }

    //dialogFragment for entering user data
    private fun showUserInputDialog()
    {
        //Set Dialog variable and inflate ViewModel
        val dialogView = layoutInflater.inflate(R.layout.user_data_dialog_fragment, null)
        val dialogBuilder = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setTitle(getString(R.string.user_input_dialog_titel))
        val dialog = dialogBuilder.show()

        //Cancel button -> leave the dialog
        val cancelButton = dialogView.findViewById<Button>(R.id.imageButtonUserCancel)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        //Ok Button -> saving the entered parameters in the internal storage
        val okButton = dialogView.findViewById<Button>(R.id.imageButtonUserOk)

        okButton.setOnClickListener{
            //If gender == woman =>1 else 0
            val genderSelected =
                dialogView.findViewById<RadioGroup>(R.id.radioGroupGender).checkedRadioButtonId
            if(genderSelected == R.id.radioButtonWoman){
                viewModel.userGenderIsFemale.value = 1
            }else{
                viewModel.userGenderIsFemale.value = 0
            }

            //Checking in case of not entered value for weight
            if (dialogView.findViewById<TextView>(R.id.editTextUserWeight).text.toString() != "") {
                var weightTemp =  dialogView.findViewById<TextView>(R.id.editTextUserWeight).text.toString()
                    .toInt()



                // TODO: Hier wird bei jeder eingabe zurück auf 23kg korrigiert
                //Correction of entered weight to match the values in table
                //For now only data for weight from 20 till 22 entered
//                if(weightTemp > 23 )
//                    weightTemp = 23
//                if (weightTemp < 20)
//                    weightTemp = 20


                viewModel.weightOfUser.value = weightTemp

                //Update of all key values for water demand
                viewModel.updateDataByStartActivity(viewModel.weightOfUser.value!!.toLong(),
                    viewModel.userGenderIsFemale.value!!)

                dialog.dismiss()

                Toast.makeText(activity, getString(R.string.toast_daten_gesichert), Toast.LENGTH_SHORT).show()
            } else
            {
                Toast.makeText(activity, getString(R.string.toast_gewicht_eingeben), Toast.LENGTH_SHORT).show()
            }
        }
    }
}