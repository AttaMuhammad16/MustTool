package com.musttool.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.an.deviceinfo.device.model.Battery
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.musttool.R
import com.musttool.utils.Utils

class BatteryInfo : AppCompatActivity() {
    lateinit var batteryPercentage:TextView
    lateinit var batteryH:TextView
    lateinit var technology:TextView
    lateinit var voltage:TextView
    lateinit var temperature:TextView
    lateinit var batteryPresent:TextView
    lateinit var chargeSource:TextView
    lateinit var charging:TextView
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery_info)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        batteryPercentage=findViewById(R.id.batteryPercentage)
        batteryH=findViewById(R.id.batteryHealth)
        technology=findViewById(R.id.technology)
        voltage=findViewById(R.id.voltage)
        temperature=findViewById(R.id.temperature)
        batteryPresent=findViewById(R.id.batteryPresent)
        chargeSource=findViewById(R.id.chargingSource)
        charging=findViewById(R.id.charging)

        // battery info
        val battery = Battery(this)
        var batteryPercent = battery.batteryPercent
        batteryPercentage.text="${"$batteryPercent %"}"

        var batteryHealth = battery.batteryHealth
        batteryH.text=batteryHealth.toString()

        var batteryTechnology = battery.batteryTechnology
        technology.text=batteryTechnology.toString()

        var batteryVoltage = battery.batteryVoltage
        voltage.text="$batteryVoltage v"

        var batteryTemperature = battery.batteryTemperature
        temperature.text="$batteryTemperature °C"

        var isBatteryPresent = battery.isBatteryPresent
        batteryPresent.text=isBatteryPresent.toString()

        var chargingSource = battery.chargingSource
        chargeSource.text=chargingSource.toString()

        var isPhoneCharging = battery.isPhoneCharging
        charging.text=isPhoneCharging.toString()



    }
}