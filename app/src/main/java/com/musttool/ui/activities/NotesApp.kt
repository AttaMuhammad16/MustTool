package com.musttool.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.musttool.MustToolDatabase
import com.musttool.Notes.Note
import com.musttool.R
import com.musttool.adapters.NoteAdapter
import com.musttool.ui.viewmodels.NotesViewModel
import com.musttool.utils.Utils
import com.musttool.utils.Utils.preventShowingKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class NotesApp : AppCompatActivity() {
    @Inject
    lateinit var mustToolDatabase: MustToolDatabase

    lateinit var addBtn: FloatingActionButton
    lateinit var notesRecycler: RecyclerView
    lateinit var list: ArrayList<Note>
    lateinit var adapter: NoteAdapter
    lateinit var searchView: SearchView
    lateinit var temp: ArrayList<Note>
    lateinit var backArrowImg: ImageView
    val notesViewModel: NotesViewModel by viewModels()

    @SuppressLint("ClickableViewAccessibility", "DiscouragedApi", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_app)
        list = ArrayList()
        temp = ArrayList()

        Utils.statusBarColor(this, R.color.myColor)
        Utils.systemBottomNavigationColor(this, R.color.navigation_bar_color)
        addBtn = findViewById(R.id.addBtn)

        Utils.objectAnimater(addBtn) // rotating  animation.

        notesRecycler = findViewById(R.id.notesRecycler)
        searchView = findViewById(R.id.searchView)
        backArrowImg = findViewById(R.id.backArrowImg)
        Utils.searchViewTextClearSearchIconsColor(searchView, this@NotesApp, R.color.black) // change text color and default Icons Colors.


        Utils.setSearchViewHintColor(this, searchView, R.color.hint_edt_color) // set color of the hint in the search view

        backArrowImg.setOnClickListener {
            Utils.navigationToMainActivity(this, backArrowImg) {
                onBackPressed()
            }
        }

        addBtn.setOnClickListener {
            showDialog()
        }


        lifecycleScope.launch { // getting data from data base.
            notesViewModel.apply {
                getNotes()
                notes.observe(this@NotesApp){notesList->
                    list.clear()
                    notesList.forEach { it ->
                        list.add(it)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }

        adapter = NoteAdapter(list, this@NotesApp,mustToolDatabase,notesViewModel)
        notesRecycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        notesRecycler.adapter = adapter
        adapter.notifyDataSetChanged()

        searchView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                searchView.requestFocus()
            }
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val lowercaseQuery = newText?.toLowerCase()
                temp.clear()
                for (data in list) {
                    val lowercaseTitle = data.title?.toLowerCase()
                    val lowercaseDescription = data.description?.toLowerCase()

                    if (lowercaseTitle?.contains(lowercaseQuery.orEmpty()) == true ||
                        lowercaseDescription?.contains(lowercaseQuery.orEmpty()) == true
                    ) {
                        temp.add(data)
                    }
                }
                adapter = NoteAdapter(temp, this@NotesApp,mustToolDatabase,notesViewModel)
                notesRecycler.adapter = adapter
                return true
            }
        })
    }

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    private fun showDialog() {
        var builder = AlertDialog.Builder(this@NotesApp).setView(R.layout.add_note_dialog).show()
        var titleEdt = builder.findViewById<EditText>(R.id.titleEdt)
        var desEdt = builder.findViewById<EditText>(R.id.desEdt)

        var cancelBtn = builder.findViewById<Button>(R.id.cancelBtn)
        var saveBtn = builder.findViewById<Button>(R.id.saveBtn)

        cancelBtn.setOnClickListener {
            builder.dismiss()
        }

        saveBtn.setOnClickListener {
            var title = titleEdt.text.toString()
            var description = desEdt.text.toString()
            if (title.isEmpty()) {
                titleEdt.error = "Enter title."
            } else if (description.isEmpty()) {
                desEdt.error = "Enter description."
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    var date = SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(Date())
                    var data=Note(0, title, description, date)
                    notesViewModel.putNotes(data) // uploading Notes.
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                }
                builder.dismiss()
                preventShowingKeyboard(this)
            }
        }
    }
}