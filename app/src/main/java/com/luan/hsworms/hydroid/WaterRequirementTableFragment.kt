package com.luan.hsworms.hydroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.Backend.WaterRequirementTableAdapter

class WaterRequirementTableFragment: Fragment()  {
    private lateinit var rootView: View

    //RecyclerView
    private lateinit var rv: RecyclerView
    private lateinit var adapter: WaterRequirementTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_water_requirement_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view

        initRecyclerView()
    }

    private fun initRecyclerView(){
        rv = rootView.findViewById(R.id.water_rv)
        //Temporary placeholder
        val testContent = ArrayList<String>(List(25){"TEST"})
        adapter = WaterRequirementTableAdapter(testContent)
        rv.adapter = adapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WaterRequirementTableFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}