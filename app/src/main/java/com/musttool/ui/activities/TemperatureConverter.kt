package com.musttool.ui.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import android.app.Activity
import android.provider.MediaStore.Audio.Radio
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.musttool.R
import com.musttool.ui.viewmodels.TemperatureConverterViewModel
import com.musttool.utils.Utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemperatureConverter : AppCompatActivity() {

    lateinit var typeSpin: Spinner
    val types = arrayOf("Celsius", "Kelvin", "Fahrenheit")
    val temperatureConverterViewModel:TemperatureConverterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_converter)
        statusBarColor(this,R.color.myColor)

        val convert = findViewById<View>(R.id.convert) as Button
        typeSpin = findViewById<View>(R.id.type) as Spinner

        val typeAdapter = ArrayAdapter(this, R.layout.spinner_items_layout, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        typeSpin!!.setAdapter(typeAdapter)

        convert.setOnClickListener {
            view: View? -> convertFunc()
        }

    }

    private fun convertFunc() {
        val temperatureEdt = findViewById<EditText>(R.id.temperature)
        val temperature = temperatureEdt.text.toString().trim()
        var tempNo = temperature.toDouble()
        val format = typeSpin.selectedItem.toString().trim()
        val answer1 = findViewById<View>(R.id.answer1) as TextView
        val answer2 = findViewById<View>(R.id.answer2) as TextView
        val ans1 = findViewById<View>(R.id.ans1) as TextView
        val ans2 = findViewById<View>(R.id.ans2) as TextView

        if (!temperature.isEmpty()) {
            if (format == "Celsius") {
                val celsiusValue = tempNo

                temperatureConverterViewModel.apply {
                    forCelsiusTemperature(celsiusValue)

                    temName1.observe(this@TemperatureConverter) { it ->
                        ans1.text = it
                    }
                    temName2.observe(this@TemperatureConverter) { it ->
                        ans2.text = it
                    }

                    temperature1.observe(this@TemperatureConverter) { it ->
                        answer1.text = it
                    }
                    temperature2.observe(this@TemperatureConverter) { it ->
                        answer2.text = it
                    }
                }

            } else if (format == "Kelvin") {
                val kelvinValue = tempNo
                temperatureConverterViewModel.apply {
                    forKelvinTemperature(kelvinValue)

                    temName1.observe(this@TemperatureConverter) { it ->
                        ans1.text = it
                    }
                    temName2.observe(this@TemperatureConverter) { it ->
                        ans2.text = it
                    }

                    temperature1.observe(this@TemperatureConverter) { it ->
                        answer1.text = it
                    }
                    temperature2.observe(this@TemperatureConverter) { it ->
                        answer2.text = it
                    }
                }

            } else if (format == "Fahrenheit") {
                val fahrenheitValue = tempNo
                temperatureConverterViewModel.apply {
                    forFahrenheitTemperature(fahrenheitValue)

                    temName1.observe(this@TemperatureConverter) { it ->
                        ans1.text = it
                    }
                    temName2.observe(this@TemperatureConverter) { it ->
                        ans2.text = it
                    }

                    temperature1.observe(this@TemperatureConverter) { it ->
                        answer1.text = it
                    }
                    temperature2.observe(this@TemperatureConverter) { it ->
                        answer2.text = it
                    }
                }

            }
        }

    }
}