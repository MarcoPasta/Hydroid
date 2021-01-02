package com.luan.hsworms.hydroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class AddWaterDialogFragment: DialogFragment() {
    private lateinit var rootView: View

    //Views
    private lateinit var btnSave: Button
    private lateinit var btnAbort: Button
    private lateinit var imgSmall: ImageView
    private lateinit var imgMiddle: ImageView
    private lateinit var imgBig: ImageView
    private lateinit var imgHuge: ImageView
    private lateinit var tvQuantity:TextView
    private lateinit var tvQuantitySmall:TextView
    private lateinit var tvQuantityMiddle:TextView
    private lateinit var tvQuantityBig:TextView
    private lateinit var tvQuantityHuge:TextView

    //ViewModel
    private lateinit var addWaterViewModel: AddWaterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.add_water_dialog, container, false)
        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addWaterViewModel = ViewModelProvider(requireActivity(),
        AddWaterViewModelFactory(requireActivity().application)).get(AddWaterViewModel::class.java)

        initButtons()
        initTextView()
        initImageViews()
    }

    private fun initButtons(){
        btnSave = rootView.findViewById(R.id.btn_add_water_ok)
        btnAbort = rootView.findViewById(R.id.btn_add_water_cancel)

        btnSave.setOnClickListener{ saveData() }
        btnAbort.setOnClickListener{ dismiss() }
    }

    private fun initImageViews(){
        imgSmall = rootView.findViewById(R.id.iv_glass_small)
        imgMiddle = rootView.findViewById(R.id.iv_glass_middle)
        imgBig = rootView.findViewById(R.id.iv_glass_big)
        imgHuge = rootView.findViewById(R.id.iv_glass_huge)

        imgSmall.setOnClickListener{ tvQuantity.setText(tvQuantitySmall.text) }
        imgMiddle.setOnClickListener{ tvQuantity.setText(tvQuantityMiddle.text) }
        imgBig.setOnClickListener{ tvQuantity.setText(tvQuantityBig.text) }
        imgHuge.setOnClickListener{ tvQuantity.setText(tvQuantityHuge.text) }
    }

    private fun initTextView(){
        tvQuantity = rootView.findViewById(R.id.tv_quantity)
        tvQuantity.setText(addWaterViewModel.consum.toString())
    }

    private fun saveData(){

        //TODO: muss implementiert werden, Verbindung mit DB erstellen
        dismiss()
    }
}