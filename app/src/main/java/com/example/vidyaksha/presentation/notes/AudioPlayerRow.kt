// file: presentation/notes/AudioPlayerRow.kt
package com.example.vidyaksha.presentation.notes

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow

@Composable
fun AudioPlayerRow(uriString: String) {
    val ctx = LocalContext.current
    val exo = remember {
        ExoPlayer.Builder(ctx).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(uriString)))
            prepare()
        }
    }

    var playing by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            exo.release()
        }
    }

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.Start) {
        IconButton(onClick = {
            if (playing) { exo.pause(); playing = false } else { exo.play(); playing = true }
        }) {
            Icon(if (playing) Icons.Default.Close else Icons.Default.PlayArrow, contentDescription = null)
        }
        Text(text = "Audio")
        Spacer(modifier = Modifier.weight(1f))
        // TODO: You can add progress bar and time display by listening to exo.currentPosition
    }
}
