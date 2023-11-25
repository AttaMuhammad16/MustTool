package com.musttool

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.musttool.Notes.Note
import com.musttool.Notes.NotesDAO
import javax.inject.Inject

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class MustToolDatabase:RoomDatabase() {
    abstract fun noteDao() : NotesDAO
}

