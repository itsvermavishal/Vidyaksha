package com.example.vidyaksha.presentation.notes

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vidyaksha.data.local.NoteEntity
import kotlin.math.abs
import kotlin.random.Random

@Composable
fun AnimatedVerticalNoteList(
    notes: List<NoteEntity>,
    onOpen: (NoteEntity) -> Unit,
    onDelete: (NoteEntity) -> Unit
) {
    val listState = rememberLazyListState()
    val itemHeightDp = 220.dp

    // 🎨 All possible gradients
    val gradientSets = listOf(
        listOf(Color(0xFFB3E5FC), Color(0xFFE1F5FE)), // Light Blue
        listOf(Color(0xFFFFF9C4), Color(0xFFFFECB3)), // Soft Yellow
        listOf(Color(0xFFC8E6C9), Color(0xFFA5D6A7)), // Mint Green
        listOf(Color(0xFFFFCCBC), Color(0xFFFFAB91)), // Peach
        listOf(Color(0xFFD1C4E9), Color(0xFFB39DDB)), // Lavender
        listOf(Color(0xFFFFE0B2), Color(0xFFFFCC80)), // Warm Orange
        listOf(Color(0xFFFFCDD2), Color(0xFFF8BBD0))  // Pink
    )

    // 🔄 State that re-shuffles gradients every tap
    var colorSeed by remember { mutableStateOf(0) }
    val shuffledGradients = remember(colorSeed) {
        gradientSets.shuffled(Random(colorSeed))
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(top = 4.dp, bottom = 8.dp)

            // 👇 Tap anywhere to change gradients
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { colorSeed = Random.nextInt() }
                )
            },
        contentPadding = PaddingValues(bottom = 72.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(notes, key = { _, note -> note.id }) { index, note ->
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val center = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2

            var rawScale = 0.9f
            val itemInfo = visibleItems.find { it.index == index }
            if (itemInfo != null) {
                val itemCenter = itemInfo.offset + itemInfo.size / 2
                val distance = abs(center - itemCenter).toFloat()
                val maxDistance = layoutInfo.viewportSize.height / 2f + itemInfo.size
                val normalized = (1f - (distance / maxDistance)).coerceIn(0f, 1f)
                rawScale = 0.9f + normalized * 0.2f
            }

            val scale by animateFloatAsState(
                targetValue = rawScale,
                animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
            )

            NoteListItem(
                note = note,
                gradientColors = shuffledGradients[index % shuffledGradients.size],
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeightDp)
                    .scale(scale)
                    .shadow(
                        elevation = if (scale > 1f) 10.dp else 4.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                onOpen = { onOpen(note) },
                onDelete = { onDelete(note) }
            )
        }
    }
}

@Composable
private fun NoteListItem(
    note: NoteEntity,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    // 🟢 Gradient with smooth fade-to-white on the right side
    val blendedGradient = Brush.horizontalGradient(
        colors = listOf(
            gradientColors[0].copy(alpha = 1f),
            gradientColors[1].copy(alpha = 0.9f),
            Color.White.copy(alpha = 0.8f)
        )
    )

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable { onOpen() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blendedGradient)
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = note.title.ifBlank { "Untitled" },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )

                    Row {
                        IconButton(onClick = { onOpen() }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFF5E35B1)
                            )
                        }
                        IconButton(onClick = { onDelete() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFE53935)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = note.content,
                    fontSize = 16.sp,
                    color = Color(0xFF333333),
                    lineHeight = 22.sp,
                    maxLines = 6
                )
            }
        }
    }
}
