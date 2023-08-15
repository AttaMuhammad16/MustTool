package com.musttool.Notes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)

    @Query("DELETE FROM Note")
    suspend fun deleteAll(): Int

    @Query("SELECT id,title,description,date FROM Note")
    fun get(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE id = :id")
    fun getById(id:Long): Note


}