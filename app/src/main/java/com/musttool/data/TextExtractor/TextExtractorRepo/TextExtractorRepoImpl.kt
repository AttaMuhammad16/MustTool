package com.musttool.data.TextExtractor.TextExtractorRepo

import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.musttool.utils.Utils
import javax.inject.Inject

class TextExtractorRepoImpl @Inject constructor() :TextExtractorRepo {
    override fun processImage(context: Activity, scanningBar: LottieAnimationView, extractText: TextView,bitmap: Bitmap,recognizer: TextRecognizer) {
        scanningBar.visibility= View.VISIBLE
        var image=bitmap?.let {
            InputImage.fromBitmap(it,0)
        }
        image?.let {
            recognizer.process(it).addOnSuccessListener {
                extractText.text=it.text
                scanningBar.visibility= View.GONE
            }.addOnFailureListener {
                Utils.myToast(context,"Try Again",Toast.LENGTH_SHORT)
                scanningBar.visibility= View.GONE
            }
        }
    }
}