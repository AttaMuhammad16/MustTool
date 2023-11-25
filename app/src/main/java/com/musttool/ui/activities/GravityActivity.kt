package com.musttool.ui.activities

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.musttool.R
import com.musttool.utils.Utils
import kotlin.math.pow


class GravityActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var gravitySensor: Sensor
    private lateinit var lineChart: LineChart
    private lateinit var gravityValue: TextView
    private val entries = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gravity)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        lineChart = findViewById(R.id.lineChart)
        gravityValue = findViewById(R.id.gravityValue)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!=null) {
            gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)!!
            sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this@GravityActivity, "Gravity Sensor not available.", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            val gravityX = event.values[0]
            val gravityY = event.values[1]
            val gravityZ = event.values[2]

            val gravityMagnitude = Math.sqrt(gravityX.toDouble().pow(2.0) + gravityY.toDouble().pow(2.0) + gravityZ.toDouble().pow(2.0)).toFloat()

            entries.add(Entry(entries.size.toFloat(), gravityMagnitude))

            updateLineChart(gravityMagnitude.toString())
            gravityValue.text=gravityMagnitude.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No need to handle accuracy changes for step counter
    }

    private fun updateLineChart(gravityMagnitude: String) {
        val dataSet = LineDataSet(entries, "$gravityMagnitude m/s²")
        dataSet.color = Color.MAGENTA
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.valueTextSize = 12f

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.invalidate()

        val description = Description()
        description.text = "Gravity"
        lineChart.description = description

        // Customize appearance
        lineChart.axisLeft.textColor = Color.WHITE // Y-axis label color
        lineChart.axisRight.textColor = Color.WHITE // Y-axis label color
        lineChart.xAxis.textColor = Color.WHITE // X-axis label color
        lineChart.legend.textColor = Color.WHITE // Legend color
        lineChart.description.textColor = Color.WHITE // Description color

        // Customize grid lines
        lineChart.xAxis.gridColor = Color.parseColor("#404040") // Custom grid line color
        lineChart.axisLeft.gridColor = Color.parseColor("#404040") // Custom grid line color
        lineChart.axisRight.gridColor = Color.parseColor("#404040") // Custom grid line color

    }

}