package com.musttool.ui.activities

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.an.deviceinfo.location.LocationInfo
import com.musttool.R
import com.musttool.utils.Utils

class UserLocation : AppCompatActivity() {
    val LOCATION_PERMISSION_REQUEST_CODE = 123
    lateinit var cityTv:TextView
    lateinit var latitudeTv:TextView
    lateinit var longitudeTv:TextView
    lateinit var countryCodeTv:TextView
    lateinit var stateTv:TextView
    lateinit var addressLine1Tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_location)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        cityTv=findViewById(R.id.cityTv)
        latitudeTv=findViewById(R.id.latitudeTv)
        longitudeTv=findViewById(R.id.longitudeTv)
        countryCodeTv=findViewById(R.id.countryCodeTv)
        stateTv=findViewById(R.id.stateTv)
        addressLine1Tv=findViewById(R.id.addressLine1Tv)

        // user location
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationInfo = LocationInfo(this)
            val location = locationInfo.location

            val city = location.city
            val latitude = location.latitude
            val longitude = location.longitude
            val countryCode = location.countryCode
            val state = location.state
            val addressLine1 = location.addressLine1


            cityTv.text=city.toString()
            latitudeTv.text=latitude.toString()
            longitudeTv.text=longitude.toString()
            countryCodeTv.text=countryCode.toString()
            stateTv.text=state.toString()
            addressLine1Tv.text=addressLine1.toString()

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val locationInfo = LocationInfo(this)
                val location = locationInfo.location

                val city = location.city
                val latitude = location.latitude
                val longitude = location.longitude
                val countryCode = location.countryCode
                val addressLine1 = location.addressLine1
                val state = location.state

                cityTv.text=city.toString()
                latitudeTv.text=latitude.toString()
                longitudeTv.text=longitude.toString()
                countryCodeTv.text=countryCode.toString()
                stateTv.text=state.toString()
                addressLine1Tv.text=addressLine1.toString()

            } else {
            }
        }
    }

}