package com.musttool.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.musttool.MustToolDatabase
import com.musttool.Notes.Note
import com.musttool.R
import com.musttool.adapters.NoteAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class NotesApp : AppCompatActivity() {
    lateinit var mustToolDatabase: MustToolDatabase
    lateinit var addBtn:FloatingActionButton
    lateinit var notesRecycler:RecyclerView
    lateinit var list: ArrayList<Note>
    lateinit var adapter:NoteAdapter
    lateinit var searchView: SearchView
    lateinit var temp:ArrayList<Note>
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_app)
        list= ArrayList()
        temp= ArrayList()
        mustToolDatabase=MustToolDatabase.getInstance(this@NotesApp)
        window.statusBarColor = ContextCompat.getColor(this, R.color.temperatureActivityColor)
        addBtn=findViewById(R.id.addBtn)
        notesRecycler=findViewById(R.id.notesRecycler)
        searchView=findViewById(R.id.searchView)

        addBtn.setOnClickListener {
            showDialog()
        }

        CoroutineScope(Dispatchers.IO).launch {
            mustToolDatabase.noteDao().get()?.collect{ it->
                list.clear()
                it.forEach {
                    list.add(it)
                }
                withContext(Dispatchers.Main){
                    adapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch {
            adapter= NoteAdapter(list,this@NotesApp)
            notesRecycler.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            notesRecycler.adapter=adapter
            adapter.notifyDataSetChanged()
        }
        searchView.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN && !searchView.isIconified) {
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
                adapter = NoteAdapter(temp, this@NotesApp)
                notesRecycler.adapter = adapter
                return true
            }
        })




    }

    @SuppressLint("SimpleDateFormat")
    private fun showDialog() {
        var builder= AlertDialog.Builder(this@NotesApp).setView(R.layout.add_note_dialog).show()
        var titleEdt=builder.findViewById<EditText>(R.id.titleEdt)
        var desEdt=builder.findViewById<EditText>(R.id.desEdt)

        var cancelBtn=builder.findViewById<Button>(R.id.cancelBtn)
        var saveBtn=builder.findViewById<Button>(R.id.saveBtn)

        cancelBtn.setOnClickListener {
            builder.dismiss()
        }

        saveBtn.setOnClickListener {
            var title=titleEdt.text.toString()
            var description=desEdt.text.toString()
            if (title.isEmpty()){
                titleEdt.error="Enter title."
            }else if (description.isEmpty()){
                desEdt.error="Enter description."
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    var date=SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(Date())
                    mustToolDatabase.noteDao().insert(Note(0,title,description,date))
                }
                builder.dismiss()
            }
        }

    }
}