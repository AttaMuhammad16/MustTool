package com.musttool.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

import android.app.Activity
import android.provider.MediaStore.Audio.Radio
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.musttool.R

class TemperatureConverter : Activity() {

    lateinit var typeSpin: Spinner
    val types = arrayOf("Celsius", "Kelvin", "Fahrenheit")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature_converter)
        window.statusBarColor = ContextCompat.getColor(this, R.color.temperatureActivityColor)

        val convert = findViewById<View>(R.id.convert) as Button
        typeSpin = findViewById<View>(R.id.type) as Spinner

        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        typeSpin!!.setAdapter(typeAdapter)

        convert.setOnClickListener {
                view: View? -> convertFunc()
        }

    }

    private fun convertFunc() {

        val temperatureEdt = findViewById<EditText>(R.id.temperature)
        val temperature = temperatureEdt.text.toString().trim()
        val tempNo = temperature.toString().toFloat()?:0.0
        val format = typeSpin.selectedItem.toString().trim()
        val answer1 = findViewById<View>(R.id.answer1) as TextView
        val answer2 = findViewById<View>(R.id.answer2) as TextView
        val ans1 = findViewById<View>(R.id.ans1) as TextView
        val ans2 = findViewById<View>(R.id.ans2) as TextView

        if (!temperature.isEmpty()) {
            if (format == "Celsius") {

                ans1.text = "Fahrenheit"
                ans2.text = "Kelvin"

                val celsiusValue = tempNo.toDouble()
                val fahrenheitValue = (celsiusValue * 9/5) + 32
                val kelvinValue = celsiusValue + 273.15

                answer1.text = String.format("%.3f", fahrenheitValue)
                answer2.text = String.format("%.3f", kelvinValue)

            } else if (format == "Kelvin") {

                ans1.text = "Celsius"
                ans2.text = " Fahrenheit"

                val kelvinValue = tempNo.toDouble()
                val celsiusValue = kelvinValue - 273.15
                val fahrenheitValue = (celsiusValue * 9/5) + 32

                answer1.text = String.format("%.3f", celsiusValue)
                answer2.text = String.format("%.3f", fahrenheitValue)

            } else if (format == "Fahrenheit") {
                ans1.text = "Celsius"
                ans2.text = "Kelvin"
                val fahrenheitValue = tempNo.toDouble()
                val celsiusValue = (fahrenheitValue - 32) * 5/9
                val kelvinValue = celsiusValue + 273.15

                answer1.text = String.format("%.3f", celsiusValue)
                answer2.text = String.format("%.3f", kelvinValue)
            }
        }

    }
}