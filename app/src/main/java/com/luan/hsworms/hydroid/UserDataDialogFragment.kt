package com.luan.hsworms.hydroid

import android.app.Dialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.coroutines.NonCancellable.cancel

//class UserDataDialogFragment : Fragment() {
//
//    companion object {
//        fun newInstance() = UserDataDialogFragment()
//    }
//
//    private lateinit var viewModel: UserDataDialogViewModel
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.user_data_dialog_fragment, container, false)
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(UserDataDialogViewModel::class.java)
//        // TODO: Use the ViewModel
//    }
//
//}

class UserDataDialogFragment : DialogFragment() {

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//    //When called from the navigation drawer
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        var rootView:View = inflater.inflate(R.layout.user_data_dialog_fragment, null)
//        return  rootView
//
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i("onCreateDialog","onCreateDialog")
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.user_data_dialog_fragment, null))
                // Add action buttons
                .setPositiveButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
                    })
//                .setNegativeButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        getDialog()?.cancel()
//                    })
                .setTitle("Benutzerdaten")
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}