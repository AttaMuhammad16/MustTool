package com.musttool.ui.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChartViewViewModel @Inject constructor():ViewModel() {

    private var _data:MutableLiveData<String> = MutableLiveData()
    val data:LiveData<String> = _data

    fun updateLineChart(magnitude:String,lineChart:LineChart,entries:ArrayList<Entry>,chartName:String,chartDes:String,lineColor:Int) {
        viewModelScope.launch {
            _data.value=magnitude

            val dataSet = LineDataSet(entries, "$magnitude $chartName") //
            dataSet.color = lineColor // color
            dataSet.setDrawCircles(false) // color
            dataSet.setDrawValues(false)
            dataSet.valueTextSize = 12f

            val data = LineData(dataSet)
            lineChart.data = data
            lineChart.invalidate()

            val description = Description()
            description.text = chartDes
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
}