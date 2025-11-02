package com.example.vidyaksha.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteCard(
    title: String,
    description: String,
    backgroundColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // Generate a pleasant random gradient each time
    val gradientColors = listOf(
        listOf(Color(0xFF7C4DFF), Color(0xFFB388FF)), // Deep Purple
        listOf(Color(0xFF00BCD4), Color(0xFF80DEEA)), // Cyan
        listOf(Color(0xFFFF7043), Color(0xFFFFAB91)), // Deep Orange
        listOf(Color(0xFF66BB6A), Color(0xFFA5D6A7)), // Green
        listOf(Color(0xFFFFC107), Color(0xFFFFE082)), // Amber
        listOf(Color(0xFF42A5F5), Color(0xFF90CAF9)), // Blue
        listOf(Color(0xFFE91E63), Color(0xFFF48FB1)), // Pink
        listOf(Color(0xFF9C27B0), Color(0xFFE1BEE7)), // Purple
        listOf(Color(0xFF009688), Color(0xFF80CBC4)), // Teal
        listOf(Color(0xFFEF5350), Color(0xFFEF9A9A)), // Red
        listOf(Color(0xFF26C6DA), Color(0xFFB2EBF2)), // Light Blue Cyan
        listOf(Color(0xFF8BC34A), Color(0xFFC5E1A5)), // Light Green
        listOf(Color(0xFFAB47BC), Color(0xFFCE93D8)), // Violet
        listOf(Color(0xFFFFA726), Color(0xFFFFCC80)), // Orange
        listOf(Color(0xFF5C6BC0), Color(0xFF9FA8DA)), // Indigo
        listOf(Color(0xFF26A69A), Color(0xFF80CBC4)), // Sea Green
        listOf(Color(0xFFD81B60), Color(0xFFF48FB1)), // Rose
        listOf(Color(0xFF29B6F6), Color(0xFF81D4FA)), // Sky Blue
        listOf(Color(0xFF7E57C2), Color(0xFFB39DDB)), // Lavender
        listOf(Color(0xFFEC407A), Color(0xFFF48FB1))  // Fuchsia
    )

// Pick a random gradient from the list
    val randomGradient = remember { gradientColors.random() }

    val gradient = Brush.linearGradient(
        colors = listOf(
            randomGradient[0].copy(alpha = 0.9f),
            randomGradient[1].copy(alpha = 0.7f)
        )
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(brush = gradient)
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF1A1A1A)
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF5E35B1)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFE53935)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 15.sp,
                color = Color(0xFF333333),
                lineHeight = 20.sp
            )
        }
    }
}
