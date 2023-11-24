package com.musttool.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.musttool.R
import com.musttool.ui.viewmodels.QrCodeGeneratorViewModel
import com.musttool.utils.Utils
import com.musttool.utils.Utils.shareImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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
                Utils.myToast(this,"Please Enter Your Text.", Toast.LENGTH_SHORT)
            }
        }

        share.setOnClickListener{
            if(::bitmap.isInitialized){
                val defaultUri: Uri = Uri.parse("")
                val imageUri: Uri = QrCodeGenViewModel.getImageUri(this, bitmap) ?: defaultUri  // backend class
                Utils.shareImage(this@GenQRActivity,imageUri)
            }else{
                Utils.myToast(this,"Qr Not Available Yet.",Toast.LENGTH_SHORT)
            }
        }

    }
}