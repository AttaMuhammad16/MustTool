package com.musttool.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.vision.Frame
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.musttool.R
import com.musttool.ui.viewmodels.TextExtractorViewModel
import com.musttool.utils.CameraPermissionHandler.CameraPermissionHandler
import com.musttool.utils.Utils
import com.musttool.utils.Utils.REQUEST_CODE_IMAGE_FROM_CAMERA_TO_CROP_ACTIVITY
import com.musttool.utils.Utils.REQUEST_CODE_IMAGE_FROM_GALLERY_TO_CROP_ACTIVITY
import com.musttool.utils.Utils.REQUEST_CODE_IMAGE_PICK_FROM_GALLERY
import com.musttool.utils.Utils.REQUEST_CODE_TAKE_IMAGE_FROM_CAMERA
import com.musttool.utils.Utils.getCapturedImage
import com.musttool.utils.Utils.getImageFromGallery
import com.musttool.utils.Utils.gotoCropActivity
import com.musttool.utils.Utils.myToast
import com.musttool.utils.Utils.takeImageFromCamera
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class TextExtracter : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var extractedText: TextView
    private lateinit var captureBtn: Button
    private lateinit var extractBtn: Button
    private lateinit var clear: Button
    private lateinit var pickImage: Button
    private lateinit var copyBtn: Button
    private lateinit var recognizer :com.google.android.gms.vision.text.TextRecognizer
    private var bitmap: Bitmap? = null
    private lateinit var lottieAnim: LottieAnimationView
    private lateinit var scanningBar: LottieAnimationView

    @Inject
    lateinit var cameraPermissionHandler: CameraPermissionHandler
    val textExtractorViewModel: TextExtractorViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_extracter)
        recognizer=com.google.android.gms.vision.text.TextRecognizer.Builder(this).build()

        imageView = findViewById(R.id.imageView)
        extractedText = findViewById(R.id.extractedText)
        captureBtn = findViewById(R.id.captureBtn)
        extractBtn = findViewById(R.id.extractBtn)
        clear = findViewById(R.id.clearText)
        pickImage = findViewById(R.id.pickImage)
        copyBtn = findViewById(R.id.copyBtn)
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
            var tv = extractedText.text
            if (tv == "Your Text") {
            } else {
                extractedText.text = ""
            }
        }


        captureBtn.setOnClickListener {
            if (cameraPermissionHandler.checkStorageAndCameraPermissions()) {
                takeImage()
                extractedText.text = ""
                lottieAnim.visibility = View.GONE
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    cameraPermissionHandler.requestStorageAndCameraPermissions()
                    myToast(this, "Permission Required", Toast.LENGTH_SHORT)
                } else {
                    cameraPermissionHandler.requestStorageAndCameraPermissions()
                }
            }
        }


        extractBtn.setOnClickListener {
            if (bitmap!=null){
                recognizeTextFromImage()
            }
        }

        pickImage.setOnClickListener {
            if (cameraPermissionHandler.checkStorageAndCameraPermissions()) {
                pickFromGallery()
                extractedText.text = ""
                lottieAnim.visibility = View.GONE
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    cameraPermissionHandler.requestStorageAndCameraPermissions()
                    myToast(this, "Permission Required", Toast.LENGTH_SHORT)
                } else {
                    cameraPermissionHandler.requestStorageAndCameraPermissions()
                }
            }
        }


        if (cameraPermissionHandler.checkStorageAndCameraPermissions()) {
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                cameraPermissionHandler.requestStorageAndCameraPermissions()
            } else {
                cameraPermissionHandler.requestStorageAndCameraPermissions()
            }
        }


        copyBtn.setOnClickListener {
            val textToCopy = extractedText.text.toString().trim()
            if (textToCopy == "Your Text") {
                myToast(this, "Please Select Image To get Text", Toast.LENGTH_SHORT)
            } else {
                Utils.copyToClipboard(this, textToCopy)
                myToast(this, "TextCopied", Toast.LENGTH_SHORT)
            }
        }
    }


    private fun pickFromGallery() { // pick image from gallery
        getImageFromGallery(this)
    }

//    private fun processImage() {
//        if (bitmap != null) {
////            textExtractorViewModel.getTextFromRecognizer(this@TextExtracter, scanningBar, extractedText, bitmap!!, recognizer)
//
//        } else {
//            myToast(this, "Take Image", Toast.LENGTH_SHORT)
//        }
//    }

    private fun takeImage() { // capture image
        takeImageFromCamera(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQUEST_CODE_TAKE_IMAGE_FROM_CAMERA -> { // image capture
                    val contentUri = getCapturedImage(data, this)
                    if (contentUri != null) {
                        gotoCropActivity(this, contentUri, "DATA", REQUEST_CODE_IMAGE_FROM_CAMERA_TO_CROP_ACTIVITY)
                    } else {
                        myToast(this, "Captured Image is Null.", Toast.LENGTH_SHORT)
                    }
                }


                REQUEST_CODE_IMAGE_PICK_FROM_GALLERY -> { // image select
                    val selectedImageUri = data?.data
                    if (selectedImageUri != null) {
                        gotoCropActivity(this, selectedImageUri, "DATA", REQUEST_CODE_IMAGE_FROM_GALLERY_TO_CROP_ACTIVITY)
                    } else {
                        myToast(this, "Gallery Image is Null.", Toast.LENGTH_SHORT)
                    }
                }


                REQUEST_CODE_IMAGE_FROM_CAMERA_TO_CROP_ACTIVITY -> { // image capture
                    val uriString = data?.getStringExtra("RESULT")
                    if (uriString != null) {
                        val resultUri = Uri.parse(uriString)
                        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(resultUri))
                        imageView.setImageBitmap(bitmap)
                    }
                }


                REQUEST_CODE_IMAGE_FROM_GALLERY_TO_CROP_ACTIVITY -> {  // image select
                    val bundle = data?.getStringExtra("RESULT")
                    if (bundle !== null) {
                        val uri = Uri.parse(bundle)
                        bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }


    private fun recognizeTextFromImage() {
        // Show the scanning animation
        scanningBar.visibility = View.VISIBLE

        // Create an InputImage from the bitmap
        val inputImage = InputImage.fromBitmap(bitmap!!, 0)

        // Get an instance of TextRecognizer
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        // Process the image
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val recognizedText = visionText.text
                extractedText.text = if (recognizedText.isNotEmpty()) {
                    recognizedText.trim()
                } else {
                    "No text found in the image."
                }
                scanningBar.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("TAG", "Text recognition failed: ${exception.localizedMessage}")
                extractedText.text = "Text recognition failed."
                scanningBar.visibility = View.GONE
            }
    }



}
