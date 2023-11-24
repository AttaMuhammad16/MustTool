package com.musttool.ui.viewmodels

import android.util.Log
import android.util.SparseArray
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class QrCodeScannerViewModel @Inject constructor() : ViewModel() {
    private var _data: MutableLiveData<SparseArray<Barcode>> = MutableLiveData()
    val data: LiveData<SparseArray<Barcode>> = _data
    fun setBarcodeProcessor(barcodeDetector: BarcodeDetector) {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                _data.postValue(barcodes)
            }
        })
    }
}
