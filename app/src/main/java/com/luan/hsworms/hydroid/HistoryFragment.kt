package com.luan.hsworms.hydroid

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement
import com.luan.hsworms.hydroid.Backend.HistoryListAdapter
import com.luan.hsworms.hydroid.Backend.WaterRequirementTableAdapter

class HistoryFragment : Fragment() {

    /**
     * To initialize objects with findViewById
     */
    private lateinit var rootView:View

    //RecyclerView
    private lateinit var rv:RecyclerView
    private lateinit var adapter:HistoryListAdapter

    //History ViewModel
    private lateinit var historyViewModel: HistoryViewModel

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
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view

        initRecyclerView()

        //Displaying information from the database to the screen
        historyViewModel = ViewModelProvider(requireActivity(),
            HistoryViewModelFactory(requireActivity().application)).get(HistoryViewModel::class.java)

        historyViewModel.getHistoryLiveData().observe(viewLifecycleOwner, Observer {
            items ->
            adapter.updateContent(ArrayList(items))
        })
    }

    private fun initRecyclerView(){
        rv = rootView.findViewById(R.id.history_rv)
        //Temporary placeholder
        val testContent = ArrayList<String>(List(25){"TEST"})
        adapter = HistoryListAdapter(ArrayList())
        rv.adapter = adapter

        //Implement LongClickListener
        //Delete Entity in BD by long click
        adapter.setOnItemLongClickListener(object: HistoryListAdapter.OnItemLongClickListener{
            override fun setOnItemLongClickListener(position: Int) {
                startAlarmDialog(adapter.content[position])
            }
        })
    }

    private fun startAlarmDialog(history: History){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("Warnung - Eintrag wird gelöscht")
            setPositiveButton("OKAY", DialogInterface.OnClickListener { _, _ ->
                Toast.makeText(requireContext(),
                    "Eintrag ${history.date} wurde gelöscht",
                    Toast.LENGTH_SHORT).show()
                historyViewModel.delete(history)
            })
            setNegativeButton("Ablehnen") { dialog, _ ->
                dialog.dismiss()
            }
            setTitle("Bestätigung der Entfernung des Eintrags")
        }
        val dialog = builder.create()
        dialog.show()
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}