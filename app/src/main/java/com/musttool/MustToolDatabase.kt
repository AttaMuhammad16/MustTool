package com.musttool

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.musttool.Notes.Note
import com.musttool.Notes.NotesDAO
import com.musttool.password.openHelperFactory
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class MustToolDatabase:RoomDatabase() {
    abstract fun noteDao() : NotesDAO

    companion object {
        @Volatile
        private var instance: MustToolDatabase? = null
        fun getInstance(context: Context): MustToolDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context): MustToolDatabase {
            return Room.databaseBuilder(context.applicationContext, MustToolDatabase::class.java, "flashcash_database")
//                .openHelperFactory(openHelperFactory)
                .build()
        }
    }
}


object password {
    val openHelperFactory = SupportFactory(SQLiteDatabase.getBytes("!@@##$%%QWEERRTT/.,><~_++".toCharArray()))
}
