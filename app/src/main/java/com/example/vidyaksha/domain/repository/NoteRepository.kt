package com.example.vidyaksha.domain.repository

import com.example.vidyaksha.data.local.AttachmentEntity
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.data.local.NoteWithAttachments
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun addNote(title: String, content: String)

    suspend fun updateNote(note: NoteEntity)

    suspend fun deleteNote(note: NoteEntity)

    suspend fun saveNoteWithAttachments(
        title: String,
        contentWithTokens: String,
        attachmentsToInsert: List<AttachmentEntity>
    ): Long

    suspend fun updateNoteWithAttachments(
        note: NoteEntity,
        toInsert: List<AttachmentEntity>,
        toDelete: List<AttachmentEntity>
    )

    suspend fun getNoteWithAttachments(id: Long): NoteWithAttachments?
}
