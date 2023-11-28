package com.musttool.data.TextExtractor.TextExtractorRepo

import android.app.Activity
import android.graphics.Bitmap
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.mlkit.vision.text.TextRecognizer

interface TextExtractorRepo {
    fun processImage(context:Activity,scanningBar:LottieAnimationView,extractText: TextView,bitmap: Bitmap,recognizer: TextRecognizer)
}