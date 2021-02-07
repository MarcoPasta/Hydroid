package com.luan.hsworms.hydroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement
import com.luan.hsworms.hydroid.Backend.WaterRequirementTableAdapter

/**
 * A class for working with a Water Requirement Table menu section, containing a RecyclerView with records from the WaterRequirement table in the database. Implementing the ability to change, delete records from the database using Сliсks on entries. Use ActionButton to add new entries.
 *
 * @author Andrej Alpatov
 */
class WaterRequirementTableFragment: Fragment()  {
    private lateinit var rootView: View

    //RecyclerView
    private lateinit var rv: RecyclerView
    private lateinit var adapter: WaterRequirementTableAdapter

    //WaterRequirementTableViewModel
    private lateinit var waterRequirementTableViewModel: WaterRequirementTableViewModel

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

        //Displaying information from the database to the screen
        waterRequirementTableViewModel = ViewModelProvider(requireActivity(),
        WaterRequirementTableViewModelFactory(requireActivity().application))
            .get(WaterRequirementTableViewModel::class.java)

        //update content in case of changes
        waterRequirementTableViewModel.getLiveWaterRequirement().observe(viewLifecycleOwner, {
            items ->
            adapter.updateContent(ArrayList(items))
        })

        rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            .setOnClickListener{
                val dialog = WaterRequirementTableDialogFragment()
                dialog.show(childFragmentManager, "New Water data input")
            }
    }

    /**
     * Initializing of RecyclerView. Implementing click and long click handlers
     */
    private fun initRecyclerView(){
        rv = rootView.findViewById(R.id.water_rv)
        adapter = WaterRequirementTableAdapter(ArrayList())
        rv.adapter = adapter

        /**
         * Implement ClickListener. Change Entity in BD by short click
         */
        adapter.setOnItemClickListener(object: WaterRequirementTableAdapter.OnItemClickListener{
            override fun setOnItemClickListener(position: Int) {
                val dialog = WaterRequirementTableDialogFragment(adapter.content[position])
                dialog.show(parentFragmentManager, "update WaterRequirementsDialog")
            }
        })

        /**
         * Implement LongClickListener. Delete Entity in BD by long click
         */
        adapter.setOnItemLongClickListener(object: WaterRequirementTableAdapter.OnItemLongClickListener{
            override fun setOnItemLongClickListener(position: Int) {
                startAlarmDialog(adapter.content[position])
            }
        })
    }

    /**
     * Displays a dialog warning about deleting a record in the database. With two buttons "ok" and "cancel"
     */
    private fun startAlarmDialog(waterReqirement: WaterRequirement){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("Warnung - Eintrag wird gelöscht")
                setPositiveButton("OKAY") { _, _ ->
                    Toast.makeText(
                        requireContext(),
                        "Eintrag ${waterReqirement.genderWoman} ${waterReqirement.weight} wurde gelöscht",
                        Toast.LENGTH_SHORT
                    ).show()
                    waterRequirementTableViewModel.delete(waterReqirement)
                }
            setNegativeButton("Ablehnen") { dialog, _ ->
                    dialog.dismiss()
                }
            setTitle("Bestätigung der Entfernung des Eintrags")
        }
        val dialog = builder.create()
        dialog.show()
    }
}