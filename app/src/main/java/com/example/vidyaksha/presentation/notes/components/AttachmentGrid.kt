package com.example.vidyaksha.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.AttachmentEntity
import com.example.vidyaksha.data.local.AttachmentType

@Composable
fun AttachmentGrid(
    attachments: List<AttachmentEntity>,
    onRemove: (AttachmentEntity) -> Unit = {},
    onOpen: (AttachmentEntity) -> Unit = {}
) {
    val context = LocalContext.current

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(attachments, key = { it.id }) { att ->
            Card(
                modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
                    .clickable { onOpen(att) }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when (att.type) {
                        AttachmentType.IMAGE -> {
                            AsyncImage(
                                model = att.uri,
                                contentDescription = att.fileName,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }

                        AttachmentType.AUDIO -> {
                            Icon(
                                painter = painterResource(R.drawable.outline_music_note_24),
                                contentDescription = "Audio",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        AttachmentType.VIDEO -> {
                            Icon(
                                painter = painterResource(R.drawable.outline_slow_motion_video_24),
                                contentDescription = "Video",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        AttachmentType.FILE -> {
                            Icon(
                                painter = painterResource(R.drawable.outline_upload_file_24),
                                contentDescription = "File",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        AttachmentType.LINK -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Link", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    // delete button
                    IconButton(
                        onClick = { onRemove(att) },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}
