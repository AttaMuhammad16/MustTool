package com.musttool

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.number.Precision.currency
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.navigation.NavigationView
import com.musttool.adapters.AppAdapter
import com.musttool.models.AppModel


class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: RecyclerView.Adapter<AppAdapter.ViewHolder>
    lateinit var apps: ArrayList<AppModel>
    lateinit var layoutManger: RecyclerView.LayoutManager

    lateinit var drawer: DrawerLayout
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var navDrwer: NavigationView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        apps = ArrayList()
        var toolBar=findViewById<Toolbar>(R.id.toolBAR)

        window.statusBarColor = ContextCompat.getColor(this, R.color.myColor)

        var menu=findViewById<ImageButton>(R.id.menu)

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
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MustToolApp")
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.musttool")
                    startActivity(Intent.createChooser(shareIntent, "Share link via"))
                }
                R.id.contact->{
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
        apps.add(AppModel("Acceleration Meter", R.drawable.accelerartion))
        apps.add(AppModel("Gravity Meter", R.drawable.gravity))
        apps.add(AppModel("Light Measurement/ Lux meter", R.drawable.light))
        apps.add(AppModel("Gyroscope Testing", R.drawable.gyroscope))
        apps.add(AppModel("Compass(direction)", R.drawable.navigation))
        apps.add(AppModel("Calculator", R.drawable.calculator))
        apps.add(AppModel("Flash Light", R.drawable.flashlight))
        apps.add(AppModel("Device Info", R.drawable.baseline_perm_device_information_24))
        apps.add(AppModel("CPU Info", R.drawable.cpu))
        apps.add(AppModel("Battery Info", R.drawable.battery))
        apps.add(AppModel("Available Sensors", R.drawable.sensor))
        apps.add(AppModel("SOS FlashLight", R.drawable.sos))
        apps.add(AppModel("Memory Usage", R.drawable.ram))
        apps.add(AppModel("User Location", R.drawable.baseline_location_on_24))
        apps.add(AppModel("Multi\nLanguages Translator", R.drawable.language_translator))
//        apps.add(AppModel("UrlShortner", R.drawable.language_translator))


        recyclerView = findViewById(R.id.recyclerView)
        layoutManger = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManger
        adapter = AppAdapter(apps, this)
        recyclerView.adapter = adapter
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            builder.setCancelable(false)
            builder.setMessage("Do you want to Exit?")
            builder.setPositiveButton("Yes") { dialog, which ->
                finishAffinity()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            val alert = builder.create()
            alert.show()
        }
    }
}