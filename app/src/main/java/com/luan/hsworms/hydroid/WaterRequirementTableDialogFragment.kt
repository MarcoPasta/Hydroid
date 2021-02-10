package com.luan.hsworms.hydroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.luan.hsworms.hydroid.backend.database.WaterRequirement
import kotlin.math.roundToInt

/**
 *This class is used for the WaterRequirementTableDialog dialog box. Which is used to add and modify records in the WaterRequirement table of the database.
 *
 * @param waterRequirement Record in the database table WaterRequirement. Used when you need to change a database record. (type: WaterRequirement?)
 */
class WaterRequirementTableDialogFragment(private var waterRequirement: WaterRequirement? = null) : DialogFragment() {

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
    ): View {
        rootView = inflater.inflate(R.layout.water_requirement_dialog, container, false)

        return rootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waterRequirementTableViewModel = ViewModelProvider(requireActivity(),
        WaterRequirementTableViewModelFactory(requireActivity().application)
        ).get(WaterRequirementTableViewModel::class.java)

        //Initializing all layout objects
        initButtons()
        initEditText()
        initRadioButton()
        initTextView()

        //Check if the dialog is used to add or change values. If update, then the input parameter is != null.
        if(waterRequirement != null) {
            //If gender == woman =>true else false
            val gender:Boolean = waterRequirement!!.genderWoman

            if(gender) {
                rgGender.check(R.id.radioButtonWoman)
            }else {
                rgGender.check(R.id.radioButtonMan)
            }

            //Filling layout objects
            etWeight.editText?.setText(waterRequirement?.weight.toString())
            etWater.editText?.setText(waterRequirement?.requirements.toString())
            tvTitel.text = "Datensatz ändern"
            tvComment.text = "In diesem Dialog können Sie die Werte ändern"
        }
    }

    /**
     * Initializing Dialog Box Buttons Ok and cancel. Implementation of ClickListener
     */
    private fun initButtons() {
        btnSave = rootView.findViewById(R.id.dialog_btn_save)
        btnAbort = rootView.findViewById(R.id.dialog_btn_abort)

        btnSave.setOnClickListener { saveData() }
        btnAbort.setOnClickListener { dismiss() }
    }

    /**
     * Initializing of EditText Objects for weight and water amount
     */
    private fun initEditText() {
        etWeight = rootView.findViewById(R.id.dialog_edit_text_weight)
        etWater = rootView.findViewById(R.id.dialog_edit_text_amount_of_water)
    }

    /**
     * Initializing of RadioGroup Object
     */
    private fun initRadioButton() {
        rgGender = rootView.findViewById(R.id.radioGroupGender)
    }

    /**
     * Initializing of TextView Objects for titel and Comment
     */
    private fun initTextView() {
        tvTitel = rootView.findViewById(R.id.tv_water_requirement_dialog_titel)
        tvComment = rootView.findViewById(R.id.tv_water_requirement_dialog_comment)
    }

    /**
     * Saving the data entered or changed in the dialog in the database
     */
    private fun saveData() {
        val genderSelected =
            rootView.findViewById<RadioGroup>(R.id.radioGroupGender).checkedRadioButtonId
        //If gender == woman =>true else false
        val gender:Boolean = (genderSelected == R.id.radioButtonWoman)

        //Adding a record to the database, provided that both textViews contain text
        if (!TextUtils.isEmpty(etWeight.editText?.text.toString())
            && !TextUtils.isEmpty(etWater.editText?.text.toString())) {

                if (waterRequirement != null) {     //In case of entity change

                    waterRequirement?.requirements = etWater.editText?.text.toString().toFloat().roundToInt()
                    waterRequirement?.weight = etWeight.editText?.text.toString().toFloat().roundToInt()
                    waterRequirement?.genderWoman = gender

                    waterRequirementTableViewModel.update(waterRequirement!!)
                    dismiss()

                } else {//In case of add new entity
                    waterRequirementTableViewModel.insert(gender,
                        etWeight.editText?.text.toString().toFloat().roundToInt(),
                        etWater.editText?.text.toString().toFloat().roundToInt())

                    Toast.makeText(requireContext(), "Neue Werte zur Datenbank hinzugefügt", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            //Otherwise, display a message about the need to fill in the fields
        } else {
            Toast.makeText(requireContext(), "Bitte füllen Sie beide Felder aus", Toast.LENGTH_SHORT).show()
        }
    }
}