package com.luan.hsworms.hydroid

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
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
    private lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.FullScreenDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View
    {
        rootView = inflater.inflate(R.layout.add_water_dialog, container, false)
        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity(),
        MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)

        initButtons()
        initTextView()
        initImageViews()

        btnSave.setOnClickListener{ saveData(tvQuantity.text.toString().toInt()) }
    }

    private fun initButtons(){
        btnSave = rootView.findViewById(R.id.btn_add_water_ok)
        btnAbort = rootView.findViewById(R.id.btn_add_water_cancel)

        btnAbort.setOnClickListener{ dismiss() }
    }

    private fun initImageViews(){
        imgSmall = rootView.findViewById(R.id.iv_glass_small)
        imgMiddle = rootView.findViewById(R.id.iv_glass_middle)
        imgBig = rootView.findViewById(R.id.iv_glass_big)
        imgHuge = rootView.findViewById(R.id.iv_glass_huge)

        imgSmall.setOnClickListener{
            tvQuantity.setText(tvQuantitySmall.text)

            NotificationActivity.showNotification(
                "AddSmallWater",
                1,
                requireContext(),
                R.drawable.ic_done,
                "Glückwunsch!",
                "Du hast 50ml getrunken!",
                null,
                NotificationCompat.PRIORITY_DEFAULT,
                "AddWater50mlChannel",
                "Added50MlWater",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }
        imgMiddle.setOnClickListener{
            tvQuantity.setText(tvQuantityMiddle.text)
            NotificationActivity.showNotification(
                "AddMiddleWater",
                1,
                requireContext(),
                R.drawable.ic_done,
                "Glückwunsch!",
                "Du hast 100ml getrunken!",
                null,
                NotificationCompat.PRIORITY_DEFAULT,
                "AddWater100mlChannel",
                "Added100MlWater",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }
        imgBig.setOnClickListener{ tvQuantity.setText(tvQuantityBig.text) }
        imgHuge.setOnClickListener{ tvQuantity.setText(tvQuantityHuge.text) }
    }

    private fun initTextView(){
        tvQuantitySmall = rootView.findViewById(R.id.tv_volume_small)
        tvQuantityMiddle = rootView.findViewById(R.id.tv_volume_middle)
        tvQuantityBig = rootView.findViewById(R.id.tv_volume_big)
        tvQuantityHuge = rootView.findViewById(R.id.tv_volume_huge)
        tvQuantity = rootView.findViewById(R.id.tv_quantity)
        //tvQuantity.setText(mainViewModel.currentlyDrunkLiquid.value.toString())
        tvQuantity.setText("0")
    }

    private fun saveData(waterIn:Int){
        mainViewModel.addDrunkWater(waterIn)
        dismiss()
    }
}