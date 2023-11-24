package com.musttool.data.QrCodeGenerate.qrcodegeneraterrepo

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

interface GenerateQRCodeRepo {
    suspend fun generateQrCode(text:String): Bitmap?
    fun getImageUri(context: Context,img:Bitmap): Uri?
}