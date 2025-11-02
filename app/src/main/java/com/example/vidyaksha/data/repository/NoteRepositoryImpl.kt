package com.example.vidyaksha.data.repository

import com.example.vidyaksha.data.local.AttachmentEntity
import com.example.vidyaksha.data.local.NoteDao
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.data.local.NoteWithAttachments
import com.example.vidyaksha.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> = dao.getAllNotes()

    override suspend fun addNote(title: String, content: String) {
        val note = NoteEntity(
            title = title,
            content = content,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertNote(note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        dao.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteNote(note: NoteEntity) {
        dao.deleteNote(note)
    }

    override suspend fun saveNoteWithAttachments(
        title: String,
        contentWithTokens: String,
        attachmentsToInsert: List<AttachmentEntity>
    ): Long {
        val note = NoteEntity(
            title = title,
            content = contentWithTokens,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        val noteId = dao.insertNote(note)
        if (attachmentsToInsert.isNotEmpty()) {
            dao.insertAttachments(
                attachmentsToInsert.map { it.copy(noteId = noteId) }
            )
        }
        return noteId
    }

    override suspend fun updateNoteWithAttachments(
        note: NoteEntity,
        toInsert: List<AttachmentEntity>,
        toDelete: List<AttachmentEntity>
    ) {
        dao.updateNote(note.copy(updatedAt = System.currentTimeMillis()))
        if (toInsert.isNotEmpty()) dao.insertAttachments(toInsert.map { it.copy(noteId = note.id) })
        if (toDelete.isNotEmpty()) dao.deleteAttachments(toDelete)
    }

    override suspend fun getNoteWithAttachments(id: Long): NoteWithAttachments? {
        return dao.getNoteWithAttachments(id)
    }
}
