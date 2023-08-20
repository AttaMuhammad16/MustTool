package com.musttool.adapters

import android.content.Context
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.musttool.R

class AvailableSensorsAdapter(var list: MutableList<Sensor>, var context: Context) : RecyclerView.Adapter<AvailableSensorsAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sensorTv=itemView.findViewById<TextView>(R.id.sensorTv)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.available_sensors_item_layout, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder,  position: Int) {
        holder.sensorTv.text = list[position].toString().substring(1, list[position].toString().length - 1).replace(",", "\n")
    }
    override fun getItemCount(): Int {
        return list.size
    }

}