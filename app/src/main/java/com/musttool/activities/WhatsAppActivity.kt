package com.musttool.activities

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.musttool.R


class WhatsAppActivity : AppCompatActivity() {
    lateinit var btn:Button
    lateinit var num:EditText
    lateinit var mes:EditText
    var message:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whats_app)
        window.statusBarColor = ContextCompat.getColor(this, R.color.temperatureActivityColor)

        btn=findViewById(R.id.btn)
        num=findViewById(R.id.num)
        mes=findViewById(R.id.mes)
        num.requestFocus()


        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (clipboard.hasPrimaryClip() && clipboard.primaryClipDescription!!.hasMimeType(MIMETYPE_TEXT_PLAIN)) {
            val clipData = clipboard.primaryClip
            val itemCount = clipData?.itemCount ?: 0
            if (itemCount > 0) {
                val item = clipData?.getItemAt(0)
                val text = item?.text?.toString()
                Log.i("TAG", text!!)
                btn.text = text
                if (!text.isNullOrEmpty()) {
                    mes.setText(text)
                    Log.i("TAG", "onCreate: $text")
                } else {
                    text.let {
                        if (it != null) {
                            if (it.length in 11..13) {
                                num.setText(it)
                                Log.i("TAG", "onCreate: $text")
                            }
                        }
                    }
                }
            }
        }

        btn.setOnClickListener {
            val phoneNumber = num.text.toString()
            message = mes.text.toString()
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$phoneNumber/?text=${Uri.encode(message)}")
            startActivity(intent)
        }
    }
}