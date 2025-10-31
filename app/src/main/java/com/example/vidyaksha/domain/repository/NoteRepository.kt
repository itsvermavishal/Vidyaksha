package com.example.vidyaksha.domain.repository

import com.example.vidyaksha.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    /** Get all notes ordered by id DESC */
    fun getAllNotes(): Flow<List<NoteEntity>>

    /** Insert a new note */
    suspend fun addNote(title: String, content: String)

    /** Update existing note */
    suspend fun updateNote(note: NoteEntity)

    /** Delete a note */
    suspend fun deleteNote(note: NoteEntity)
}
