package com.musttool.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.musttool.R

class FullNoteActivity : AppCompatActivity() {
    var title=""
    var des=""
    var date=""
    var color=0
    lateinit var date_tv:TextView
    lateinit var title_tv:TextView
    lateinit var des_tv:TextView
    lateinit var constraint:ConstraintLayout
    lateinit var scroll:ScrollView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_note)

        date_tv=findViewById(R.id.date_tv)
        title_tv=findViewById(R.id.title_tv)
        des_tv=findViewById(R.id.des_tv)
        constraint=findViewById(R.id.constraint)
        scroll=findViewById(R.id.scroll)

        title=intent.getStringExtra("title").toString()
        des=intent.getStringExtra("des").toString()
        date=intent.getStringExtra("date").toString()
        color=intent.getIntExtra("color",-1)

        if(color!=-1){
            window.statusBarColor = color
            constraint.setBackgroundColor(color)
            scroll.setBackgroundColor(color)
        }
        date_tv.text=date
        title_tv.text=title
        des_tv.text=des

    }
}