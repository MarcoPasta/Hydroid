package com.luan.hsworms.hydroid

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

class UserInputFragment : Fragment() {

    companion object {
        fun newInstance() = UserInputFragment()
    }

    private lateinit var viewModel: UserInputViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val navController = this.findNavController()
//        navController.navigate(R.id.action_userInputFragment_to_logoFragment)

        return inflater.inflate(R.layout.user_input_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserInputViewModel::class.java)
        // TODO: Use the ViewModel
    }

}