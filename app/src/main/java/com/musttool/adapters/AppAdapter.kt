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
import com.musttool.models.AppModel
import com.musttool.ui.activities.Acceleration
import com.musttool.ui.activities.AvailableSensors
import com.musttool.ui.activities.BarCodeScanner
import com.musttool.ui.activities.BatteryInfo
import com.musttool.ui.activities.CPUInfoActivity
import com.musttool.ui.activities.CalculatoreActivity
import com.musttool.ui.activities.CompassActivity
import com.musttool.ui.activities.DeviceInfoActivity
import com.musttool.ui.activities.FlashLightActivity
import com.musttool.ui.activities.GenQRActivity
import com.musttool.ui.activities.GravityActivity
import com.musttool.ui.activities.GyroScopeActivity
import com.musttool.ui.activities.LanguageTranslateActivity
import com.musttool.ui.activities.LightMeasureActivity
import com.musttool.ui.activities.MagneticFiledActivity
import com.musttool.ui.activities.NotesApp
import com.musttool.ui.activities.RamUseage
import com.musttool.ui.activities.SOSFlashLightActivity
import com.musttool.ui.activities.TemperatureConverter
import com.musttool.ui.activities.TextExtracter
import com.musttool.ui.activities.UserLocation
import com.musttool.ui.activities.WhatsAppActivity
import com.musttool.utils.AnimationUtils
import com.musttool.utils.IntentUtils
import kotlin.random.nextInt

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

            val intent = IntentUtils.getIntent(context,position)
            intent?.putExtra("book", list[position].title)
            holder.itemView.context.startActivity(intent)

            var randomValue = kotlin.random.Random.nextInt(0..10)
            AnimationUtils(context).getRandomAnimation(randomValue)

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.image)
        var itemTitle: TextView = itemView.findViewById(R.id.tv)
    }
}