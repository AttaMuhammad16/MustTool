package com.musttool.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.musttool.MustToolDatabase
import com.musttool.Notes.Note
import com.musttool.R
import com.musttool.activities.BarCodeScanner
import com.musttool.activities.FullNoteActivity
import com.musttool.activities.GenQRActivity
import com.musttool.activities.NotesApp
import com.musttool.activities.TemperatureConverter
import com.musttool.activities.WhatsAppActivity
import com.musttool.models.AppModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class NoteAdapter(var list: ArrayList<Note>, var context: Context) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var selectedColor: Int = 0 // Initialize with a default color
    private val itemColors = intArrayOf(
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
    lateinit var mustToolDatabase: MustToolDatabase
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title=itemView.findViewById<TextView>(R.id.titleTextView)
        var description=itemView.findViewById<TextView>(R.id.descriptionTextView)
        var datetv=itemView.findViewById<TextView>(R.id.datetv)
        var ln=itemView.findViewById<LinearLayout>(R.id.ln)
        var menuImg=itemView.findViewById<ImageView>(R.id.menuImg)
        var card=itemView.findViewById<MaterialCardView>(R.id.noteItemLayoutParent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.note_item_row, parent, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        mustToolDatabase=MustToolDatabase.getInstance(context)
        var data=list[position]
        holder.datetv.isSelected=true
        holder.title.text=data.title
        holder.description.text=data.description
        holder.datetv.text=data.date

        val colorIndex = position % itemColors.size
        val backgroundColor = context.resources.getColor(itemColors[colorIndex])
        holder.ln.setBackgroundColor(backgroundColor)

        var noteData:Note=Note()
        holder.menuImg.setOnClickListener {
            val popupMenu = PopupMenu(context, it)
            popupMenu.menuInflater.inflate(R.menu.show_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        R.id.update -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                noteData=mustToolDatabase.noteDao().getById(list[position].id)
                            }

                            var builder= AlertDialog.Builder(context).setView(R.layout.update_note_dialog).show()

                            var titleEdt=builder.findViewById<EditText>(R.id.titleEdt)
                            var desEdt=builder.findViewById<EditText>(R.id.desEdt)

                            var cancelBtn=builder.findViewById<Button>(R.id.cancelBtn)
                            var saveBtn=builder.findViewById<Button>(R.id.saveBtn)

                            cancelBtn.setOnClickListener {
                                builder.dismiss()
                            }
                            titleEdt.setText(noteData.title)
                            desEdt.setText(noteData.description)

                            saveBtn.setOnClickListener {
                                var title=titleEdt.text.toString()
                                var description=desEdt.text.toString()
                                var id=list[position].id
                                var date=list[position].date
                                if (title.isEmpty()){
                                    titleEdt.error="Enter title."
                                }else if (description.isEmpty()){
                                    desEdt.error="Enter description."
                                }else{
                                    CoroutineScope(Dispatchers.IO).launch {
                                        mustToolDatabase.noteDao().update(Note(id,title,description,date))
                                    }
                                    builder.dismiss()
                                }
                            }

                            return true
                        }

                        R.id.delete -> {
                            val alertDialog = AlertDialog.Builder(context)
                            alertDialog.setTitle("Alert?")
                            alertDialog.setMessage("Are you sure you want to delete this item?")
                            alertDialog.setPositiveButton("Delete") { dialog, _ ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    var title=list[position].title
                                    var des=list[position].description
                                    var id=list[position].id
                                    var date=list[position].date
                                    mustToolDatabase.noteDao().delete(Note(id,title,des,date))
                                }
                                dialog.dismiss()
                            }
                            alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            alertDialog.show()
                            return true
                        }
                        else -> return false
                    }
                }
            })
            popupMenu.show()
        }

        holder.card.setOnClickListener {
            selectedColor = backgroundColor
            var intent=Intent(context,FullNoteActivity::class.java)
            intent.putExtra("title",data.title)
            intent.putExtra("des",data.description)
            intent.putExtra("date",data.date)
            intent.putExtra("color",selectedColor)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


}