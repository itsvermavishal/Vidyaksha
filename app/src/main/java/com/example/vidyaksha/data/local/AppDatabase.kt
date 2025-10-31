package com.example.vidyaksha.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vidyaksha.domain.model.Session
import com.example.vidyaksha.domain.model.Subject
import com.example.vidyaksha.domain.model.Task

/**
 * Main Room Database for Vidyaksha app.
 * Handles all entities — subjects, sessions, tasks, and notes.
 */
@Database(
    entities = [
        Subject::class,
        Session::class,
        Task::class,
        NoteEntity::class
    ],
    version = 4, // 🔺 bumped version since NoteEntity changed
    exportSchema = false
)
@TypeConverters(
    ColorListConverter::class,
    Converters::class // 🔹 Added support for optional media URI List (if used)
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao
    abstract fun sessionDao(): SessionDao
    abstract fun noteDao(): NoteDao
}
