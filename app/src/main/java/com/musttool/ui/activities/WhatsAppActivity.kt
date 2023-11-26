package com.musttool.ui.activities

import android.annotation.SuppressLint
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.musttool.R
import com.musttool.utils.Utils


class WhatsAppActivity : AppCompatActivity() {
    lateinit var btn:Button
    lateinit var num:EditText
    lateinit var mes:EditText
    var message:String=""
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_app)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        btn=findViewById(R.id.btn)
        num=findViewById(R.id.num)
        mes=findViewById(R.id.mes)
        num.requestFocus()
        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        val shakeAnimation = AnimationUtils.loadAnimation(this, R.drawable.shake_animation)


        btn.setOnClickListener {
            val phoneNumber = num.text.toString()
            message = mes.text.toString()
            if (phoneNumber.isEmpty()){
                num.startAnimation(shakeAnimation)
                Utils.myToast(this,"Please Enter Your Phone Number", Toast.LENGTH_SHORT)
                num.error="Something Wrong"
            }else if (message.isEmpty()){
                mes.startAnimation(shakeAnimation)
                Utils.myToast(this,"Please Enter Your Message", Toast.LENGTH_SHORT)
                mes.error="Something Wrong"
            }else{
                Utils.sendMessageToWhatsApp(this,phoneNumber, message)
            }
        }

        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}