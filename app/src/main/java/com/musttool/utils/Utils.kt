package com.musttool.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.drawable.RippleDrawable
import android.net.Uri
import android.provider.Browser
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.gms.vision.CameraSource
import com.musttool.R
import com.musttool.ui.activities.*
import java.io.IOException

object Utils {
    val REQUEST_CAMERA_PERMISSION = 201

    fun shareText(context: Context, extraSubject: String, extraText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, extraSubject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, extraText)
        context.startActivity(Intent.createChooser(shareIntent, "Share link via"))
    }

    fun shareImage(context: Context, imgUri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imgUri)
            type = "image/*"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share image via"))
    }

    fun exitDialog(
        context: Context,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText) { dialog, which ->
            (context as Activity).finishAffinity()
        }
        builder.setNegativeButton(negativeButtonText) { dialog, which ->
            dialog.cancel()
        }
        val alert = builder.create()
        alert.show()
    }

    fun getIntent(context: Context, position: Int): Intent? {
        return when (position) {
            0 -> Intent(context, GenQRActivity::class.java)
            1 -> Intent(context, BarCodeScanner::class.java)
            2 -> Intent(context, TemperatureConverter::class.java)
            3 -> Intent(context, NotesApp::class.java)
            4 -> Intent(context, WhatsAppActivity::class.java)
            5 -> Intent(context, TextExtracter::class.java)
            6 -> Intent(context, MagneticFiledActivity::class.java)
            7 -> Intent(context, Acceleration::class.java)
            8 -> Intent(context, GravityActivity::class.java)
            9 -> Intent(context, LightMeasureActivity::class.java)
            10 -> Intent(context, GyroScopeActivity::class.java)
            11 -> Intent(context, CompassActivity::class.java)
            12 -> Intent(context, CalculatoreActivity::class.java)
            13 -> Intent(context, FlashLightActivity::class.java)
            14 -> Intent(context, DeviceInfoActivity::class.java)
            15 -> Intent(context, CPUInfoActivity::class.java)
            16 -> Intent(context, BatteryInfo::class.java)
            17 -> Intent(context, AvailableSensors::class.java)
            18 -> Intent(context, SOSFlashLightActivity::class.java)
            19 -> Intent(context, RamUseage::class.java)
            20 -> Intent(context, UserLocation::class.java)
            21 -> Intent(context, LanguageTranslateActivity::class.java)
            else -> null
        }
    }


    fun myToast(context: Context, data: String, duration: Int) {
        Toast.makeText(context, data, duration).show()
    }


    fun getRandomAnimation(randomValue: Int, context: Context) {
        when (randomValue) {
            0 -> Animatoo.animateZoom(context)
            1 -> Animatoo.animateSlideDown(context)
            2 -> Animatoo.animateSlideUp(context)
            3 -> Animatoo.animateFade(context)
            4 -> Animatoo.animateSpin(context)
            5 -> Animatoo.animateWindmill(context)
            6 -> Animatoo.animateShrink(context)
            7 -> Animatoo.animateInAndOut(context)
            8 -> Animatoo.animateCard(context)
            9 -> Animatoo.animateDiagonal(context)
            10 -> Animatoo.animateSplit(context)
        }

    }


    fun isValidURL(url: String): Boolean {
        return try {
            var uri = Uri.parse(url)
            uri.scheme != null && (uri.scheme == "http" || uri.scheme == "https")
        } catch (e: Exception) {
            false
        }
    }


    fun openInChrome(context: Context, url: String) {
        val chromeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        chromeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        chromeIntent.putExtra(Browser.EXTRA_APPLICATION_ID, "com.android.chrome")
        context.startActivity(chromeIntent)
    }


    fun copyToClipboard(context: Context, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
    }


    fun startCameraSource(cameraSource: CameraSource, context: Context, surfaceView: SurfaceView) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            cameraSource.start(surfaceView.holder)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun handleCameraPermissionResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted.invoke()
            } else {
                onPermissionDenied.invoke()
            }
        }
    }

    fun statusBarColor(context: Context, color: Int) {
        (context as Activity).window.statusBarColor = ContextCompat.getColor(context, color)
    }

    fun systemBottomNavigationColor(context: Context, color: Int) {
        (context as Activity).window.navigationBarColor = ContextCompat.getColor(context, color)
    }


    fun preventShowingKeyboard(context: Context) {
        (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    fun navigationToMainActivity(
        context: Context,
        backArrowImg: ImageView,
        onBackPressedAction: () -> Unit
    ) {
        val rippleDrawable = RippleDrawable(
            ColorStateList.valueOf(context.resources.getColor(R.color.rippleColor)),
            null,
            null
        )
        backArrowImg.background = rippleDrawable
        onBackPressedAction.invoke()
        getRandomAnimation(0, context)
    }


    fun setSearchViewHintColor(context: Context, searchView: SearchView, color: Int, inputText: String? = null) {
        val queryHint = "Search"
        val hintColor = ContextCompat.getColor(context, color)
        val spannable = SpannableString(queryHint)
        spannable.setSpan(ForegroundColorSpan(hintColor), 0, queryHint.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (!inputText.isNullOrBlank()) {
            searchView.setQuery(inputText, false)
        }else{
            searchView.queryHint = spannable
        }
    }

    @SuppressLint("DiscouragedApi")
    fun searchViewTextClearSearchIconsColor(searchView: SearchView, context: Context, color: Int){
        (searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText).setTextColor(ContextCompat.getColor(context, color))
        (searchView.findViewById<View>(androidx.appcompat.R.id.search_mag_icon) as ImageView).setColorFilter(ContextCompat.getColor(context, color))
        (searchView.findViewById<View>(androidx.appcompat.R.id.search_close_btn) as ImageView).setColorFilter(ContextCompat.getColor(context, color))
    }



    fun myColors():IntArray{
        var itemColors = intArrayOf(
            R.color.color1,
            R.color.color2,
            R.color.color4,
            R.color.color5,
            R.color.color6,
            R.color.color7,
            R.color.color8,
            R.color.color9,
            R.color.color10,
            R.color.color11,
            R.color.color12,
            R.color.color13,
            R.color.color14,
            R.color.color15,
            R.color.color16,
            R.color.color17,
        )
        return itemColors
    }

}