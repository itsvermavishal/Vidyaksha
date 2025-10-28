package com.example.vidyaksha.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BrainfireCard(fact: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .heightIn(min = 130.dp)
    ) {
        // Dashed outline (outer)
        Canvas(modifier = Modifier.matchParentSize()) {
            val stroke = Stroke(
                width = 2.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), 0f)
            )
            drawRoundRect(
                color = Color.LightGray,
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(28.dp.toPx()),
                style = stroke
            )
        }

        // Golden background centered with equal padding
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = Color(0xFFD7C39A), // golden tone
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f) // keep centered proportionally
                .align(Alignment.Center)
                .padding(horizontal = 14.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fact,
                    color = Color(0xFF1A1A1A),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "~ Today's fact",
                    color = Color(0xFF4B4B4B),
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        // Floating Brainfire badge (slightly above dashed line)
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = 18.dp, y = (-16).dp) // moved slightly up
                .background(Color(0xFFBBDEFB), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Brainfire",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
