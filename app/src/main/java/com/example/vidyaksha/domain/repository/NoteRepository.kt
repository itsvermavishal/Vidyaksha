package com.example.vidyaksha.domain.repository

import com.example.vidyaksha.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>
    suspend fun addNote(title: String, content: String)
    suspend fun updateNote(note: NoteEntity)
    suspend fun deleteNote(note: NoteEntity)
}
