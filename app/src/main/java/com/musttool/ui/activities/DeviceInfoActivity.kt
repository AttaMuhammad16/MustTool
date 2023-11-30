package com.musttool.ui.activities

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.an.deviceinfo.device.model.Battery
import com.an.deviceinfo.device.model.Device
import com.an.deviceinfo.device.model.Memory
import com.an.deviceinfo.location.LocationInfo
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.musttool.R
import com.musttool.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask


class DeviceInfoActivity : AppCompatActivity() {
    val LOCATION_PERMISSION_REQUEST_CODE = 123
    lateinit var manufacturer:TextView
    lateinit var deviceNum:TextView
    lateinit var version:TextView
    lateinit var versionCode:TextView
    lateinit var model:TextView
    lateinit var product:TextView
    lateinit var fingerprint:TextView
    lateinit var hardware:TextView
    lateinit var radioVersion:TextView
    lateinit var board:TextView
    lateinit var displayVersion:TextView
    lateinit var buildBrand:TextView
    lateinit var buildHost:TextView
    lateinit var buildTime:TextView
    lateinit var buildUser:TextView
    lateinit var serialNumber:TextView
    lateinit var osVersion:TextView
    lateinit var language:TextView
    lateinit var sdkVersion:TextView
    lateinit var screenDensity:TextView
    lateinit var screenHeight:TextView
    lateinit var screenWidth:TextView
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_info)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)

        manufacturer=findViewById(R.id.manufacturer)
        deviceNum=findViewById(R.id.deviceNum)
        version=findViewById(R.id.version)
        versionCode=findViewById(R.id.versionCode)
        model=findViewById(R.id.model)
        product=findViewById(R.id.product)
        fingerprint=findViewById(R.id.fingerprint)
        hardware=findViewById(R.id.hardware)
        radioVersion=findViewById(R.id.radioVersion)
        board=findViewById(R.id.board)
        displayVersion=findViewById(R.id.displayVersion)
        buildBrand=findViewById(R.id.buildBrand)
        buildHost=findViewById(R.id.buildHost)
        buildTime=findViewById(R.id.buildTime)
        buildUser=findViewById(R.id.buildUser)
        serialNumber=findViewById(R.id.serialNumber)
        osVersion=findViewById(R.id.osVersion)
        language=findViewById(R.id.language)
        sdkVersion=findViewById(R.id.sdkVersion)
        screenDensity=findViewById(R.id.screenDensity)
        screenHeight=findViewById(R.id.screenHeight)
        screenWidth=findViewById(R.id.screenWidth)

        var backArrowImg = findViewById<ImageView>(R.id.backArrowImg)
        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }

        // device info
        val device = Device(this)

        var Manufacturer = device.manufacturer
        var deviced = device.device
        var ReleaseBuildVersion = device.releaseBuildVersion
        var BuildVersionCodeName = device.buildVersionCodeName
        var Model = device.model
        var Product = device.product
        var Fingerprint = device.fingerprint
        var hasFingerprint = !Fingerprint.isNullOrEmpty()
        var Hardware = device.hardware
        var RadioVersion = device.radioVersion
        var Board = device.board
        var DisplayVersion = device.displayVersion
        var BuildBrand = device.buildBrand
        var BuildHost = device.buildHost
        var BuildTime = device.buildTime
        var BuildUser = device.buildUser
        var Serial = device.serial
        var OsVersion = device.osVersion
        var Language = device.language
        var SDKVersion = device.sdkVersion
        var ScreenDensity = device.screenDensity
        var ScreenHeight = device.screenHeight
        var ScreenWidth = device.screenWidth


        manufacturer.text=Manufacturer.toString()
        deviceNum.text=deviced.toString()
        version.text=ReleaseBuildVersion.toString()
        versionCode.text=BuildVersionCodeName.toString()
        model.text=Model.toString()
        product.text=Product.toString()
        fingerprint.text=hasFingerprint.toString()
        hardware.text=Hardware.toString()
        radioVersion.text=RadioVersion.toString().replace(",","\n")
        board.text=Board.toString()
        displayVersion.text=DisplayVersion.toString()
        buildBrand.text=BuildBrand.toString()
        buildHost.text=BuildHost.toString()

        val timestampMillis = BuildTime
        val date = Date(timestampMillis)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        buildTime.text=formattedDate
        buildUser.text=BuildUser.toString()
        serialNumber.text=Serial.toString()
        osVersion.text=OsVersion.toString()
        language.text=Language.toString()
        sdkVersion.text=SDKVersion.toString()
        screenDensity.text=ScreenDensity.toString()
        screenHeight.text=ScreenHeight.toString()
        screenWidth.text=ScreenWidth.toString()


    }
}