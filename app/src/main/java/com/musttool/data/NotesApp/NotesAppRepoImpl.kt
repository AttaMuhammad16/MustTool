package com.musttool.data.NotesApp

import com.musttool.MustToolDatabase
import com.musttool.Notes.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NotesAppRepoImpl @Inject constructor(private val mustToolDatabase: MustToolDatabase):NotesAppRepo {

    override suspend fun getNotes(): Flow<List<Note>> = flow{
            mustToolDatabase.noteDao().get().collect { notes ->
                emit(notes)
        }
    }

    override suspend fun putNotes(note: Note) {
        mustToolDatabase.noteDao().insert(note)
    }

    override suspend fun updateNotes(note: Note) {
        mustToolDatabase.noteDao().update(note)
    }

    override suspend fun deleteNotes(note: Note) {
       mustToolDatabase.noteDao().delete(note)
    }

    override suspend fun getNoteById(id: Long): Note {
        var note=mustToolDatabase.noteDao().getById(id)
        return note
    }

}