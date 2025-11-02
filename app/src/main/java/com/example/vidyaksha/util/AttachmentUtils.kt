package com.example.vidyaksha.presentation.notes.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openAttachment(context: Context, uri: String, mimeType: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(uri), mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Open with"))
}

fun guessMime(type: String): String = when (type) {
    "image" -> "image/*"
    "audio" -> "audio/*"
    "video" -> "video/*"
    "pdf" -> "application/pdf"
    else -> "*/*"
}
