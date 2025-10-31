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

    // 🔹 Fetch a single note by ID
    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Int): NoteEntity?

    // 🔹 Insert a new note
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    // 🔹 Update an existing note
    @Update
    suspend fun updateNote(note: NoteEntity)

    // 🔹 Delete a specific note
    @Delete
    suspend fun deleteNote(note: NoteEntity)

    // 🔹 Delete all notes (optional helper)
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}
