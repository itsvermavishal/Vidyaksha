package com.example.vidyaksha.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for Room database.
 * Defines CRUD operations for NoteEntity.
 */
@Dao
interface NoteDao {

    // 🔹 Fetch all notes (latest first)
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    // 🔹 Fetch a note with its attachments (relation)
    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteWithAttachments(id: Long): NoteWithAttachments?

    // 🔹 Fetch a single note by ID
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    // 🔹 Insert a new note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    // 🔹 Update an existing note
    @Update
    suspend fun updateNote(note: NoteEntity)

    // 🔹 Delete a specific note
    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // 🔹 Delete all notes
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()

    // 🔹 Attachments handling
    @Insert
    suspend fun insertAttachment(att: AttachmentEntity): Long

    @Insert
    suspend fun insertAttachments(list: List<AttachmentEntity>): List<Long>

    @Query("DELETE FROM attachments WHERE noteId = :noteId")
    suspend fun deleteAttachmentsForNote(noteId: Long)

    @Query("SELECT * FROM attachments WHERE noteId = :noteId")
    suspend fun getAttachmentsFor(noteId: Long): List<AttachmentEntity>

    @Delete
    suspend fun deleteAttachment(att: AttachmentEntity)

    // ✅ New helper for batch delete (used in repository)
    @Delete
    suspend fun deleteAttachments(attachments: List<AttachmentEntity>)
}
