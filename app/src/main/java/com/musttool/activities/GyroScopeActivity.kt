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

class GyroScopeActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var gyroSensor: Sensor
    private lateinit var lineChart: LineChart
    private lateinit var gyroValue: TextView
    private val entries = ArrayList<Entry>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gyro_scope)
        lineChart = findViewById(R.id.lineChart)
        gyroValue = findViewById(R.id.gyroValue)

        window.statusBarColor = ContextCompat.getColor(this, R.color.gyro)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {

            gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)

        } else {
            Toast.makeText(this@GyroScopeActivity, "Gyroscope Sensor not available.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            val gyroX = event.values[0]
            val gyroY = event.values[1]
            val gyroZ = event.values[2]

            // Calculate the magnitude of the gyroscope vector
            val gyroMagnitude = Math.sqrt(
                gyroX.toDouble().pow(2.0) +
                        gyroY.toDouble().pow(2.0) +
                        gyroZ.toDouble().pow(2.0)
            ).toFloat()

            // Add new data entry to the chart
            entries.add(Entry(entries.size.toFloat(), gyroMagnitude))

            // Update the line chart
            updateLineChart(gyroMagnitude.toString())
            gyroValue.text=gyroMagnitude.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No need to handle accuracy changes for gyroscope
    }

    private fun updateLineChart(gyroMagnitude: String) {
        val dataSet = LineDataSet(entries, "$gyroMagnitude rad/s")
        dataSet.color = Color.GREEN
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.valueTextSize = 12f

        val data = LineData(dataSet)
        lineChart.data = data
        lineChart.invalidate()

        val description = Description()
        description.text = "Gyroscope Data"
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