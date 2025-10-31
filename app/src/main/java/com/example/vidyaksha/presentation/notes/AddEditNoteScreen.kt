package com.example.vidyaksha.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vidyaksha.data.local.NoteEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    existingNote: NoteEntity? = null,
    onSave: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(existingNote?.title ?: "")) }
    var content by remember { mutableStateOf(TextFieldValue(existingNote?.content ?: "")) }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existingNote == null) "Add Note" else "Edit Note",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { onSave(title.text, content.text) }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Title Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content Box with internal scroll and auto-scroll
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Transparent)
            ) {
                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        // Detect typing direction to adjust scroll
                        val prevLength = content.text.length
                        content = it

                        coroutineScope.launch {
                            if (it.text.length > prevLength) {
                                // User typing forward → scroll upward if near bottom
                                if (scrollState.value >= scrollState.maxValue - 200) {
                                    scrollState.animateScrollTo(
                                        (scrollState.value + 80).coerceAtMost(scrollState.maxValue)
                                    )
                                }
                            } else if (it.text.length < prevLength) {
                                // User deleting → scroll downward if near top
                                if (scrollState.value <= 100) {
                                    scrollState.animateScrollTo(
                                        (scrollState.value - 80).coerceAtLeast(0)
                                    )
                                }
                            }
                        }
                    },
                    label = { Text("Write something...") },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF7F7F7),
                        unfocusedContainerColor = Color(0xFFF9F9F9)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Media action icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: launch image picker */ }) {
                    Icon(Icons.Default.Person, contentDescription = "Add Photo")
                }
                IconButton(onClick = { /* TODO: launch audio recorder */ }) {
                    Icon(Icons.Default.Call, contentDescription = "Add Audio")
                }
                IconButton(onClick = { /* TODO: attach link */ }) {
                    Icon(Icons.Default.Lock, contentDescription = "Add Link")
                }
                IconButton(onClick = { /* TODO: other file */ }) {
                    Icon(Icons.Default.List, contentDescription = "Attach")
                }
            }

            Text(
                text = "Attachments will be shown here (UI stub).",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
