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

class LightMeasureActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var lightSensor: Sensor
    private lateinit var lineChart: LineChart
    private lateinit var lightValue: TextView
    private val entries = ArrayList<Entry>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_measure)
        lineChart = findViewById(R.id.lineChart)
        lightValue = findViewById(R.id.lightValue)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!=null) {
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)!!
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this@LightMeasureActivity, "Light Sensor not available.", Toast.LENGTH_LONG).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val lightIntensity = event.values[0]

            // Add new data entry to the chart
            entries.add(Entry(entries.size.toFloat(), lightIntensity))

            // Update the line chart
            updateLineChart(lightIntensity.toString())
            lightValue.text=lightIntensity.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No need to handle accuracy changes for light sensor
    }

    private fun updateLineChart(lightIntensity: String) {
        val dataSet = LineDataSet(entries, "$lightIntensity lux")
        dataSet.color = Color.YELLOW
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.valueTextSize = 12f

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.invalidate()

        val description = Description()
        description.text = "Light Intensity"
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