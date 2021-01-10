package com.luan.hsworms.hydroid

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
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
        mainViewModel.addWaterRequirementBecauseOfSport(addWater)
        dismiss()
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        Log.i("onCreateDialog","onCreateDialog")
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            // Get the layout inflater
//            val inflater = requireActivity().layoutInflater;
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(inflater.inflate(R.layout.fragment_sport_dialog, null))
//                // Add action buttons
////                .setPositiveButton(R.string.cancel,
////                    DialogInterface.OnClickListener { dialog, id ->
////                        // sign in the user ...
////                    })
////                .setNegativeButton(R.string.cancel,
////                    DialogInterface.OnClickListener { dialog, id ->
////                        getDialog()?.cancel()
////                    })
//                .setTitle("Sport")
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
}











//class WetterDialogFragment : DialogFragment() {
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            builder.setTitle(R.string.pick_color)
//                .setItems(R.array.colors_array,
//                    DialogInterface.OnClickListener { dialog, which ->
//                        // The 'which' argument contains the index position
//                        // of the selected item
//                    })
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
//
//}










//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"
//
///**
// * A simple [Fragment] subclass.
// * Use the [WetterDialogFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class WetterDialogFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_wetter_dialog, container, false)
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment WetterDialogFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            WetterDialogFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
//}

