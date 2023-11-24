package com.musttool.data.QrCodeGenerate.qrcoderepoimpl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.musttool.data.QrCodeGenerate.qrcodegeneraterrepo.GenerateQRCodeRepo
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class QrCodeGeneratorImplRepo @Inject constructor():GenerateQRCodeRepo {
    override suspend fun generateQrCode(text: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, 300, 300, null)
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

    override fun getImageUri(context: Context, img: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, img, "Image", null)
        return Uri.parse(path)
    }

}