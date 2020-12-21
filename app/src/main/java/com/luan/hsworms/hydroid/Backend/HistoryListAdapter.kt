package com.luan.hsworms.hydroid.Backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.R

class HistoryListAdapter(var content:ArrayList<String>):RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    //Icons showing whether the daily target is met
    private val statusDrawables = arrayOf(R.drawable.ic_done, R.drawable.ic_not_done)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return content.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var tvDrunk: TextView = itemView.findViewById(R.id.item_liquidDrunk)
        var tvLiquidRequirement: TextView = itemView.findViewById(R.id.item_liquid_requirement)
        var tvRequirement: TextView = itemView.findViewById(R.id.item_daily_requirement)
        var tvProcent: TextView = itemView.findViewById(R.id.item_weight)
        var imageDelete: ImageView = itemView.findViewById(R.id.item_image_delete)
    }
}