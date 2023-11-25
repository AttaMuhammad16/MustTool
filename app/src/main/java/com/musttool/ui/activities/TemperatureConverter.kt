package com.musttool.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.musttool.R
import com.musttool.ui.viewmodels.TemperatureConverterViewModel
import com.musttool.utils.Utils
import com.musttool.utils.Utils.myToast
import com.musttool.utils.Utils.preventShowingKeyboard
import com.musttool.utils.Utils.statusBarColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TemperatureConverter : AppCompatActivity() {

    lateinit var typeSpin: Spinner
    val types = arrayOf("Celsius", "Kelvin", "Fahrenheit")
    val temperatureConverterViewModel: TemperatureConverterViewModel by viewModels()
    lateinit var temperatureEdt: EditText
    lateinit var backArrowImg: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_converter)
        statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        temperatureEdt = findViewById(R.id.temperature)
        temperatureEdt.requestFocus()

        val convert = findViewById<View>(R.id.convert) as Button
        typeSpin = findViewById<View>(R.id.type) as Spinner
        typeSpin.background.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        backArrowImg = findViewById(R.id.backArrowImg)

        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }


        val typeAdapter = ArrayAdapter(this, R.layout.spinner_items_layout, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        typeSpin!!.setAdapter(typeAdapter)

        convert.setOnClickListener { view: View? ->
            convertFunc()
        }

    }

    private fun convertFunc() {
        var tempNo = 0.0
        val temperature = temperatureEdt.text.toString().trim()
        if (temperature.isNotEmpty()) {
            tempNo = temperature.toDouble()
        } else {
            myToast(this@TemperatureConverter, "Please Enter Your Value", Toast.LENGTH_SHORT)
        }

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