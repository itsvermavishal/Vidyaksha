package com.example.vidyaksha.di

import android.app.Application
import androidx.room.Room
import com.example.vidyaksha.data.local.*
import com.example.vidyaksha.data.repository.NoteRepositoryImpl
import com.example.vidyaksha.domain.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "vidyaksha.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides @Singleton
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()

    @Provides @Singleton
    fun provideSubjectDao(database: AppDatabase): SubjectDao = database.subjectDao()

    @Provides @Singleton
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()

    @Provides @Singleton
    fun provideSessionDao(database: AppDatabase): SessionDao = database.sessionDao()

}
