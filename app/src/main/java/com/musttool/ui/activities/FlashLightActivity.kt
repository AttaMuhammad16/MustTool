package com.musttool.ui.activities

import android.annotation.SuppressLint
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.musttool.R

class FlashLightActivity : AppCompatActivity() {
    lateinit var constraint:ConstraintLayout
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_light)
        window.statusBarColor=ContextCompat.getColor(this@FlashLightActivity,R.color.black)

        val onButton = findViewById<Button>(R.id.flash_on_btn)
        val offButton = findViewById<Button>(R.id.flash_off_btn)
        constraint= findViewById(R.id.constraint)

        onButton.setOnClickListener {
            onFlash()
//            constraint.setBackgroundResource(R.drawable.light_on)
        }

        offButton.setOnClickListener {
            offFlash()
//            constraint.setBackgroundResource(R.drawable.light_of)
        }

    }


    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onFlash(){
        var cameraManager : CameraManager? = null
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

        try{
            var cameraId : String? = null
            cameraId = cameraManager.cameraIdList[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager!!.setTorchMode(cameraId,true)
                Toast.makeText(this, "Flash on",Toast.LENGTH_SHORT).show()
            }

        }catch (e: CameraAccessException){
            Toast.makeText(this@FlashLightActivity, "Something wrong", Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun offFlash(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val cameraManage = getSystemService(CAMERA_SERVICE) as CameraManager

            try {
                val cameraId = cameraManage.cameraIdList[0]
                cameraManage.setTorchMode(cameraId,false)
                Toast.makeText(this, "Flash of",Toast.LENGTH_SHORT).show()
            }catch (e: CameraAccessException){

            }
        }
    }
}