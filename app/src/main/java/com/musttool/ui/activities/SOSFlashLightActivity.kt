package com.musttool.ui.activities

import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.musttool.R
import com.musttool.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SOSFlashLightActivity : AppCompatActivity() {
    lateinit var sosBtn: Button

    lateinit var dotBtn: Button
    lateinit var dashBtn: Button
    lateinit var spaceBtn: Button
    lateinit var edt: EditText
    lateinit var morseCodeTV: TextView

    private lateinit var cameraManager: CameraManager

    private val dot = 200L
    private val dash = 500L
    private val space = 100L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sosflash_light)
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        sosBtn = findViewById(R.id.sosBtn)
        dotBtn = findViewById(R.id.dotBtn)
        dashBtn = findViewById(R.id.dashBtn)
        spaceBtn = findViewById(R.id.spaceBtn)
        edt = findViewById(R.id.edt)
        morseCodeTV = findViewById(R.id.morseCodeTV)
        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)

        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }



        dotBtn.setOnClickListener {
            var d= longArrayOf(200L)
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, true)
                    flashPattern(d, cameraId)
                    cameraManager.setTorchMode(cameraId, false)
                } catch (e: CameraAccessException) {
                    Toast.makeText(this@SOSFlashLightActivity, "Something wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dashBtn.setOnClickListener {
            var das= longArrayOf(500L)
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, true)
                    flashPattern(das, cameraId)
                    cameraManager.setTorchMode(cameraId, false)
                } catch (e: CameraAccessException) {
                    Toast.makeText(this@SOSFlashLightActivity, "Something wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        spaceBtn.setOnClickListener {
            var s= longArrayOf(100L)
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    cameraManager.setTorchMode(cameraId, true)
                    flashPattern(s,cameraId)
                    cameraManager.setTorchMode(cameraId, false)
                } catch (e: CameraAccessException) {
                    Toast.makeText(this@SOSFlashLightActivity, "Something wrong.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        sosBtn.setOnClickListener {
            if (edt.text.isEmpty()){
                edt.error="Enter Your Message."
                Toast.makeText(this@SOSFlashLightActivity, "Enter Your Message.", Toast.LENGTH_SHORT).show()
            }else{
                var message=edt.text.toString()
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    try {
                        val cameraId = cameraManager.cameraIdList[0]
                        cameraManager.setTorchMode(cameraId, true)
                        val inputText = message
                        val pattern = generatePattern(inputText)
                        CoroutineScope(Dispatchers.IO).launch {
                            updateMorseCodeTextView(pattern)
                        }
                        flashPattern(pattern, cameraId)
                        cameraManager.setTorchMode(cameraId, false)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    private suspend fun updateMorseCodeTextView(pattern: LongArray) {
        val morseCodeString = StringBuilder()
        for (pulse in pattern) {
            if (pulse == dot) {
                morseCodeString.append(".")
            } else if (pulse == dash) {
                morseCodeString.append("-")
            } else if (pulse == space) {
                morseCodeString.append(" ")
            }
        }
        withContext(Dispatchers.Main){
            morseCodeTV.text = "Morse Code:  $morseCodeString"
            Log.i("TAG", "updateMorseCodeTextView:$morseCodeString")
        }

    }

    private fun generatePattern(text: String): LongArray {
        val pattern = ArrayList<Long>()
        val dot = 200L
        val dash = 500L
        val space = 100L
        for (char in text.toUpperCase()) {
            when (char) {

                '.' -> pattern.addAll(listOf(dot,dash)) // .-
                '-' -> pattern.addAll(listOf(dash,dot,dot,dot)) // -...
                'A' -> pattern.addAll(listOf(dot,dash)) // .-
                'B' -> pattern.addAll(listOf(dash, dot, dot, dot)) // -...
                'C' -> pattern.addAll(listOf(dash, dot, dash, dot)) // -.-.
                'D' -> pattern.addAll(listOf(dash, dot, dot)) // -..
                'E' -> pattern.addAll(listOf(dot)) // .
                'F' -> pattern.addAll(listOf(dot, dot, dash, dot)) // ..-.
                'G' -> pattern.addAll(listOf(dash, dash, dot)) // --.
                'H' -> pattern.addAll(listOf(dot, dot, dot, dot)) // ....
                'I' -> pattern.addAll(listOf(dot, dot)) // ..
                'J' -> pattern.addAll(listOf(dot, dash, dash, dash)) // .---
                'K' -> pattern.addAll(listOf(dash, dot, dash)) // -.-
                'L' -> pattern.addAll(listOf(dot, dash, dot, dot)) // .-..
                'M' -> pattern.addAll(listOf(dash, dash)) // --
                'N' -> pattern.addAll(listOf(dash, dot)) // -.
                'O' -> pattern.addAll(listOf(dash, dash, dash)) // ---
                'P' -> pattern.addAll(listOf(dot, dash, dash, dot)) // .--.
                'Q' -> pattern.addAll(listOf(dash, dash, dot, dash)) // --.-
                'R' -> pattern.addAll(listOf(dot, dash, dot)) // .-.
                'S' -> pattern.addAll(listOf(dot, dot,dot)) // ...
                'T' -> pattern.addAll(listOf(dash)) // -
                'U' -> pattern.addAll(listOf(dot, dot, dash)) // ..-
                'V' -> pattern.addAll(listOf(dot, dot, dot, dash)) // ...-
                'W' -> pattern.addAll(listOf(dot, dash, dash)) // .--
                'X' -> pattern.addAll(listOf(dash, dot,  dot, dash)) // -..-
                'Y' -> pattern.addAll(listOf(dash, dot, dash, dash)) // -.--
                'Z' -> pattern.addAll(listOf(dash, dash, dot, dot)) // --..

                '1' -> pattern.addAll(listOf(dot,  dash, dash, dash, dash)) // .----
                '2' -> pattern.addAll(listOf(dot,  dot, dash, dash, dash)) // ..---
                '3' -> pattern.addAll(listOf(dot,  dot, dot, dash, dash)) // ...--
                '4' -> pattern.addAll(listOf(dot,  dot, dot, dot, dash)) // ....-
                '5' -> pattern.addAll(listOf(dot,  dot, dot, dot, dot)) // .....
                '6' -> pattern.addAll(listOf(dash, dot, dot, dot, dot)) // -....
                '7' -> pattern.addAll(listOf(dash, dash, dot, dot, dot)) // --...
                '8' -> pattern.addAll(listOf(dash, dash, dash, dot, dot)) // ---..
                '9' -> pattern.addAll(listOf(dash, dash, dash, dash, dot)) // ----.
                '0' -> pattern.addAll(listOf(dash, dash, dash, dash, dash)) // -----


                // Special characters
                ',' -> pattern.addAll(listOf(dash, dash, dot, dot, dash, dash)) // --..--
                '?' -> pattern.addAll(listOf(dot, dot, dash, dash, dot, dot)) // ..--..
                '\'' -> pattern.addAll(listOf(dot, dash, dash, dash, dash, dot)) // .----.
                '!' -> pattern.addAll(listOf(dash, dot, dash, dot, dash, dash)) // -.-.--
                '/' -> pattern.addAll(listOf(dash, dot, dot, dash, dot)) // -..-.
                '(' -> pattern.addAll(listOf(dash, dot, dash, dash, dot)) // -.--.
                ')' -> pattern.addAll(listOf(dash, dot, dash, dash, dot, dash)) // -.--.-
                '&' -> pattern.addAll(listOf(dot, dash, dot, dot, dot)) // .-...
                ':' -> pattern.addAll(listOf(dash, dash, dash, dot, dot, dot)) // ---...
                ';' -> pattern.addAll(listOf(dash, dot, dash, dot, dash, dot)) // -.-.-.
                '=' -> pattern.addAll(listOf(dash, dot, dot, dot, dash)) // -...-
                '+' -> pattern.addAll(listOf(dot, dash, dot, dash, dot)) // .-.-.
                '-' -> pattern.addAll(listOf(dash, dot, dot, dot, dot, dash)) // -....-
                '_' -> pattern.addAll(listOf(dot, dot, dash, dash, dot, dash)) // ..--.-
                '"' -> pattern.addAll(listOf(dot, dash, dot, dot, dash, dot)) // .-..-.
                '@' -> pattern.addAll(listOf(dot, dash, dash, dot, dash, dot)) // .--.-.

                ' ' -> pattern.add(space)
            }
        }
        return pattern.toLongArray()
    }

    private fun flashPattern(pattern: LongArray, cameraId: String) {
        Thread {
            try {
                for (pulse in pattern) {
                    cameraManager.setTorchMode(cameraId, true)
                    Thread.sleep(pulse)
                    cameraManager.setTorchMode(cameraId, false)
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }

}
