package com.musttool.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.musttool.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class GenQRActivity : AppCompatActivity() {
    lateinit var imageQR: ImageView
    lateinit var edt: EditText
    lateinit var btn:Button
    lateinit var share:Button
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_qractivity)
        window.statusBarColor = ContextCompat.getColor(this, R.color.temperatureActivityColor)

        edt = findViewById(R.id.edt);
        imageQR = findViewById(R.id.imageQR);
        btn = findViewById(R.id.btn);
        share = findViewById(R.id.share);

        btn.setOnClickListener {
            val txt = edt.text.toString().trim()
            if (txt.isNotEmpty()) {
                bitmap = generateQRCode(txt)!!
                imageQR.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Please Enter Some text", Toast.LENGTH_SHORT).show()
            }
        }

        share.setOnClickListener{
            if(bitmap!=null){
                val imageUri: Uri = getImageUri(this, bitmap)
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_STREAM, imageUri)
                    type = "image/*"
                }
                startActivity(Intent.createChooser(shareIntent, "Share image via"))
            }
        }

    }
    private fun generateQRCode(value: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(value, BarcodeFormat.QR_CODE, 300, 300, null)
        } catch (IllegalArgumentException: WriterException) {
            return null
        }
        val bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565)
        for (x in 0 until 300) {
            for (y in 0 until 300) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }

    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Image", null)
        return Uri.parse(path)
    }

}