package com.luan.hsworms.hydroid

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
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

        //Moved the initialization of view from onActivityCreated to onCreateView, since
        // onCreateView occurs earlier and in it you can already work with variables from the view
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        //Block of OBSERVERS to the Live Data Objects
        //TODO: Add implementation of observer methods

        viewModel.currentlyDrunkLiquid.observe(viewLifecycleOwner, { newAge ->
            binding.textView.text = newAge.toString()
        })

        viewModel.dailyLiquidRequirement.observe(viewLifecycleOwner, { newAge ->

        })

        return  binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i("onActivityCreated", "onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        //Initializing an object with user data with data from a file
        viewModel.ourUserData = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        //The dialog is launched at the start of the application, only if the default values of the variables have not changed
        //todo: Change the if-condition to ==
        if(viewModel.ageOfUser.value != 0 || viewModel.weightOfUser.value != 0)
            userInput()
    }


    //dialogFragment for entering user data
    fun userInput() {
        val newFragment = UserDataDialogFragment()
        newFragment.show(childFragmentManager, "userdata")
    }

}