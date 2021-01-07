package com.luan.hsworms.hydroid

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement

//Incoming parameter for the class in the case of using a dialog to change values
class WaterRequirementTableDialogFragment(var waterRequirement: WaterRequirement? = null) : DialogFragment() {
    private lateinit var rootView: View

    //Views
    private lateinit var btnSave: Button
    private lateinit var btnAbort: Button

    private lateinit var etWeight: TextInputLayout
    private lateinit var etWater: TextInputLayout

    private lateinit var rgGender: RadioGroup

    private lateinit var tvTitel: TextView
    private lateinit var tvComment: TextView

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
        initRadioButton()
        initTextView()

        //Check if the dialog is used to add or change values. If update, then the input parameter is != null.
        if(waterRequirement != null){
            //If gender == woman =>true else false
            val gender:Boolean = waterRequirement!!.genderWoman

            if(gender){
                rgGender.check(R.id.radioButtonWoman)
            }else{
                rgGender.check(R.id.radioButtonMan)
            }

            etWeight.editText?.setText(waterRequirement?.weight.toString())
            etWater.editText?.setText(waterRequirement?.requirements.toString())
            tvTitel.setText("Datensatz ändern")
            tvComment.setText("In diesem Dialog können Sie die Werte ändern")
        }
    }

    //Initializing Dialog Box Buttons and Running Functions when Pressed
    private fun initButtons() {
        btnSave = rootView.findViewById(R.id.dialog_btn_save)
        btnAbort = rootView.findViewById(R.id.dialog_btn_abort)

        btnSave.setOnClickListener { saveData() }
        btnAbort.setOnClickListener { dismiss() }
    }

    //Initializing Variables by Dialog Components
    private fun initEditText() {
        etWeight = rootView.findViewById(R.id.dialog_edit_text_weight)
        etWater = rootView.findViewById(R.id.dialog_edit_text_amount_of_water)
    }

    private fun initRadioButton(){
        rgGender = rootView.findViewById(R.id.radioGroupGender)
    }

    private fun initTextView(){
        tvTitel = rootView.findViewById(R.id.tv_water_requirement_dialog_titel)
        tvComment = rootView.findViewById(R.id.tv_water_requirement_dialog_comment)
    }

    //Saving the data entered in the dialog in the database
    private fun saveData() {
        val genderSelected =
            rootView.findViewById<RadioGroup>(R.id.radioGroupGender).checkedRadioButtonId
        //If gender == woman =>true else false
        val gender:Boolean = (genderSelected == R.id.radioButtonWoman)

        //Adding a record to the database, provided that both textViews contain text
        if (!TextUtils.isEmpty(etWeight.editText?.text.toString())
            && !TextUtils.isEmpty(etWater.editText?.text.toString())) {

                if (waterRequirement != null){//In case of entity change

                    waterRequirement?.requirements = etWater.editText?.text.toString()!!.toInt()
                    waterRequirement?.weight = etWeight.editText?.text.toString()!!.toInt()
                    waterRequirement?.genderWoman = gender

                    waterRequirementTableViewModel.update(waterRequirement!!)
                    dismiss()

                } else{//In case of add new entity
                    waterRequirementTableViewModel.insert(gender,  etWeight.editText?.text.toString()!!.toInt(),
                        etWater.editText?.text.toString()!!.toInt())

                    Toast.makeText(requireContext(), "Neue Werte zur Datenbank hinzugefügt", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            //Otherwise, display a message about the need to fill in the fields
        }else{
            Toast.makeText(requireContext(), "Bitte füllen Sie beide Felder aus", Toast.LENGTH_SHORT).show()
        }
    }
}