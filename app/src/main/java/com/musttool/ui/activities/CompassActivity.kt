package com.musttool.ui.activities

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.musttool.R
import com.musttool.utils.Utils
import io.github.derysudrajat.compassqibla.CompassQibla

class CompassActivity : AppCompatActivity() {

    private var currentNeedleDegree = 0f
    lateinit var tvDirection: TextView
    lateinit var ivCompass: ImageView
    lateinit var ivNeedle: ImageView
    private var currentCompassDegree = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)

        tvDirection=findViewById(R.id.tvDirection)
        ivCompass=findViewById(R.id.ivCompass)
        ivNeedle=findViewById(R.id.ivNeedle)

        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }


        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)!=null){
            val compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) // Deprecated but still used for compass
        }else{
            Toast.makeText(this@CompassActivity, "compass sensor not available", Toast.LENGTH_SHORT).show()
        }

        CompassQibla.Builder(this).onPermissionGranted { permission ->
            Toast.makeText(this, "PermissionGranted", Toast.LENGTH_SHORT).show()
        }.onPermissionDenied {
            Toast.makeText(this, "PermissionDenied", Toast.LENGTH_SHORT).show()
            requestLocationPermission()
        }.onDirectionChangeListener { qiblaDirection ->

            tvDirection.text = if (qiblaDirection.isFacingQibla) "You're Facing Qibla"
            else "${qiblaDirection.needleAngle.toInt()}°"

            val rotateCompass = RotateAnimation(currentCompassDegree, qiblaDirection.compassAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
                duration = 1000
            }
            currentCompassDegree = qiblaDirection.compassAngle

            ivCompass.startAnimation(rotateCompass)

            val frameCount = 60 // Number of frames for the animation
            val startDegree = currentNeedleDegree
            val endDegree = qiblaDirection.needleAngle
            val duration = 500 // Total duration of the animation in milliseconds
            val frameDuration = duration / frameCount // Duration of each frame
            val ivNeedle = findViewById<View>(R.id.ivNeedle) // Replace with your ImageView


            val rotateNeedle = RotateAnimation(
                startDegree, endDegree, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
            ).apply {
                interpolator = LinearInterpolator() // Linear interpolator for smooth rotation
                repeatCount = 0
                repeatMode = Animation.RESTART
                this.duration = frameDuration.toLong()
            }

            val animationSet = AnimationSet(true)
            animationSet.addAnimation(rotateNeedle)
            animationSet.fillAfter = true // Keep the final rotation state

            currentNeedleDegree = qiblaDirection.needleAngle
//            ivNeedle.startAnimation(animationSet)

        }.build()

    }
    private fun requestLocationPermission() {
        // Replace with your actual permission request logic
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION), 111)
    }


}