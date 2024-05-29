package com.musttool.ui.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.RippleDrawable
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
import com.musttool.utils.Utils.getRandomAnimation
import com.musttool.utils.Utils.navigationToMainActivity
import com.musttool.utils.Utils.shareImage
import com.musttool.utils.Utils.statusBarColor
import com.musttool.utils.Utils.systemBottomNavigationColor
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
    lateinit var backArrowImg:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_qractivity)
        statusBarColor(this,R.color.myColor)
        systemBottomNavigationColor(this,R.color.navigation_bar_color)

        edt = findViewById(R.id.edt);
        imageQR = findViewById(R.id.imageQR);
        btn = findViewById(R.id.btn);
        share = findViewById(R.id.share);
        lottieAnim = findViewById(R.id.lottieAnim)
        backArrowImg = findViewById(R.id.backArrowImg)
        edt.requestFocus()

        backArrowImg.setOnClickListener {
            navigationToMainActivity(this,backArrowImg) {
                onBackPressed()
            }
        }


        btn.setOnClickListener {
            txt = edt.text.toString().trim()
            if (txt.isNotEmpty()) {
                lottieAnim.visibility= View.GONE
                lifecycleScope.launch {
                    bitmap = QrCodeGenViewModel.setData(this@GenQRActivity,txt) !! // backend class
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
                shareImage(this@GenQRActivity,imageUri)
            }else{
                Utils.myToast(this,"Qr Not Available Yet.",Toast.LENGTH_SHORT)
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}
