// file: presentation/notes/AddEditNoteScreen.kt
package com.example.vidyaksha.presentation.notes

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.AttachmentEntity
import com.example.vidyaksha.data.local.AttachmentType
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.presentation.notes.components.AttachmentGrid
import com.example.vidyaksha.presentation.notes.utils.guessMime
import com.example.vidyaksha.presentation.notes.utils.openAttachment
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEditNoteScreen(
    existingNote: NoteEntity? = null,
    existingAttachments: List<AttachmentEntity> = emptyList(),
    onSaveWithAttachments: suspend (NoteEntity, List<AttachmentEntity>, List<AttachmentEntity>) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf(TextFieldValue(existingNote?.title ?: "")) }
    var content by remember { mutableStateOf(TextFieldValue(existingNote?.content ?: "")) }

    val attachments = remember { mutableStateListOf<AttachmentEntity>().apply { addAll(existingAttachments) } }
    val toInsert = remember { mutableStateListOf<AttachmentEntity>() }
    val toDelete = remember { mutableStateListOf<AttachmentEntity>() }

    var showAddLinkDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    /* ----------------- Launchers ----------------- */

    fun handleUriPicked(uri: Uri?, type: AttachmentType, compress: Boolean) {
        uri ?: return
        try {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        } catch (_: Exception) { }

        coroutineScope.launch {
            try {
                val (path, name, mime) = saveUriAsFile(context, uri, compress)
                val att = AttachmentEntity(
                    id = 0L,
                    noteId = existingNote?.id ?: 0L,
                    uri = uri.toString(),
                    type = type,
                    filePath = path,
                    fileName = name,
                    mimeType = mime
                )
                attachments.add(att)
                toInsert.add(att)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        handleUriPicked(uri, AttachmentType.IMAGE, true)
    }
    val pickAudioLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        handleUriPicked(uri, AttachmentType.AUDIO, false)
    }
    val pickVideoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        handleUriPicked(uri, AttachmentType.VIDEO, false)
    }
    val pickFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        handleUriPicked(uri, AttachmentType.FILE, false)
    }

    /* ----------------- Scaffold ----------------- */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingNote == null) "Add Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        coroutineScope.launch {
                            val now = System.currentTimeMillis()
                            val updated = NoteEntity(
                                id = existingNote?.id ?: 0L,
                                title = title.text,
                                content = content.text,
                                createdAt = existingNote?.createdAt ?: now,
                                updatedAt = now
                            )
                            onSaveWithAttachments(updated, toInsert.toList(), toDelete.toList())
                            onBack()
                        }
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

// Content box styled like the first screenshot
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp)
            ) {
                val innerScrollState = rememberScrollState()
                val coroutineScope = rememberCoroutineScope()

                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        coroutineScope.launch {
                            // Always follow the last line (bottom)
                            innerScrollState.animateScrollTo(innerScrollState.maxValue)
                        }
                    },
                    placeholder = { Text("Write something...") },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(innerScrollState),
                    shape = MaterialTheme.shapes.medium,
                    maxLines = Int.MAX_VALUE
                )

            }



            if (attachments.isNotEmpty()) {
                AttachmentGrid(
                    attachments = attachments,
                    onRemove = { att ->
                        attachments.remove(att)
                        if (att.id != 0L) toDelete.add(att)
                        toInsert.remove(att)
                    },
                    onOpen = { att ->
                        openAttachment(context, att.uri, guessMime(att.type.name))
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { pickImageLauncher.launch(arrayOf("image/*")) }) {
                    Icon(painterResource(R.drawable.outline_add_photo_alternate_24), contentDescription = "Image")
                }
                IconButton(onClick = { pickAudioLauncher.launch(arrayOf("audio/*")) }) {
                    Icon(painterResource(R.drawable.outline_music_note_24), contentDescription = "Audio")
                }
                IconButton(onClick = { pickVideoLauncher.launch(arrayOf("video/*")) }) {
                    Icon(painterResource(R.drawable.outline_slow_motion_video_24), contentDescription = "Video")
                }
                IconButton(onClick = { pickFileLauncher.launch(arrayOf("application/pdf", "*/*")) }) {
                    Icon(painterResource(R.drawable.outline_upload_file_24), contentDescription = "File")
                }
                IconButton(onClick = { showAddLinkDialog = true }) {
                    Icon(painterResource(R.drawable.outline_add_link_24), contentDescription = "Link")
                }
            }
        }
    }

    if (showAddLinkDialog) {
        AddLinkDialog(
            onDismiss = { showAddLinkDialog = false },
            onAdd = { text, url ->
                val token = "$text ($url)"
                val newText = content.text + if (content.text.isEmpty()) token else "\n$token"
                content = TextFieldValue(newText, TextRange(newText.length))
                showAddLinkDialog = false
            }
        )
    }
}


/* ----------------- Add Link Dialog ----------------- */

@Composable
fun AddLinkDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onAdd(text, url) }) { Text("Add") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
        title = { Text("Add Link") },
        text = {
            Column {
                OutlinedTextField(value = text, onValueChange = { text = it }, label = { Text("Display Text") })
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL") })
            }
        }
    )
}

/* ----------------- File Utilities ----------------- */

suspend fun saveUriAsFile(context: Context, uri: Uri, compress: Boolean): Triple<String, String, String> {
    val cr = context.contentResolver
    val mime = cr.getType(uri) ?: "application/octet-stream"
    val name = cr.query(uri, arrayOf(android.provider.OpenableColumns.DISPLAY_NAME), null, null, null)?.use {
        if (it.moveToFirst()) it.getString(0) else null
    } ?: "file_${System.currentTimeMillis()}"

    val dir = File(context.filesDir, "attachments").apply { if (!exists()) mkdirs() }
    val out = File(dir, "${UUID.randomUUID()}_$name")

    cr.openInputStream(uri)?.use { input ->
        if (compress && mime.startsWith("image/")) {
            val bmp = BitmapFactory.decodeStream(input)
            val scaled = bmp?.let {
                if (it.width > 1200) {
                    val ratio = 1200f / it.width
                    Bitmap.createScaledBitmap(it, 1200, (it.height * ratio).toInt(), true)
                } else it
            }
            if (scaled != null) {
                FileOutputStream(out).use { scaled.compress(Bitmap.CompressFormat.JPEG, 85, it) }
            }
        } else {
            FileOutputStream(out).use { input.copyTo(it) }
        }
    }

    return Triple(out.absolutePath, name, mime)
}

