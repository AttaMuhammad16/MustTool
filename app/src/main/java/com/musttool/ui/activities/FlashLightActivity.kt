package com.musttool.ui.activities


import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.musttool.R
import com.musttool.utils.Utils
import com.musttool.utils.Utils.offFlash
import com.musttool.utils.Utils.onFlash
import com.musttool.utils.Utils.vibratePhone

class FlashLightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flash_light)
        Utils.statusBarColor(this, R.color.myColor)
        systemBottomNavigationBlackColor(this, R.color.black)
        var img=findViewById<ImageView>(R.id.img)

        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }
        var i=true
        val vibrator= getSystemService(VIBRATOR_SERVICE) as Vibrator

        img.setOnClickListener {
            if (i){
                vibratePhone(vibrator,60)
                onFlash(this)
               img.setImageResource(R.drawable.flash_light_on)
                i=false
            }else{
                vibratePhone(vibrator,60)
                offFlash(this)
                img.setImageResource(R.drawable.flash_light_of)
                i=true
            }
        }

    }
    fun systemBottomNavigationBlackColor(context: Context, color: Int) {
        (context as Activity).window.navigationBarColor = ContextCompat.getColor(context, color)
    }

}