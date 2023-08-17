package com.musttool.activities

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
import kotlin.math.pow

class MagneticFiledActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var magnetSensor: Sensor
    private lateinit var lineChart: LineChart
    private lateinit var magneticFieldValue: TextView
    private val entries = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_magnetic_filed)
        lineChart = findViewById(R.id.lineChart)
        magneticFieldValue = findViewById(R.id.magneticFieldValue)
        window.statusBarColor = ContextCompat.getColor(this, R.color.myColor)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (magnetSensor != null) {
            sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this@MagneticFiledActivity, "Magnet Sensor not available.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            val magnitude = Math.sqrt(
                event.values[0].toDouble().pow(2.0) +
                        event.values[1].toDouble().pow(2.0) +
                        event.values[2].toDouble().pow(2.0)
            ).toFloat()

            // Add new data entry to the chart
            entries.add(Entry(entries.size.toFloat(), magnitude))

            // Update the line chart
            updateLineChart(magnitude.toString())
            magneticFieldValue.text=magnitude.toString()
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }


    private fun updateLineChart(magnitude:String) {
        val dataSet = LineDataSet(entries, "$magnitude Magnetic Field (μT)") //
        dataSet.color = Color.RED // color
        dataSet.setDrawCircles(false) // color
        dataSet.setDrawValues(true)
        dataSet.valueTextSize = 12f

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.invalidate()

        val description = Description()
        description.text = "Magnetic Field "
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