package com.luan.hsworms.hydroid.Backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement
import com.luan.hsworms.hydroid.R

/**
 * Class - adapter for working with the recyclerView Layout and its interaction with the WaterRequirement database table
 *
 * @author Andrej Alpatov
 */
class WaterRequirementTableAdapter (var content:ArrayList<WaterRequirement>):RecyclerView.Adapter<WaterRequirementTableAdapter.ViewHolder>(){

    /**
     * Interface for ClickListener
     */
    private lateinit var mItemListener:OnItemClickListener
    /**
     * Interface for LongClickListener
     */
    private lateinit var mItemLongListener:OnItemLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /**
         * layout inflate
         */
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_water_requirement, parent, false)
        return  ViewHolder(view, mItemListener, mItemLongListener)
    }

    /**
     * Populating Layout Objects with Values from the WaterRequirement Database Table
     *
     * @param holder ViewHolder object
     * @param position Position of object, to be filled
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /**
         * WaterRequirement object on the position [position]
         */
        val waterRequirement = content[position]

        /**
         * User gender
         */
        var gender = "Weiblich"
        if(!waterRequirement.genderWoman){
            gender = "Männlich"
        }

        holder.tvGender.text = gender
        holder.tvRequirement.text = waterRequirement.requirements.toString()
        holder.tvWeight.text = waterRequirement.weight.toString()
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
     * Class for initializing all required layout objects und Implementation of LongClickListener and ClickListener
     */
    class ViewHolder(itemView: View, mItemListener: OnItemClickListener,
                     mItemLongClickListener: OnItemLongClickListener): RecyclerView.ViewHolder(itemView){

        var tvGender: TextView = itemView.findViewById(R.id.item_gender)
        var tvWeight: TextView = itemView.findViewById(R.id.item_weight_table)
        var tvRequirement: TextView = itemView.findViewById(R.id.item_requirement)

        //Implementation of 2 ClickListener
        init {
            itemView.setOnClickListener {
                mItemListener?.setOnItemClickListener(adapterPosition)
            }

            itemView.setOnLongClickListener {
                mItemLongClickListener?.setOnItemLongClickListener(adapterPosition)

                true
            }
        }
    }

    /**
     * Updating the recycler with new data
     *
     * @param content new content (ArrayList of WaterRequirement database table records)
     */
    fun updateContent(content: ArrayList<WaterRequirement>){
        this.content = content
        //Update of content
        notifyDataSetChanged()
    }


    ////////////////////////////////////////
    //Interfaces

    /**
     * Implementation of interface for ClickListener
     */
    interface OnItemClickListener{
        /**
         * Implementation of ClickListener for a specific position
         *
         * @param position  Position, click on which will be processed
         */
        fun setOnItemClickListener(position: Int)
    }

    /**
     * Implementation of clicksListener
     *
     * @param mLongClickListener interface for LongClickListener
     */
    fun setOnItemClickListener(mItemListener:OnItemClickListener){
        this.mItemListener = mItemListener
    }

    /**
     * Implementation of interface for LongClickListener
     */
    interface OnItemLongClickListener{
        /**
         * Implementation of LongClickListener for a specific position
         *
         * @param position  Position, click on which will be processed
         */
        fun setOnItemLongClickListener(position: Int)
    }

    /**
     * Implementation of clicksListener
     *
     * @param mLongClickListener interface for LongClickListener
     */
    fun setOnItemLongClickListener(mLongClickListener: OnItemLongClickListener){
        this.mItemLongListener = mLongClickListener
    }
}