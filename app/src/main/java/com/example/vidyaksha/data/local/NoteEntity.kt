package com.example.vidyaksha.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single note saved in the local Room database.
 * Updated to support timestamps & optional media attachments.
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val title: String = "",

    val content: String = "",

    // Optional — store local URIs (images, videos, audio) as comma-separated string
    val mediaUris: String? = null,

    // Optional — creation & last updated timestamps (for sorting or syncing)
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
