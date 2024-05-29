package com.musttool.ui.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.musttool.R
import com.musttool.adapters.AppAdapter
import com.musttool.models.AppModel
import com.musttool.utils.Utils
import io.github.derysudrajat.compassqibla.CompassQibla


class MainActivity:AppCompatActivity() {
    val REQUEST_CODE=1

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerView.Adapter<AppAdapter.ViewHolder>
    lateinit var apps: ArrayList<AppModel>
    lateinit var layoutManger: RecyclerView.LayoutManager

    lateinit var drawer: DrawerLayout
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var navDrwer: NavigationView

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var appUpdateInfoTask: Task<AppUpdateInfo>


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        appUpdateManager = AppUpdateManagerFactory.create(this@MainActivity)
        appUpdateInfoTask = appUpdateManager.appUpdateInfo

        checkForUpdate()

        recyclerView = findViewById(R.id.recyclerView)
        apps = ArrayList()
        val menu=findViewById<ImageButton>(R.id.menu)

        menu.setOnClickListener {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawer = findViewById(R.id.drawer)
        navDrwer = findViewById(R.id.navDrwer)
        toggle = ActionBarDrawerToggle(this@MainActivity, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navDrwer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.share -> {
                   Utils.shareText(this,"MustToolApp","https://play.google.com/store/apps/details?id=com.musttool")
                }
                R.id.contact ->{
                    SweetAlertDialog(this@MainActivity, SweetAlertDialog.NORMAL_TYPE).setTitleText("atta1639916@gmail.com").show()
                }
            }
            true
        }

        apps.add(AppModel("QR Generator", R.drawable.qr))
        apps.add(AppModel("Bar code & Qr Code\n Scanner", R.drawable.qrc))
        apps.add(AppModel("Temperature Converter", R.drawable.converter))
        apps.add(AppModel("Notes", R.drawable.notes_bac))
        apps.add(AppModel("Message Sender", R.drawable.wht))
        apps.add(AppModel("Text Extractor", R.drawable.text_extractor))
        apps.add(AppModel("Magnetic Filed Detector", R.drawable.magnet))
        apps.add(AppModel("Acceleration Meter", R.drawable.acceleration))
        apps.add(AppModel("Gravity Meter", R.drawable.gravity))
        apps.add(AppModel("Light Measurement/ Lux meter", R.drawable.lightbulb))
        apps.add(AppModel("Gyroscope Testing", R.drawable.gyroscope))
        apps.add(AppModel("Compass(direction)", R.drawable.navigation))
        apps.add(AppModel("Calculator", R.drawable.calculator))
        apps.add(AppModel("Flash Light", R.drawable.flash_icon))
        apps.add(AppModel("Device Info", R.drawable.device))
        apps.add(AppModel("CPU Info", R.drawable.cpu))
        apps.add(AppModel("Battery Info", R.drawable.battery_info))
        apps.add(AppModel("Available Sensors", R.drawable.sensor))
        apps.add(AppModel("SOS FlashLight", R.drawable.sos))
        apps.add(AppModel("Memory Usage", R.drawable.ram))
        apps.add(AppModel("User Location", R.drawable.baseline_location_on_24))
        apps.add(AppModel("Multi\nLanguages Translator", R.drawable.language_translator))

        recyclerView = findViewById(R.id.recyclerView)
        layoutManger = GridLayoutManager(this, 4)
        recyclerView.layoutManager = layoutManger
        adapter = AppAdapter(apps, this)
        recyclerView.adapter = adapter

        CompassQibla.Builder(this).onPermissionGranted { permission ->
            Toast.makeText(this, "PermissionGranted", Toast.LENGTH_SHORT).show()
        }.onPermissionDenied {
            Toast.makeText(this, "PermissionDenied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            Utils.exitDialog(this,"Do you want to Exit?","Yes","No")
        }
    }

    private fun checkForUpdate(){
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,this@MainActivity,REQUEST_CODE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressUpdate()
    }

    private fun inProgressUpdate() {
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,this@MainActivity,1)
            }
        }
    }

}