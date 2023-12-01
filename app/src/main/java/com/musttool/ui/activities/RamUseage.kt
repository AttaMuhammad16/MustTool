package com.musttool.ui.activities

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StatFs
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.an.deviceinfo.device.model.Memory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.musttool.R
import com.musttool.utils.Utils
import com.skydoves.progressview.OnProgressChangeListener
import com.skydoves.progressview.OnProgressClickListener
import com.skydoves.progressview.ProgressView
import com.skydoves.progressview.progressView


class RamUseage : AppCompatActivity() {
    private lateinit var ramChart: LineChart
    private val handler = Handler()
    private val entries = ArrayList<Entry>()
    lateinit var progressView:ProgressView
    lateinit var progress2:ProgressView
    lateinit var ramUsageValue:TextView
    lateinit var memoryValue:TextView
    lateinit var remainingMemoryTv:TextView
    lateinit var usedMemoryTv:TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ram_useage)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        ramChart = findViewById(R.id.ramChart)
        progressView = findViewById(R.id.progressView)
        progress2 = findViewById(R.id.progress2)
        ramUsageValue = findViewById(R.id.ramUsageValue)
        memoryValue = findViewById(R.id.memoryValue)
        remainingMemoryTv = findViewById(R.id.remainingMemoryTv)
        usedMemoryTv = findViewById(R.id.usedMemoryTv)
        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }



        handler.postDelayed(object : Runnable {
            override fun run() {
                updateRAMUsage()
                handler.postDelayed(this, 500)
            }
        }, 1000)

        val memory = Memory(this)
        val totalRAMBytes = memory.totalRAM
        val totalInternalMemorySizeBytes = memory.totalInternalMemorySize
        val totalExternalMemorySizeBytes = memory.totalExternalMemorySize
        val isHasExternalSDCard = memory.isHasExternalSDCard

        val totalRAMGB = totalRAMBytes / (1024.0 * 1024 * 1024)
        val availableInternalMemorySizeBytes = memory.availableInternalMemorySize
        val totalInternalMemorySizeGB = totalInternalMemorySizeBytes / (1024.0 * 1024 * 1024)
        val totalExternalMemorySizeGB = totalExternalMemorySizeBytes / (1024.0 * 1024 * 1024)
        val availableInternalMemorySizeGB = availableInternalMemorySizeBytes / (1024.0 * 1024 * 1024)


        var readAbleRam=String.format("%.2f",totalRAMGB)
        progressView.labelText="Total Ram: $readAbleRam GB"
        progress2.labelText="Ram Usage"

        var readableMemory=String.format("%.2f",totalInternalMemorySizeGB)
        memoryValue.text="$readableMemory GB"

        var readableRemainingMemory=String.format("%.2f",availableInternalMemorySizeGB)
        remainingMemoryTv.text="$readableRemainingMemory GB"

        updateStorageUsage()


    }

    @SuppressLint("SetTextI18n")
    private fun updateRAMUsage() {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val usedMemory = (memoryInfo.totalMem - memoryInfo.availMem) / (1024 * 1024)
        entries.add(Entry(entries.size.toFloat(), usedMemory.toFloat()))
        updateChart(usedMemory.toFloat())
        ramUsageValue.text="$usedMemory MB"
    }

    private fun updateChart(usedMemory: Float) {
        val dataSet = LineDataSet(entries, "$usedMemory MB")
        dataSet.color = Color.RED
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.valueTextSize = 12f

        val data = LineData(dataSet)
        ramChart.data = data
        ramChart.invalidate()
        val description = Description()
        description.text = "MB"
        ramChart.description = description

        ramChart.axisLeft.textColor = Color.WHITE
        ramChart.axisRight.textColor = Color.WHITE
        ramChart.xAxis.textColor = Color.WHITE
        ramChart.legend.textColor = Color.WHITE
        ramChart.description.textColor = Color.WHITE

        ramChart.xAxis.gridColor = Color.parseColor("#404040")
        ramChart.axisLeft.gridColor = Color.parseColor("#404040")
        ramChart.axisRight.gridColor = Color.parseColor("#404040")

    }


    @SuppressLint("SetTextI18n")
    private fun updateStorageUsage() {
        val stat = StatFs(Environment.getDataDirectory().path)
        val totalBytes = stat.totalBytes
        val availableBytes = stat.availableBytes
        val usedBytes = totalBytes - availableBytes

        val usedStorageGB = usedBytes / (1024.0 * 1024.0 * 1024.0)

        var m=String.format("%.2f",usedStorageGB)
        usedMemoryTv.text = "$m GB"

    }



//    private fun updateStorageUsage() {
//        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val memoryInfo = Debug.MemoryInfo()
//        Debug.getMemoryInfo(memoryInfo)
//        val usedMemoryMB = memoryInfo.totalPss / 1024
//
//        // For now, we'll just log the used memory
//        Log.i("TAG", "Used RAM: $usedMemoryMB MB")
//
////        entries.add(Entry(entries.size.toFloat(), usedStorageGB.toFloat()))
////        updateChart(usedStorageGB.toFloat())
//
////        storageUsageValue.text = "%.2f GB".format(usedStorageGB)
//    }



}
