package com.example.vidyaksha.presentation.home

import com.example.vidyaksha.data.local.AttachmentEntity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.domain.repository.NoteRepository
import com.example.vidyaksha.domain.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    subjectRepository: SubjectRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

    val subjects = subjectRepository
        .getAllSubjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val notes = noteRepository
        .getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addNote(title: String, content: String) = viewModelScope.launch {
        noteRepository.addNote(title, content)
    }

    fun updateNote(note: NoteEntity) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }

    fun deleteNote(note: NoteEntity) = viewModelScope.launch {
        noteRepository.deleteNote(note)
    }

    fun addNoteWithAttachments(note: NoteEntity, attachments: List<AttachmentEntity>) {
        viewModelScope.launch {
            noteRepository.saveNoteWithAttachments(
                title = note.title,
                contentWithTokens = note.content,
                attachmentsToInsert = attachments
            )
        }
    }

    fun updateNoteWithAttachments(
        note: NoteEntity,
        toInsert: List<AttachmentEntity>,
        toDelete: List<AttachmentEntity>
    ) {
        viewModelScope.launch {
            noteRepository.updateNoteWithAttachments(
                note = note,
                toInsert = toInsert,
                toDelete = toDelete
            )

        }
    }

}
