package com.example.vidyaksha.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.vidyaksha.data.local.NoteEntity

@Composable
fun AddOrEditNoteDialog(
    existingNote: NoteEntity?,
    onDismiss: () -> Unit,
    onSave: (title: String, desc: String) -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(existingNote?.title ?: "")) }
    var desc by remember { mutableStateOf(TextFieldValue(existingNote?.content ?: "")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existingNote == null) "Add Note" else "Edit Note") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(title.text, desc.text) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

