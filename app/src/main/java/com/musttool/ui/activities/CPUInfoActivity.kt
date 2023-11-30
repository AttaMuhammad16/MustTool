package com.musttool.ui.activities

import android.content.Context
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.opengl.GLES20
import android.opengl.GLES30
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.musttool.R
import com.musttool.utils.Utils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.Arrays
import java.util.Timer
import java.util.TimerTask

class CPUInfoActivity : AppCompatActivity() {
    private lateinit var cpuFrequencyUpdater: CpuFrequencyUpdater
    lateinit var textViewCore0: TextView
    lateinit var textViewCore1: TextView
    lateinit var textViewCore2: TextView
    lateinit var textViewCore3: TextView
    lateinit var textViewCore4: TextView
    lateinit var textViewCore5: TextView
    lateinit var textViewCore6: TextView
    lateinit var textViewCore7: TextView
    lateinit var lineChart: LineChart
    lateinit var numberOfCores: TextView
    lateinit var processerTV: TextView
    lateinit var capuAbi: TextView
    lateinit var supportAbi: TextView
    lateinit var supportAbi32: TextView
    lateinit var support64Abi: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpuinfo)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        textViewCore0=findViewById(R.id.textViewCore0)
        textViewCore1=findViewById(R.id.textViewCore1)
        textViewCore2=findViewById(R.id.textViewCore2)
        textViewCore3=findViewById(R.id.textViewCore3)
        textViewCore4=findViewById(R.id.textViewCore4)
        textViewCore5=findViewById(R.id.textViewCore5)
        textViewCore6=findViewById(R.id.textViewCore6)
        textViewCore7=findViewById(R.id.textViewCore7)
        numberOfCores=findViewById(R.id.numberOfCores)
        processerTV=findViewById(R.id.processerTV)
        capuAbi=findViewById(R.id.capuAbi)
        supportAbi=findViewById(R.id.supportAbi)
        supportAbi32=findViewById(R.id.supportAbi32)
        support64Abi=findViewById(R.id.support64Abi)

        lineChart=findViewById(R.id.lineChart)

        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }

        val textViews = listOf(textViewCore0, textViewCore1,textViewCore2,textViewCore3,textViewCore4,textViewCore5,textViewCore6,textViewCore7)
        cpuFrequencyUpdater = CpuFrequencyUpdater {
            runOnUiThread {
                cpuFrequencyUpdater.updateTextViews() // Call the updateTextViews() function here
                cpuFrequencyUpdater.updateLineChart()

            }
        }

        cpuFrequencyUpdater.setTextViews(textViews)
        numberOfCores.text=numberOfCpuCores().toString()

        executeShellCommand("cat /proc/cpuinfo")

        capuAbi.text=(Build.CPU_ABI).toString()


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


        lineChart.xAxis.textColor = Color.WHITE
        lineChart.legend.textColor = Color.WHITE
        lineChart.setBorderColor(Color.WHITE)



        val supportedAbis = Arrays.toString(Build.SUPPORTED_ABIS)
        supportAbi.text = supportedAbis.substring(1, supportedAbis.length - 1) // Remove square brackets

        var supportedAbis32Bit=Arrays.toString(Build.SUPPORTED_32_BIT_ABIS)
        supportAbi32.text=supportedAbis32Bit.substring(1,supportedAbis32Bit.length-1)

        var supported64Bit=Arrays.toString(Build.SUPPORTED_64_BIT_ABIS)
        support64Abi.text=supported64Bit.substring(1,supported64Bit.length-1)


    }

    override fun onDestroy() {
        super.onDestroy()
        cpuFrequencyUpdater.stop()
    }

    private inner class CpuFrequencyUpdater(private val onUpdate: () -> Unit) {
        private val handler = Handler(Looper.getMainLooper())
        private val frequencyData = mutableMapOf<String, Long>()
        private val textViews = mutableListOf<TextView>()
        private val timer = Timer()

        init {
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateCpuFrequencyData()
                    handler.post { onUpdate() } // Move this line here
                }
            }, 0, 500)
        }

        fun setTextViews(textViews: List<TextView>) {
            this.textViews.addAll(textViews)
        }

        private fun updateCpuFrequencyData() {
            val numberOfCpuCores = getNumberOfCpuCores()
            for (coreIndex in 0 until numberOfCpuCores) {
                val cpuFrequency = getCpuFrequency(coreIndex)
                frequencyData["CPU $coreIndex"] = cpuFrequency
            }
        }

        fun stop() {
            timer.cancel()
        }

        fun updateTextViews() {
            for ((index, textView) in textViews.withIndex()) {
                val coreText = "CPU $index"
                val frequency = frequencyData[coreText] ?: 0L
                val formattedFrequency = "$frequency Hz"
                textView.text = "$coreText: $formattedFrequency"
            }
        }

        fun updateLineChart() {
            val entries = mutableListOf<Entry>()

            for ((index, frequency) in frequencyData.entries.withIndex()) {
                entries.add(Entry(index.toFloat(), frequency.value.toFloat()))
            }

            val dataSet = LineDataSet(entries, "CPU Performance")
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS.toList())
            dataSet.valueTextSize = 10f
            dataSet.valueTextColor=Color.WHITE


            val dataSets = mutableListOf<ILineDataSet>()
            dataSets.add(dataSet)

            val lineData = LineData(dataSets)
            lineChart.data = lineData
            lineChart.invalidate()
        }
    }



    fun getNumberOfCpuCores(): Int { // cores
        val cpuInfoPattern = Regex("^cpu[0-9]+$")
        val dir = File("/sys/devices/system/cpu/")
        val coreFiles = dir.listFiles { file -> file.name.matches(cpuInfoPattern) }
//        Log.i("TAG", "getNumberOfCpuCores: ${coreFiles.size}")
        return coreFiles.size
    }


    fun getCpuFrequency(coreIndex: Int): Long {
        val path = "/sys/devices/system/cpu/cpu$coreIndex/cpufreq/scaling_cur_freq"
        try {
            val reader = FileReader(path)
            val bufferedReader = BufferedReader(reader)
            val line = bufferedReader.readLine()
            bufferedReader.close()
            reader.close()

            return line?.toLongOrNull() ?: 0L
        } catch (e: IOException) {
            e.printStackTrace()
            return 0L
        }
    }

    fun numberOfCpuCores(): Int { // cores
        val cpuInfoPattern = Regex("^cpu[0-9]+$")
        val dir = File("/sys/devices/system/cpu/")
        val coreFiles = dir.listFiles { file -> file.name.matches(cpuInfoPattern) }
        return coreFiles.size
    }


    fun executeShellCommand(command: String): String {
        val process = Runtime.getRuntime().exec(command)
        val inputStream = process.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream))
        val output = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            output.append(line).append("\n")
        }
        processerTV.text=output
        return output.toString()
    }

}