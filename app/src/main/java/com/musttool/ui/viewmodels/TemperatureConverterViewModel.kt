package com.musttool.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TemperatureConverterViewModel @Inject constructor():ViewModel() {
    var temperature1:MutableLiveData<String> = MutableLiveData()
    var temperature2:MutableLiveData<String> = MutableLiveData()

    var temName1:MutableLiveData<String> =  MutableLiveData()
    var temName2:MutableLiveData<String> =  MutableLiveData()

    fun forCelsiusTemperature(celsiusValue:Double=0.0){
        val kelvinValue = celsiusValue + 273.15
        val fahrenheitValue = (celsiusValue * 9/5) + 32

        temperature1.value=String.format("%.3f", kelvinValue)
        temperature2.value=String.format("%.3f", fahrenheitValue)

        temName1.value="Kelvin"
        temName2.value="Fahrenheit"

    }

    fun forKelvinTemperature(kelvinValue:Double){
        val celsiusValue = kelvinValue - 273.15
        val fahrenheitValue = (celsiusValue * 9/5) + 32

        temperature1.value=String.format("%.3f", celsiusValue)
        temperature2.value=String.format("%.3f", fahrenheitValue)

        temName1.value="Celsius"
        temName2.value="Fahrenheit"

    }


    fun forFahrenheitTemperature(fahrenheitValue:Double){
        val celsiusValue = (fahrenheitValue - 32) * 5/9
        val kelvinValue = celsiusValue + 273.15

        temperature1.value=String.format("%.3f", celsiusValue)
        temperature2.value=String.format("%.3f", kelvinValue)

        temName1.value="Celsius"
        temName2.value="Kelvin"

    }


}