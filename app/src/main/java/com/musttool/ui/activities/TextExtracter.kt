package com.musttool.ui.activities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.musttool.R
import com.musttool.utils.Utils
import java.io.File
import java.io.FileOutputStream


class TextExtracter : AppCompatActivity() {
    lateinit var imageView:ImageView
    lateinit var extractedText:TextView
    lateinit var captureBtn:Button
    lateinit var extractBtn:Button
    lateinit var clear:Button
    lateinit var pickImage:Button
    lateinit var copyBtn:Button
    var recognizer=TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private var bitmap:Bitmap?=null
    lateinit var lottieAnim:LottieAnimationView
    lateinit var scanningBar:LottieAnimationView
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.musttool.R.layout.activity_text_extracter)

        imageView=findViewById(R.id.imageView)
        extractedText=findViewById(R.id.extractedText)
        captureBtn=findViewById(R.id.captureBtn)
        extractBtn=findViewById(R.id.extractBtn)
        clear=findViewById(R.id.clearText)
        pickImage=findViewById(R.id.pickImage)
        copyBtn=findViewById(R.id.copyBtn)
        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        lottieAnim = findViewById(R.id.lottieAnim)
        scanningBar = findViewById(R.id.scanningBar)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }

        clear.setOnClickListener {
            extractedText.text=""
        }

        captureBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA), 0)
            }else{
                takeImage()
                extractedText.text=""
                lottieAnim.visibility= View.GONE
            }
        }

        extractBtn.setOnClickListener {
            processImage()
        }

        pickImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA), 0)
            }else{
                pickFromGallery()
                extractedText.text=""
                lottieAnim.visibility= View.GONE
            }
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA), 0)
        }
        copyBtn.setOnClickListener {
            val textToCopy = extractedText.text.toString()
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", textToCopy)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickFromGallery() { // pick image from gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 2)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processImage() { // get text from image
        if (bitmap!=null){
            scanningBar.visibility=View.VISIBLE
            var image=bitmap?.let {
                InputImage.fromBitmap(it,0)
            }
            image?.let {
                recognizer.process(it).addOnSuccessListener {
                    extractedText.text=it.text
                    scanningBar.visibility=View.GONE
                }.addOnFailureListener {
                    Toast.makeText(this@TextExtracter, "Try Again Please.", Toast.LENGTH_SHORT).show()
                    scanningBar.visibility=View.GONE
                }
            }
        }else{
            Toast.makeText(this@TextExtracter, "Take Image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takeImage() { // capture image
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> { // image capture
                    val capturedBitmap = data?.extras?.get("data") as Bitmap?
                    val file = File(externalCacheDir, "image.png") // Change the file name and location as needed
                    val contentUri = FileProvider.getUriForFile(this, "com.musttool.fileprovider", file)
                    val outputStream = FileOutputStream(file)
                    capturedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()

                    if (contentUri != null) {
                        val intent = Intent(this@TextExtracter, CropeActivity::class.java)
                        intent.putExtra("DATA", contentUri.toString())
                        startActivityForResult(intent, 101)
                    } else {
                        Toast.makeText(this@TextExtracter, "uri is null", Toast.LENGTH_SHORT).show()
                    }

                }
                2->{ // image select
                    val selectedImageUri = data?.data
                    var intent = Intent(this@TextExtracter, CropeActivity::class.java)
                    intent.putExtra("DATA", selectedImageUri.toString())
                    startActivityForResult(intent, 102)

                }
                101->{ // image capture
                    if (requestCode==101&&resultCode==-1){
                        val uriString = data?.getStringExtra("RESULT")
                        if (uriString != null) {
                            val resultUri = Uri.parse(uriString)
                            bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(resultUri))
                            imageView.setImageBitmap(bitmap)
                        } else {
                            Toast.makeText(this@TextExtracter, "Something Wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                102->{  // image select
                    if (requestCode==102&&resultCode==-1){
                        var bundle =data?.getStringExtra("RESULT")
                        if (bundle!==null){
                            var uri=Uri.parse(bundle)
                            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                            imageView.setImageBitmap(bitmap)
                        }
                    }
                }
            }
        }
    }
}
