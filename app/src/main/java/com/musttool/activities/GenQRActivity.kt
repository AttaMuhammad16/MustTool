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
    lateinit var save:Button
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gen_qractivity)
        window.statusBarColor = ContextCompat.getColor(this, R.color.temperatureActivityColor)
        edt = findViewById(R.id.edt);
        imageQR = findViewById(R.id.imageQR);
        btn = findViewById(R.id.btn);

        share = findViewById(R.id.share);
        save = findViewById(R.id.save);

        btn.setOnClickListener {
            val txt = edt.text.toString().trim()
            if (txt.isNotEmpty()) {
                bitmap = generateQRCode(txt)!!
                imageQR.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Please Enter Some text", Toast.LENGTH_SHORT).show()
            }
        }

        save.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
            } else {
                saveBitmapToStorage(bitmap)
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

//private fun saveBitmapToStorage(bitmap: Bitmap, fileName: String) {
//    val file = File(Environment.getExternalStorageDirectory().absolutePath + "$fileName.png")
//    try {
//        val stream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//        stream.flush()
//        stream.close()
//    } catch (e: IOException) {
//        e.printStackTrace()
//        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//    }
//}
//    private fun saveBitmapToStorage(bitmap: Bitmap, fileName: String) {
//        val root = Environment.getExternalStorageDirectory().absolutePath
//        val myDir = File("$root/saved_images")
//        myDir.mkdirs()
//
//        val file = File(myDir, fileName)
//        if (file.exists()) Toast.makeText(this, "file already exists", Toast.LENGTH_SHORT).show()
//        try {
//            val out = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
//            out.flush()
//            out.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


    fun saveBitmapToStorage(bitmap: Bitmap) {
        val folder = File(Environment.getExternalStorageDirectory().toString()+"/MyAppFolder")
        if (!folder.exists()) {
            folder.mkdirs()
        }

        val file = File(folder, "MyImage.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()

        // Scan the newly created image file to add it to the system gallery
//        MediaScannerConnection.scanFile(this,arrayOf(file.toString()),null
//        ) { path, _ ->
//            Log.i("TAG","Image saved successfully: $path")
//        }
    }


    fun getImageUri(context: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Image", null)
        return Uri.parse(path)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (10) { 10 -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveBitmapToStorage(bitmap)
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied, unable to save", Toast.LENGTH_SHORT).show()
            }
            return
        }
        }
    }

}