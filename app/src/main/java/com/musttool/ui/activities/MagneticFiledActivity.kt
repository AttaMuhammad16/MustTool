package com.musttool.ui.activities

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.musttool.R
import com.musttool.ui.viewmodels.ChartViewViewModel
import com.musttool.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow

@AndroidEntryPoint
class MagneticFiledActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var magnetSensor: Sensor
    private lateinit var lineChart: LineChart
    private lateinit var magneticFieldValue: TextView
    private val entries = ArrayList<Entry>()
    val chartViewViewModel:ChartViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magnetic_filed)
        lineChart = findViewById(R.id.lineChart)
        magneticFieldValue = findViewById(R.id.magneticFieldValue)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)

        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }


        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!=null) {
            magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!!
            sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this@MagneticFiledActivity, "Magnetic Sensor not available.", Toast.LENGTH_LONG).show()
        }

        chartViewViewModel.data.observe(this){
            magneticFieldValue.text=it
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            val magnitude = Math.sqrt(event.values[0].toDouble().pow(2.0) + event.values[1].toDouble().pow(2.0) + event.values[2].toDouble().pow(2.0)).toFloat()
            entries.add(Entry(entries.size.toFloat(), magnitude))
            chartViewViewModel.updateLineChart(magnitude.toString(),lineChart,entries,"Magnetic Field","Magnetic Field chart",Color.RED)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

}