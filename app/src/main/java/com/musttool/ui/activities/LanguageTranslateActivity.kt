package com.musttool.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.musttool.R
import com.musttool.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class LanguageTranslateActivity : AppCompatActivity() {

    private var fromLanguage = "en"
    private var toLanguage = "en"
    lateinit var et_translate:EditText
    lateinit var btn_translate:Button
    lateinit var tv_translated:TextView
    lateinit var fromSpinner:Spinner
    lateinit var toSpinner:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_translater)
        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)


        et_translate=findViewById(R.id.et_translate)
        btn_translate=findViewById(R.id.btn_translate)
        tv_translated=findViewById(R.id.tv_translated)
        fromSpinner=findViewById(R.id.fromSpinner)
        toSpinner=findViewById(R.id.toSpinner)

        val languages = resources.getStringArray(R.array.fromLanguages)
        val languageCodes = resources.getStringArray(R.array.FromLanguagesCode)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = adapter
        listener()

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()
                val selectedLanguageCode = languageCodes[position]
                fromLanguage=selectedLanguageCode.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        val toAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        toSpinner.adapter = toAdapter

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()
                val selectedLanguageCode = languageCodes[position]
                toLanguage=selectedLanguageCode.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun listener() {

        btn_translate.setOnClickListener {
            val text = et_translate.text.toString()
            if (text.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val doc = Jsoup.connect(
                        "https://translate.google.com/m?hl=en" +
                                "&sl=$fromLanguage" +
                                "&tl=$toLanguage" +
                                "&ie=UTF-8&prev=_m" +
                                "&q=$text"
                    )
                    .timeout(6000)
                    .get()

                    withContext(Dispatchers.Main) {
                        val element = doc.getElementsByClass("result-container")
                        val textIs: String
                        if (element.isNotEmpty()) {
                            textIs = element[0].text()
                            tv_translated.text = textIs
                        }
                    }

                }

            } else {
                Toast.makeText(this, "Enter text to translate.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
