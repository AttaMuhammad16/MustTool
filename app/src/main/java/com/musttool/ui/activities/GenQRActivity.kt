package com.musttool.ui.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.musttool.R
import com.musttool.ui.viewmodels.QrCodeGeneratorViewModel
import com.musttool.utils.AnimationUtils
import com.musttool.utils.Toaster
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.random.nextInt


@AndroidEntryPoint
class GenQRActivity : AppCompatActivity() {
    lateinit var imageQR: ImageView
    lateinit var edt: EditText
    lateinit var btn:Button
    lateinit var share:Button
    lateinit var bitmap: Bitmap
    val QrCodeGenViewModel:QrCodeGeneratorViewModel by viewModels()
    lateinit var txt:String
    lateinit var lottieAnim:LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_qractivity)
        window.statusBarColor = ContextCompat.getColor(this, R.color.myColor)

        edt = findViewById(R.id.edt);
        imageQR = findViewById(R.id.imageQR);
        btn = findViewById(R.id.btn);
        share = findViewById(R.id.share);
        lottieAnim = findViewById(R.id.lottieAnim)

        btn.setOnClickListener {
            txt = edt.text.toString().trim()
            if (txt.isNotEmpty()) {
                lottieAnim.visibility= View.GONE
                lifecycleScope.launch {
                    bitmap = QrCodeGenViewModel.setData(txt) !! // backend class
                    imageQR.setImageBitmap(bitmap)
                }
            } else {
               Toaster.myToast(this,"Please Enter Your Text.")
            }
        }
        share.setOnClickListener{
            if(::bitmap.isInitialized){
                val defaultUri: Uri = Uri.parse("")
                val imageUri: Uri = QrCodeGenViewModel.getImageUri(this, bitmap) ?: defaultUri  // backend class
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    type = "image/*"
                }
                startActivity(Intent.createChooser(shareIntent, "Share image via"))
            }else{
                Toaster.myToast(this,"Qr Not Available Yet.")
            }
        }
    }
}