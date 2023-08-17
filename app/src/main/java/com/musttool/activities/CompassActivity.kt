package com.musttool.activities

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
import com.musttool.R
import io.github.derysudrajat.compassqibla.CompassQibla

class CompassActivity : AppCompatActivity() {
    private var currentNeedleDegree = 0f
    lateinit var tvDirection: TextView
    lateinit var tvLocation: TextView
    lateinit var ivCompass: ImageView
    lateinit var ivNeedle: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compass)
        tvDirection=findViewById(R.id.tvDirection)
        ivCompass=findViewById(R.id.ivCompass)
        ivNeedle=findViewById(R.id.ivNeedle)
        tvLocation=findViewById(R.id.tvLocation)



        CompassQibla.Builder(this).onPermissionGranted { permission ->
            Toast.makeText(this, "onPermissionGranted $permission", Toast.LENGTH_SHORT).show()
        }.onPermissionDenied {
            Toast.makeText(this, "onPermissionDenied", Toast.LENGTH_SHORT).show()
        }.onGetLocationAddress { address ->
            tvLocation.text = buildString {
                append(address.locality)
                append(", ")
                append(address.subAdminArea)
                append("\n")
                append(address.countryCode)
                append("\n")
                append(address.featureName)
                append("\n")
                append(address.latitude)
                append("\n")
                append(address.longitude)
            }
        }.onDirectionChangeListener { qiblaDirection ->
            tvDirection.text = if (qiblaDirection.isFacingQibla) "You're Facing Qibla"
            else "${qiblaDirection.needleAngle.toInt()}°"


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
            ivNeedle.startAnimation(animationSet)

        }.build()



    }
}