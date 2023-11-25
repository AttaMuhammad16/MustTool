package com.musttool.data.QrCodeGenerate.qrcodegeneraterrepo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.musttool.R
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class QrCodeGeneratorImplRepo @Inject constructor():GenerateQRCodeRepo {
    override suspend fun generateQrCode(context: Context,text: String): Bitmap? {
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode("Install This App\nhttps://play.google.com/store/apps/details?id=com.musttool\nYour Data:\n$text", BarcodeFormat.QR_CODE, 300, 300, null)
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