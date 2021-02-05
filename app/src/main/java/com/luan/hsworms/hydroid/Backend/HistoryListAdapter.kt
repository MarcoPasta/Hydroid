package com.luan.hsworms.hydroid.Backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.R

/**
 * Class - adapter for working with the recyclerView Layout and its interaction with the History database table
 */
class HistoryListAdapter(var content: ArrayList<History>) :
    RecyclerView.Adapter<HistoryListAdapter.ViewHolder>() {

    //Interface
    private lateinit var mItemLongListener:  OnItemLongClickListener

    /**
     * Create a ViewHolder
     *
     * @return ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /**
         * layout inflate
         */
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view, mItemLongListener)
    }

    /**
     * Populating Layout Objects with Values from the History Database Table
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = content[position]

        holder.date.text = history.date
        holder.tvDrunk.text = history.drunk.toString()
        holder.tvLiquidRequirement.text = history.requirements.toString()
        holder.tvFulfillment.text = history.fulfillment.toString()
        holder.tvWeight.text = history.weight.toString()
    }

    /**
     * Returns the number of records in a table
     *
     * @return number of records in a table (type: Int)
     */
    override fun getItemCount(): Int {
        return content.size
    }

    /**
     * Class for initializing all required layout objects und Implementation of LongClickListener
     */
    class ViewHolder(itemView: View, mItemLongClickListener: OnItemLongClickListener) : RecyclerView.ViewHolder(itemView) {

        var date: TextView = itemView.findViewById(R.id.item_date)
        var tvDrunk: TextView = itemView.findViewById(R.id.item_liquidDrunk)
        var tvLiquidRequirement: TextView = itemView.findViewById(R.id.item_liquid_requirement)
        var tvFulfillment: TextView = itemView.findViewById(R.id.item_daily_requirement)
        var tvWeight:TextView = itemView.findViewById(R.id.item_weight)
        var imageDelete: ImageView = itemView.findViewById(R.id.item_image_delete)

        //Implement longClickListener
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