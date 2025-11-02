// file: presentation/notes/RenderNoteContent.kt
package com.example.vidyaksha.presentation.notes

import com.example.vidyaksha.R
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.vidyaksha.data.local.AttachmentEntity
import com.example.vidyaksha.data.local.AttachmentType
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp

@Composable
fun RenderNoteContent(content: String, attachments: List<AttachmentEntity>, modifier: Modifier = Modifier) {
    val ctx = LocalContext.current

    // map placeholders (UUID tokens) to AttachmentEntity by filePath or by id you set after saving
    // We'll match tokens like [img:UUID] where UUID may be fileName or path fragment.
    val parts = tokenizeContent(content) // list of either plain text or token objects

    Column(modifier = modifier.fillMaxWidth()) {
        parts.forEach { part ->
            when (part) {
                is ContentPart.Text -> {
                    // detect links/emails/phones and render clickable spans — simplified: if contains "http", show as clickable Text
                    val text = part.text
                    if (text.contains("http://") || text.contains("https://")) {
                        Text(text = text, color = Color(0xFF1E88E5), textDecoration = TextDecoration.Underline, modifier = Modifier.clickable {
                            // open the first url — for proper handling use Linkify
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(extractFirstUrl(text)))
                            ctx.startActivity(intent)
                        })
                    } else if (text.contains("@")) {
                        Text(text = text, color = Color(0xFF1E88E5), textDecoration = TextDecoration.Underline, modifier = Modifier.clickable {
                            val email = extractFirstEmail(text) ?: return@clickable
                            val i = Intent(Intent.ACTION_SENDTO).apply { data = Uri.parse("mailto:$email") }
                            ctx.startActivity(i)
                        })
                    } else {
                        Text(text = text, fontSize = 16.sp)
                    }
                }
                is ContentPart.Token -> {
                    val token = part.token
                    // Find matching attachment by token id or by heuristic filePath contains token id
                    val att = attachments.firstOrNull { it.filePath.contains(token.id) || it.fileName.contains(token.id) || it.id.toString() == token.id }
                    if (att != null) {
                        when (att.type) {
                            AttachmentType.IMAGE -> {
                                AsyncImage(
                                    model = att.filePath,
                                    contentDescription = att.fileName,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            }
                            AttachmentType.AUDIO -> {
                                // show simple audio player row
                                AudioPlayerRow(uriString = att.filePath)
                            }
                            AttachmentType.FILE -> {
                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)) {
                                    Icon(painter = painterResource(id = R.drawable.outline_upload_file_24), // 👈 your drawable name
                                        contentDescription = "Attach File")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = att.fileName, modifier = Modifier.clickable {
                                        // open with external app
                                        ctx.startActivity(Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(att.filePath)
                                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                        })
                                    })
                                }
                            }
                            else -> { /* links handled in text part if needed */ }
                        }
                    } else {
                        // fallback: show token text
                        Text(part.token.raw, color = Color.Gray)
                    }
                }
            }
        }
    }
}

/* ---- helpers and simple parsers ---- */

private sealed class ContentPart {
    data class Text(val text: String): ContentPart()
    data class Token(val token: TokenModel): ContentPart()
}
private data class TokenModel(val raw: String, val type: String, val id: String)

private fun tokenizeContent(content: String): List<ContentPart> {
    val regex = "\\[(img|aud|file|link):([^\\]]+)]".toRegex()
    val out = mutableListOf<ContentPart>()
    var last = 0
    regex.findAll(content).forEach { m ->
        if (m.range.first > last) {
            out.add(ContentPart.Text(content.substring(last, m.range.first)))
        }
        val type = m.groups[1]!!.value
        val id = m.groups[2]!!.value
        out.add(ContentPart.Token(TokenModel(m.value, type, id)))
        last = m.range.last + 1
    }
    if (last < content.length) out.add(ContentPart.Text(content.substring(last)))
    return out
}

private fun extractFirstUrl(text: String): String {
    val r = "(https?://[\\w\\-._~:/?#[\\]@!$&'()*+,;=%]+)".toRegex()
    return r.find(text)?.value ?: ""
}

private fun extractFirstEmail(text: String): String? {
    val r = "([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})".toRegex()
    return r.find(text)?.value
}
