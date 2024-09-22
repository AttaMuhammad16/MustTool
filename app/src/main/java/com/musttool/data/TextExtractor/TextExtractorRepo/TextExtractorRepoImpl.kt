package com.musttool.data.TextExtractor.TextExtractorRepo

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.vision.Frame
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.musttool.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TextExtractorRepoImpl @Inject constructor() :TextExtractorRepo {
    override fun processImage(
        context: Activity,
        scanningBar: LottieAnimationView,
        extractText: TextView,
        bitmap: Bitmap,
        recognizer: com.google.android.gms.vision.text.TextRecognizer
    ) {

        scanningBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val frame = Frame.Builder().setBitmap(bitmap).build()
            val textBlocks = recognizer.detect(frame)
            val imageText = StringBuilder()
            Log.i("TAG", "processImage:$textBlocks")
            for (i in 0 until textBlocks.size()) {
                Log.i("TAG", "processImage:$i ")
                val textBlock = textBlocks.valueAt(i)
                imageText.append(textBlock.value).append("\n") // Add each text block with a newline for readability
            }
            withContext(Dispatchers.Main) {
                extractText.text = if (imageText.isNotEmpty()) {
                    imageText.toString().trim()
                } else {
                    "No text found in the image."
                }
                scanningBar.visibility = View.GONE
            }
        }
    }


}