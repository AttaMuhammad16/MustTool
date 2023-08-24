package com.musttool.activities

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.pm.PackageManager
import android.hardware.SensorPrivacyManager.Sensors.CAMERA
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException
import android.content.Context
import android.content.Intent
import android.provider.Browser
import android.net.Uri
import android.util.Log
import android.widget.*
import com.musttool.R


class BarCodeScanner : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var surfaceView: SurfaceView
    private lateinit var tvBarcodeValue: TextView
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private lateinit var edt:EditText
    private lateinit var search_btn: Button
    private lateinit var copyBtn: Button
    private lateinit var shareBtn: Button
    private val REQUEST_CAMERA_PERMISSION = 201
    lateinit var uri:Uri
    var url1: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_code_scanner)

        window.statusBarColor = ContextCompat.getColor(this, R.color.myColor)


        surfaceView = findViewById(R.id.surface_view)
        tvBarcodeValue = findViewById(R.id.tv_barcode_value)
        edt = findViewById(R.id.edt)
        search_btn = findViewById(R.id.search_btn)
        shareBtn = findViewById(R.id.shareBtn)
        copyBtn = findViewById(R.id.copy)


        barcodeDetector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build()

        surfaceView.holder.addCallback(this)
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    tvBarcodeValue.post {
//                        tvBarcodeValue.text = barcodes.valueAt(0).displayValue
                        edt.setText(barcodes.valueAt(0).displayValue)
                        url1=barcodes.valueAt(0).displayValue.toString()
                    }
                }
            }
        })

        search_btn.setOnClickListener{
            var a=isValidURL(url1)
            if (a){
                openInChrome(url1)
            }else{
                Toast.makeText(this@BarCodeScanner, "not valid", Toast.LENGTH_SHORT).show()
            }
        }
        shareBtn.setOnClickListener {
            if (edt.text.toString().isNotEmpty()){
                shareText(edt)
            }else{
                Toast.makeText(this@BarCodeScanner, "Please Enter Your Text", Toast.LENGTH_SHORT).show()
            }
        }
        copyBtn.setOnClickListener {
            if (edt.text.toString().isNotEmpty()){
                var t=edt.text.toString()
                copyToClipboard(t)
                Toast.makeText(this@BarCodeScanner, "Text Copied", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@BarCodeScanner, "Not Valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(p0: SurfaceHolder) {
        cameraSource.stop()
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCameraSource()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    private fun startCameraSource() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            cameraSource.start(surfaceView.holder)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraSource()
            } else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun openInChrome(url: String) {
        val chromeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        chromeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        chromeIntent.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome")
        startActivity(chromeIntent)
    }

    fun isValidURL(url: String): Boolean {
        return try {
            uri = Uri.parse(url)
            uri.scheme != null && (uri.scheme == "http" || uri.scheme == "https")
        } catch (e: Exception) {
            false
        }
    }
    fun shareText(editText: EditText) {
        val text = editText.text.toString()
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, "Share Text"))
    }
    fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }
}
