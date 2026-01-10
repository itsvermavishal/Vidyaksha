package com.example.vidyaksha.presentation.slide

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.Slide

@Composable
fun SwipeableSlidePager(
    title: String,
    slides: List<Slide>
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val slide = slides[currentIndex]
    val isDark = isSystemInDarkTheme()

    val statusBarBaseColor =
        if (isDark) Color(0xFF020617) else Color(0xFFF1F5FF)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        statusBarBaseColor,
                        Color(0xFFF62F25),
                        Color(0xFFFFF544),
                        Color(0xFFF8FAFF)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* 🔹 TOP TITLE */
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (dragOffset < -120 && currentIndex < slides.lastIndex) {
                                currentIndex++
                            } else if (dragOffset > 120 && currentIndex > 0) {
                                currentIndex--
                            }
                            dragOffset = 0f
                        }
                    ) { change, dragAmount ->
                        dragOffset += dragAmount
                        change.consume()
                    }
                }
        ) {

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SlideBlockRenderer(slide.blocks)
                }
            }

            /* ⬅ PREVIOUS */
            if (currentIndex > 0) {
                IconButton(
                    onClick = { currentIndex-- },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_left),
                        contentDescription = "Previous"
                    )
                }
            }

            /* ➡ NEXT */
            if (currentIndex < slides.lastIndex) {
                IconButton(
                    onClick = { currentIndex++ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_right),
                        contentDescription = "Next"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${currentIndex + 1} / ${slides.size}",
            color = Color.Gray
        )
    }
}
