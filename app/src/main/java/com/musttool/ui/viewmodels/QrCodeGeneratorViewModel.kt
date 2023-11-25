package com.musttool.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.musttool.data.QrCodeGenerate.qrcodegeneraterrepo.GenerateQRCodeRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QrCodeGeneratorViewModel @Inject constructor(val generateQRCode: GenerateQRCodeRepo) : ViewModel() {
    private val _data: MutableLiveData<String> = MutableLiveData()
    val data: LiveData<String> = _data

    private var deferred: CompletableDeferred<Bitmap?>? = null

    suspend fun setData(context: Context,txt: String):Bitmap? {
        _data.value = txt
        return generateQrcode(context,data.value.toString())
    }

    private suspend fun generateQrcode(context: Context,value:String): Bitmap? {
        deferred = CompletableDeferred()
        CoroutineScope(Dispatchers.IO).launch {
            val result = async {
                generateQRCode.generateQrCode(context,value)
            }
            deferred?.complete(result.await())
        }
        return deferred?.await()
    }

    fun getImageUri(context: Context,img:Bitmap): Uri? {
        return generateQRCode.getImageUri(context, img)
    }
}

