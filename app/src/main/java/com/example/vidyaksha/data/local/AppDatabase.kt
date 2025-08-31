package com.example.vidyaksha.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vidyaksha.domain.model.Session
import com.example.vidyaksha.domain.model.Subject
import com.example.vidyaksha.domain.model.Task

@Database(
    entities = [Subject::class, Session::class, Task::class],
    version = 1
)
@TypeConverters(ColorListConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun subjectDao(): SubjectDao

    abstract fun taskDao(): TaskDao

    abstract fun sessionDao(): SessionDao
}