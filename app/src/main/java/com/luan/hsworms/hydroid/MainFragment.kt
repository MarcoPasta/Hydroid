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
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.luan.hsworms.hydroid.backend.notifications.NotificationActivity
import com.luan.hsworms.hydroid.databinding.MainFragmentBinding

// For debugging
private const val TAG = "MainFragment"

/**
 * A class for handling events related to the main screen of the program.
 *
 */
class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var waterRequirementTableViewModel: WaterRequirementTableViewModel
    private var notiToken = 1

    //TODO: MainActivity dokumentieren
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        //Initializing of ViewModel
        viewModel = ViewModelProvider(requireActivity(),
        MainViewModelFactory(requireActivity().application)
        ).get(MainViewModel::class.java)

        // Initialization of notificationViewModel
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        notificationViewModel.notificationPreference = context?.getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
        notificationViewModel.loadData()

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

        waterRequirementTableViewModel = ViewModelProvider(requireActivity(),
            WaterRequirementTableViewModelFactory(requireActivity().application))
            .get(WaterRequirementTableViewModel::class.java)

        activity?.actionBar?.title = "Hydroid"

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
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

        //Check if the Start is first
        if(viewModel.isFirstStart == 1){//first start
            viewModel.clearFile()
            //viewModel.saveFirstStart(0)//From now is not first start
            //showUserInputDialog()
        }else{//not first start
            viewModel.updateDataByStartActivity(viewModel.weightOfUser.value!!.toLong(),
                viewModel.userGenderIsFemale.value!!)
        }

        Log.i("on create view", viewModel.ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 0).toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("onViewCreated", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        viewModel.dailyLiquidRequirement.observe(viewLifecycleOwner, { newLiquedRequirement ->
            binding.tvDailyRequirement.text = newLiquedRequirement.toString()
            binding.tvFulfillmentPercents.text = (viewModel.currentlyDrunkLiquid.value!!.times(100) / newLiquedRequirement).toString()
            binding.progressBarFulfillment.progress = viewModel.currentlyDrunkLiquid.value!!.times(100) / newLiquedRequirement
        })

        viewModel.currentlyDrunkLiquid.observe(viewLifecycleOwner, { newDrunkWater ->
            binding.tvDrunk.text = newDrunkWater.toString()
            binding.tvFulfillmentPercents.text = (newDrunkWater * 100 / viewModel.dailyLiquidRequirement.value!!).toString()
            Log.d(TAG, "Prozent: " + binding.tvFulfillmentPercents.text.toString().toInt())
            if(binding.tvFulfillmentPercents.text.toString().toInt() == 0)
                notiToken = 0
            Log.d(TAG, "notiToken: $notiToken")
            if(binding.tvFulfillmentPercents.text.toString().toInt() >= 100 && notiToken < 1) {
                NotificationActivity.goalAchievedNotification(
                    notificationViewModel.switchBoolNotification,
                    "Send Notification on 100 percent",
                    1,
                    requireContext(),
                    R.drawable.ic_drop_48,
                    "Du hast dein Ziel erreicht",
                    "Du hast dein Tagesziel erreicht, sehr gut!",
                    "Wenn du weiterhin so viel trinkst, wirkt sich das Positiv auf deine Gesundheit aus!",
                    NotificationCompat.PRIORITY_DEFAULT
                )
                notiToken = 1
            }
            binding.progressBarFulfillment.progress = binding.tvFulfillmentPercents.text.toString().toInt()
        })


        if(viewModel.isFirstStart == 1) //first start
            showUserInputDialog()
    }

    override fun onResume() {
        super.onResume()
        //The function is called in this callback function in case, when the date changed, the application
        // was not closed and when it was resumed, a new day was recorded in the Database History table
        if(viewModel.isFirstStart == 0) {//not first start
            viewModel.addEntityInHistory()
        }
    }

    //TODO: Implementing the showUserInputDialog in a separate class
    /**
     * This function will be called at the first start of the program to display a dialog in which the user can enter his gender and weight.
     */
    private fun showUserInputDialog()
    {
        //Set Dialog variable and inflate ViewModel
        val dialogView = layoutInflater.inflate(R.layout.user_data_dialog_fragment, null)
        val dialogBuilder = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setTitle(getString(R.string.user_input_dialog_titel))
        val dialog = dialogBuilder.show()

        //Prevents the dialog from closing by clicking outside its borders
        dialog.setCancelable(false)

        //Cancel button -> leave the dialog
        val cancelButton = dialogView.findViewById<Button>(R.id.imageButtonUserCancel)
        cancelButton.setOnClickListener {

            dialog.dismiss()

            viewModel.saveFirstStart(0)//From now is not first start

            //Update of all values
            viewModel.updateDataByStartActivity(viewModel.weightOfUser.value!!.toLong(),
                viewModel.userGenderIsFemale.value!!)

            Toast.makeText(activity, getString(R.string.toast_user_data_dont_entered), Toast.LENGTH_LONG).show()
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

            viewModel.saveFirstStart(0)//From now is not first start

            //Checking in case of not entered value for weight.
            //If not, there will be a Toast with a proposal to enter the weight
            if (dialogView.findViewById<TextView>(R.id.editTextUserWeight).text.toString() != "") {
                val weightTemp =  dialogView.findViewById<TextView>(R.id.editTextUserWeight).text.toString()
                    .toInt()

                viewModel.weightOfUser.value = weightTemp

                //Update of all values
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