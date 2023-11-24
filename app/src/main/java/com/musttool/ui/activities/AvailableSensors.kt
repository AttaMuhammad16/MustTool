package com.musttool.ui.activities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musttool.R
import com.musttool.adapters.AvailableSensorsAdapter

class AvailableSensors : AppCompatActivity() {
    lateinit var recycler:RecyclerView
    lateinit var list:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_available_sensors)

        window.statusBarColor = ContextCompat.getColor(this@AvailableSensors, R.color.color19)
        recycler=findViewById(R.id.recycler)
        list= ArrayList()

        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val availableSensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

        val sensorAdapter = AvailableSensorsAdapter(availableSensors,this)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = sensorAdapter

    }
}