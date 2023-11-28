package com.musttool.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.musttool.R
import com.musttool.utils.Utils
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID


class CropeActivity : AppCompatActivity() {
    var uri: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crope)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        var bundle=intent.getStringExtra("DATA")
        uri=Uri.parse(bundle)

        val options = UCrop.Options()
        val destinationUri = Uri.fromFile(File(cacheDir, generateRandomFileName()))
        UCrop.of(uri!!, destinationUri)
            .withOptions(options)
            .withAspectRatio(0F, 0F)
            .useSourceImageAspectRatio()
            .withMaxResultSize(2000, 2000)
            .start(this@CropeActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            UCrop.REQUEST_CROP -> {
                if (resultCode == RESULT_OK) {
                    val resultUri = UCrop.getOutput(data!!)
                    val resultIntent = Intent()
                    resultIntent.putExtra("RESULT", resultUri.toString())
                    setResult(RESULT_OK, resultIntent)
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    val cropError = UCrop.getError(data!!)
                    Log.e("CropeActivity", "Crop error: $cropError")
                }
                finish()
            }
        }
    }

    private fun generateRandomFileName(): String {
        val randomFileName = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
        return randomFileName
    }
}
