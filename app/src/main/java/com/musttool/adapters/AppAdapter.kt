package com.musttool.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.musttool.R
import com.musttool.activities.*
import com.musttool.models.AppModel
import java.util.*

class AppAdapter(var list: ArrayList<AppModel>, var context: Context) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.litem_list, parent, false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = list[position].title
        Glide.with(context).load(list[position].img).into(holder.itemImage)


        holder.itemView.setOnClickListener {
            var intent: Intent? = null
            when (position) {

                0 -> intent = Intent(holder.itemView.context, GenQRActivity::class.java)
                1 -> intent = Intent(holder.itemView.context, BarCodeScanner::class.java)
                2 -> intent = Intent(holder.itemView.context, TemperatureConverter::class.java)
                3 -> intent = Intent(holder.itemView.context, NotesApp::class.java)
                4 -> intent = Intent(holder.itemView.context, WhatsAppActivity::class.java)
                5 -> intent = Intent(holder.itemView.context, TextExtracter::class.java)
                6 -> intent = Intent(holder.itemView.context, MagneticFiledActivity::class.java)
                7 -> intent = Intent(holder.itemView.context, Acceleration::class.java)
                8 -> intent = Intent(holder.itemView.context, GravityActivity::class.java)
                9 -> intent = Intent(holder.itemView.context, LightMeasureActivity::class.java)
                10 -> intent = Intent(holder.itemView.context, GyroScopeActivity::class.java)
                11 -> intent = Intent(holder.itemView.context, CompassActivity::class.java)
                12 -> intent = Intent(holder.itemView.context, CalculatoreActivity::class.java)
                13 -> intent = Intent(holder.itemView.context, FlashLightActivity::class.java)
                14 -> intent = Intent(holder.itemView.context, DeviceInfoActivity::class.java)
                15 -> intent = Intent(holder.itemView.context, CPUInfoActivity::class.java)
                16 -> intent = Intent(holder.itemView.context, BatteryInfo::class.java)
                17 -> intent = Intent(holder.itemView.context, AvailableSensors::class.java)
                18 -> intent = Intent(holder.itemView.context, SOSFlashLightActivity::class.java)
                19 -> intent = Intent(holder.itemView.context, RamUseage::class.java)

            }

            intent?.putExtra("book", list[position].title)
            holder.itemView.context.startActivity(intent)
        }
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.image)
        var itemTitle: TextView = itemView.findViewById(R.id.tv)
    }
}