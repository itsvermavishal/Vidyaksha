package com.example.vidyaksha.presentation.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import com.example.vidyaksha.R

/* ---------- UI MODEL ---------- */
data class CardItem(
    val imageRes: Int,
    val heading: String,
    val description: String = ""
)

/* ---------- PAGER ---------- */
@Composable
fun SwipeableCardPager(
    title: String,
    cards: List<CardItem>,
    pagerHeight: Dp = 360.dp,
    enableGestures: Boolean = true
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var dragOffset by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .pointerInput(enableGestures) {
                    if (enableGestures) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (dragOffset < -120 && currentIndex < cards.lastIndex)
                                    currentIndex++
                                else if (dragOffset > 120 && currentIndex > 0)
                                    currentIndex--
                                dragOffset = 0f
                            }
                        ) { change, dragAmount ->
                            dragOffset += dragAmount
                            change.consume()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            val scale by animateFloatAsState(
                targetValue = 1f - (abs(dragOffset) / 2000f).coerceIn(0f, 0.08f),
                label = "scale"
            )

            val translateX by animateFloatAsState(
                targetValue = dragOffset * 0.2f,
                label = "translate"
            )

            val item = cards[currentIndex]

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationX = translateX
                    },
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column {

                    // ✅ SAFE IMAGE RENDERING
                    if (item.imageRes != R.drawable.placeholder_image) {
                        Image(
                            painter = painterResource(item.imageRes),
                            contentDescription = item.heading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(pagerHeight * 0.55f),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = item.heading,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }


            if (currentIndex > 0) {
                IconButton(
                    onClick = { currentIndex-- },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painterResource(id = com.example.vidyaksha.R.drawable.ic_arrow_left),
                        contentDescription = null,
                        tint = Color(0xFF6C63FF)
                    )
                }
            }

            if (currentIndex < cards.lastIndex) {
                IconButton(
                    onClick = { currentIndex++ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painterResource(id = com.example.vidyaksha.R.drawable.ic_arrow_right),
                        contentDescription = null,
                        tint = Color(0xFF6C63FF)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text("${currentIndex + 1} / ${cards.size}", color = Color.Gray)
    }
}
