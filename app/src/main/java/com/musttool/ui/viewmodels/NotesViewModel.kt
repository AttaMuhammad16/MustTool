package com.musttool.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.musttool.MustToolDatabase
import com.musttool.Notes.Note
import com.musttool.data.NotesApp.NotesAppRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class NotesViewModel @Inject constructor(private val mustToolDatabase: MustToolDatabase,private val notesAppRepo: NotesAppRepo):ViewModel() {

    private var _notes:MutableLiveData<List<Note>> = MutableLiveData()
    val notes:LiveData<List<Note>> = _notes



    fun getNotes(){
        viewModelScope.launch {
            notesAppRepo.getNotes().collect{notes->
                _notes.value=notes
            }
        }
    }

    fun putNotes(note: Note){
        viewModelScope.launch {
            notesAppRepo.putNotes(note)
        }
    }

    fun updateNote(notes:Note){
        viewModelScope.launch {
            notesAppRepo.updateNotes(notes)
        }
    }

    fun deleteNote(notes:Note){
        viewModelScope.launch {
            notesAppRepo.deleteNotes(notes)
        }
    }

    suspend fun getNoteById(id: Long):Note {
        return notesAppRepo.getNoteById(id)
    }

}