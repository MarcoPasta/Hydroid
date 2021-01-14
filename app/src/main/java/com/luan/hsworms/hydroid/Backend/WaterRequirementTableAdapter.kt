package com.luan.hsworms.hydroid.Backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement
import com.luan.hsworms.hydroid.R

class WaterRequirementTableAdapter (var content:ArrayList<WaterRequirement>):RecyclerView.Adapter<WaterRequirementTableAdapter.ViewHolder>(){

    //Interface
    private lateinit var mItemListener:OnItemClickListener
    private lateinit var mItemLongListener:OnItemLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_water_requirement, parent, false)
        return  ViewHolder(view, mItemListener, mItemLongListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waterRequirement = content[position]
        var gender = "Weiblich"
        if(!waterRequirement.genderWoman){
            gender = "Männlich"
        }

        holder.tvGender.text = gender
        holder.tvRequirement.text = waterRequirement.requirements.toString()
        holder.tvWeight.text = waterRequirement.weight.toString()
    }

    override fun getItemCount(): Int {
        return content.size
    }

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

    fun updateContent(content: ArrayList<WaterRequirement>){
        this.content = content
        //Update of content
        notifyDataSetChanged()
    }


    ////////////////////////////////////////
    //Interfaces
    interface OnItemClickListener{
        fun setOnItemClickListener(position: Int)
    }

    fun setOnItemClickListener(mItemListener:OnItemClickListener){
        this.mItemListener = mItemListener
    }

    interface OnItemLongClickListener{
        fun setOnItemLongClickListener(position: Int)
    }

    fun setOnItemLongClickListener(mLongClickListener: OnItemLongClickListener){
        this.mItemLongListener = mLongClickListener
    }
}