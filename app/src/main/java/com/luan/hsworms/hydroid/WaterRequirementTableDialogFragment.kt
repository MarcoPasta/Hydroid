package com.luan.hsworms.hydroid

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout

class WaterRequirementTableDialogFragment : DialogFragment() {
    private lateinit var rootView: View

    //Views
    private lateinit var btnSave: Button
    private lateinit var btnAbort: Button

    private lateinit var etWeight: TextInputLayout
    private lateinit var etWater: TextInputLayout

    //ViewModel
    private lateinit var waterRequirementTableViewModel: WaterRequirementTableViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.water_requirement_dialog, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waterRequirementTableViewModel = ViewModelProvider(requireActivity(),
        WaterRequirementTableViewModelFactory(requireActivity().application)
        ).get(WaterRequirementTableViewModel::class.java)

        initButtons()
        initEditText()
    }

    private fun initButtons() {
        btnSave = rootView.findViewById(R.id.dialog_btn_save)
        btnAbort = rootView.findViewById(R.id.dialog_btn_abort)

        btnSave.setOnClickListener { saveData() }
        btnAbort.setOnClickListener { dismiss() }
    }

    private fun initEditText() {
        etWeight = rootView.findViewById(R.id.dialog_edit_text_weight)
        etWater = rootView.findViewById(R.id.dialog_edit_text_amount_of_water)
    }

    private fun saveData() {
        val genderSelected =
            rootView.findViewById<RadioGroup>(R.id.radioGroupGender).checkedRadioButtonId
        //If gender == woman =>true else false
        val gender:Boolean = (genderSelected == R.id.radioButtonWoman)

        if (!TextUtils.isEmpty(etWeight.editText?.text.toString())
            && !TextUtils.isEmpty(etWater.editText?.text.toString())) {

            waterRequirementTableViewModel.insert(gender,  etWeight.editText?.text.toString()!!.toInt(),
                etWater.editText?.text.toString()!!.toInt())

            Toast.makeText(requireContext(), "Neue Werte zur Datenbank hinzugefügt", Toast.LENGTH_SHORT).show()
            dismiss()
        }else{
            Toast.makeText(requireContext(), "Bitte füllen Sie beide Felder aus", Toast.LENGTH_SHORT).show()
        }
    }
}