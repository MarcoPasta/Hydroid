package com.luan.hsworms.hydroid.Backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.R

class HistoryListAdapter(var content: ArrayList<History>) :
    RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    //Interface
    private lateinit var mItemLongListener:  OnItemLongClickListener

    //Icons showing whether the daily target is met
    //TODO: not yet implemented
    private val statusDrawables = arrayOf(R.drawable.ic_done, R.drawable.ic_not_done)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view, mItemLongListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = content[position]

        holder.date.text = history.date
        holder.tvDrunk.text = history.drunk.toString()
        holder.tvLiquidRequirement.text = history.requirements.toString()
        holder.tvFulfillment.text = history.fulfillment.toString()
        holder.tvWeight.text = history.weight.toString()
    }

    override fun getItemCount(): Int {
        return content.size
    }

    class ViewHolder(itemView: View, mItemLongClickListener: OnItemLongClickListener) : RecyclerView.ViewHolder(itemView) {

        var date: TextView = itemView.findViewById(R.id.item_date)
        var tvDrunk: TextView = itemView.findViewById(R.id.item_liquidDrunk)
        var tvLiquidRequirement: TextView = itemView.findViewById(R.id.item_liquid_requirement)
        var tvFulfillment: TextView = itemView.findViewById(R.id.item_daily_requirement)
        var tvWeight:TextView = itemView.findViewById(R.id.item_weight)
        var imageDelete: ImageView = itemView.findViewById(R.id.item_image_delete)

        //Implement ClickListener
        init {
            itemView.setOnLongClickListener {
                mItemLongClickListener?.setOnItemLongClickListener(adapterPosition)

                true
            }
        }
    }

    fun updateContent(content: ArrayList<History>){
        this.content = content
        //Update of content
        notifyDataSetChanged()
    }

    ////////////////////////////////////////
    //Interface
    interface OnItemLongClickListener{
        fun setOnItemLongClickListener(position: Int)
    }

    fun setOnItemLongClickListener(mLongClickListener: OnItemLongClickListener){
        this.mItemLongListener = mLongClickListener
    }
}