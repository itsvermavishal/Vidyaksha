package com.example.vidyaksha.presentation.notes

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidyaksha.data.local.AttachmentEntity
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    // existing notes flow...
    private val _pendingAttachments = mutableStateListOf<AttachmentEntity>() // attachments added during editing but not yet persisted
    val pendingAttachments: List<AttachmentEntity> = _pendingAttachments

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

    fun addPendingAttachment(att: AttachmentEntity) {
        _pendingAttachments.add(att)
    }

    fun removePendingAttachment(att: AttachmentEntity) {
        _pendingAttachments.remove(att)
    }

    fun clearPendingAttachments() {
        _pendingAttachments.clear()
    }

    fun saveNote(title: String, content: String, existingNoteId: Long? = null) {
        viewModelScope.launch {
            if (existingNoteId == null) {
                noteRepository.saveNoteWithAttachments(title, content, _pendingAttachments.toList())
            } else {
                val note = NoteEntity(
                    id = existingNoteId,
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                // for simplicity not handling deletions here (you should track attachments removed)
                noteRepository.updateNoteWithAttachments(
                    note = note,
                    toInsert = _pendingAttachments.toList(),
                    toDelete = emptyList()
                )
            }
            clearPendingAttachments()
        }
    }

}
