// file: data/local/AttachmentEntity.kt
package com.example.vidyaksha.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attachments")
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val noteId: Long,
    val uri: String,
    val type: AttachmentType,
    val filePath: String,
    val fileName: String,
    val mimeType: String
)

