package com.example.vidyaksha.data.repository

import com.example.vidyaksha.data.local.NoteDao
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> =
        noteDao.getAllNotes()

    override suspend fun addNote(title: String, content: String) {
        noteDao.insertNote(NoteEntity(title = title, content = content))
    }

    override suspend fun updateNote(note: NoteEntity) =
        noteDao.updateNote(note)

    override suspend fun deleteNote(note: NoteEntity) =
        noteDao.deleteNote(note)
}
