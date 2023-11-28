package com.musttool.ui.viewmodels

import android.app.Activity
import android.graphics.Bitmap
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airbnb.lottie.LottieAnimationView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import com.musttool.data.TextExtractor.TextExtractorRepo.TextExtractorRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TextExtractorViewModel @Inject constructor(val textExtractorRepo: TextExtractorRepo):ViewModel() {
    fun getTextFromRecognizer(context: Activity, scanningBar: LottieAnimationView, extractText: TextView, bitmap: Bitmap, recognizer: TextRecognizer){
        viewModelScope.launch {
            textExtractorRepo.processImage(context, scanningBar, extractText, bitmap, recognizer)
        }
    }
}