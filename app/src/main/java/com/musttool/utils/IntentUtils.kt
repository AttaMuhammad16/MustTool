package com.musttool.utils

import android.content.Context
import android.content.Intent
import com.musttool.ui.activities.*

object IntentUtils {
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
}
