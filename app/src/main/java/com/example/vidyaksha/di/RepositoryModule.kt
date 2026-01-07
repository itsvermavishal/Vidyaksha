package com.example.vidyaksha.di

import com.example.vidyaksha.data.local.ContentRepository
import com.example.vidyaksha.data.repository.ContentRepositoryImpl
import com.example.vidyaksha.data.repository.NoteRepositoryImpl
import com.example.vidyaksha.data.repository.SessionRepositoryImpl
import com.example.vidyaksha.data.repository.SubjectRepositoryImpl
import com.example.vidyaksha.data.repository.TaskRepositoryImpl
import com.example.vidyaksha.domain.repository.NoteRepository
import com.example.vidyaksha.domain.repository.SessionRepository
import com.example.vidyaksha.domain.repository.SubjectRepository
import com.example.vidyaksha.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSubjectRepository(
        impl: SubjectRepositoryImpl
    ): SubjectRepository

    @Singleton
    @Binds
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindSessionRepository(
        impl: SessionRepositoryImpl
    ): SessionRepository

    @Singleton
    @Binds
    abstract fun bindNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository


    @Binds
    abstract fun bindContentRepository(
        impl: ContentRepositoryImpl
    ): ContentRepository

}
