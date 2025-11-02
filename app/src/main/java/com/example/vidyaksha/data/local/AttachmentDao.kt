package com.example.vidyaksha.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: AttachmentEntity): Long

    @Query("SELECT * FROM attachments WHERE noteId = :noteId")
    suspend fun getAttachmentsForNote(noteId: Long): List<AttachmentEntity>

    @Query("DELETE FROM attachments WHERE id IN (:ids)")
    suspend fun deleteAttachmentsByIds(ids: List<Long>)
}
