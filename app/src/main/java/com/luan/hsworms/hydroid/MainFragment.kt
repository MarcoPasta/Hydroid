package com.luan.hsworms.hydroid

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.luan.hsworms.hydroid.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userInput()
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

        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //dialogFragment for entering user data
    fun userInput() {
        val newFragment = UserDataDialogFragment()
        newFragment.show(childFragmentManager, "userdata")
    }

}