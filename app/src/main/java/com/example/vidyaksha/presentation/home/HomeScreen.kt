// file: HomeScreen.kt
package com.example.vidyaksha.presentation.home

import com.example.vidyaksha.presentation.notes.AddEditNoteScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.NoteEntity
import com.example.vidyaksha.presentation.notes.AddEditNoteScreen
import com.example.vidyaksha.presentation.components.BrainfireCard
import com.example.vidyaksha.presentation.notes.AnimatedVerticalNoteList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import java.time.LocalDate

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    val dailyFacts = remember { getDailyFacts() }
    val todayIndex = LocalDate.now().dayOfYear % dailyFacts.size
    val currentFact = dailyFacts[todayIndex]

    var showEditor by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<NoteEntity?>(null) }

    val archivoBlack = FontFamily(Font(R.font.archivo_black))

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingNote = null
                    showEditor = true
                },
                containerColor = Color(0xFF7C4DFF),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF9E3), Color(0xFFF8F8FF))
                )
            )
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // --- Header ---
            BannerHeaderSmall(archivoBlack)
            Spacer(modifier = Modifier.height(12.dp))

            // --- Original BrainfireCard (unchanged) ---
            BrainfireCard(fact = currentFact)

            Spacer(modifier = Modifier.height(12.dp))
            ContinueLearningBar(progress = 0.62f, modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Your Notes",
                fontSize = 22.sp,
                color = Color(0xFF222222),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))

            if (notes.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "✨ No notes yet — tap + to add your first thought!",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                AnimatedVerticalNoteList(
                    notes = notes,
                    onOpen = { note ->
                        editingNote = note
                        showEditor = true
                    },
                    onDelete = { note ->
                        viewModel.deleteNote(note)
                    }
                )
            }

            Spacer(modifier = Modifier.height(100.dp)) // bottom space for FAB
        }

        // --- Fullscreen Note Editor ---
        if (showEditor) {
            AddEditNoteOverlay(
                existingNote = editingNote,
                onSave = { title, content ->
                    if (editingNote == null) {
                        viewModel.addNote(title, content)
                    } else {
                        viewModel.updateNote(editingNote!!.copy(title = title, content = content))
                    }
                    showEditor = false
                },
                onCancel = { showEditor = false }
            )
        }
    }
}

@Composable
private fun BannerHeaderSmall(font: FontFamily) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Soul breathing?",
            fontFamily = font,
            color = Color(0xFF444444).copy(alpha = 0.85f),
            fontSize = 36.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Then keep learning! ☠️",
            fontFamily = font,
            color = Color(0xFF777777),
            fontSize = 22.sp
        )
    }
}

@Composable
fun ContinueLearningBar(progress: Float, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Continue Learning",
            fontSize = 18.sp,
            color = Color(0xFF444444),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50)),
            color = Color(0xFF7C4DFF),
            trackColor = Color(0xFFE0E0E0)
        )
    }
}

@Composable
private fun AddEditNoteOverlay(
    existingNote: NoteEntity?,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        color = Color.White.copy(alpha = 0.98f),
        modifier = Modifier.fillMaxSize()
    ) {
        AddEditNoteScreen(
            existingNote = existingNote,
            onSave = { title, content -> onSave(title, content) },
            onBack = onCancel
        )
    }
}

fun getDailyFacts(): List<String> {
    return listOf(
        "Learning every day compounds into mastery.",
        "Curiosity keeps your brain young.",
        "Consistency is a form of intelligence.",
        "Knowledge compounds faster than money.",
        "The mind grows by feeding it ideas."
    )
}
