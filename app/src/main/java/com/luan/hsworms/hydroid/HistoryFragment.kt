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
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.Backend.HistoryListAdapter

/**
 * Class for fragment HistoryFragment. Which is the output of the record from the Database History table. And also provides for deleting these records by implementing LongClickListener
 *
 * @author Andrej Alpatov
 */
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

        //Initializing of HistoryViewModel
        historyViewModel = ViewModelProvider(requireActivity(),
            HistoryViewModelFactory(requireActivity().application)).get(HistoryViewModel::class.java)

        /**
         *         History LiveData observer
         */
        historyViewModel.getHistoryLiveData().observe(viewLifecycleOwner, {
            items ->
            adapter.updateContent(ArrayList(items))
        })
    }

    private fun initRecyclerView(){
        rv = rootView.findViewById(R.id.history_rv)

        adapter = HistoryListAdapter(ArrayList())
        rv.adapter = adapter

        /**
         * LongClickListener Delete Entity in BD by long click
         */
        adapter.setOnItemLongClickListener(object: HistoryListAdapter.OnItemLongClickListener{
            override fun setOnItemLongClickListener(position: Int) {
                startAlarmDialog(adapter.content[position])
            }
        })
    }

    /**
     * Start of a dialog called by LongClickListener processing, which allows deleting the selected record from the database when the ok button is clicked.
     *
     * @param history Entry of History data base table
     */
    private fun startAlarmDialog(history: History){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage("Warnung - Eintrag wird gelöscht")
            setPositiveButton("OKAY") { _, _ ->
                Toast.makeText(
                    requireContext(),
                    "Eintrag ${history.date} wurde gelöscht",
                    Toast.LENGTH_SHORT
                ).show()
                historyViewModel.delete(history)
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