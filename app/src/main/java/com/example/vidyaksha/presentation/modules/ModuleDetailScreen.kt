package com.example.vidyaksha.presentation.modules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vidyaksha.R
import com.ramcosta.composedestinations.annotation.Destination

data class ModuleLevel(
    val imageRes: Int,
    val title: String,
    val progress: Int
)

@Destination
@Composable
fun ModuleDetailScreen(
    moduleNumber: Int,
    moduleTitle: String
) {
    val description =
        "Detailed description for $moduleTitle. This can be very long; prefer loading from ViewModel using moduleNumber."

    val levels = listOf(
        ModuleLevel(R.drawable.bull_logo, "Hustler", 35),
        ModuleLevel(R.drawable.bull_logo, "MasterMind", 60),
        ModuleLevel(R.drawable.bull_logo, "Unstoppable", 85)
    )

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 🟢 Header Card
            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 140.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFDFFFD9), Color(0xFFFFFFFF))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Module : ${"%02d".format(moduleNumber)}",
                            fontSize = 14.sp,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = moduleTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1B1B),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color(0xFFBDBDBD))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            fontSize = 14.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🟢 Dashed connector + Level Cards
            // 🔹 Use a BoxWithConstraints to make the Canvas fill available height dynamically
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // 🟢 Dashed vertical line aligned with green dots
                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(start = 7.dp, top = 10.dp, bottom = 10.dp) // <-- Adjusted to dot center
                ) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f), 0f)
                    drawLine(
                        color = Color(0xFF66BB6A),
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = size.height),
                        strokeWidth = 3f,
                        pathEffect = pathEffect,
                        cap = StrokeCap.Round
                    )
                }

                // Level cards
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp), // no horizontal shift
                    verticalArrangement = Arrangement.spacedBy(56.dp)
                ) {
                    levels.forEach { level ->
                        LevelExpandableCard(level)
                    }
                }
            }


            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LevelExpandableCard(level: ModuleLevel) {
    var expanded by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (expanded) level.progress / 100f else 0f,
        label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Green dot indicator
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(Color(0xFF66BB6A), shape = RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Main Level Card
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = level.imageRes),
                            contentDescription = level.title,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF3F3F3))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = level.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                text = "Short subtitle or progress",
                                fontSize = 12.sp,
                                color = Color(0xFF757575),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF1A237E)
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Text(
                            text = "Level progress: ${level.progress}%",
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            color = Color(0xFF81C784),
                            trackColor = Color(0xFFD7FFD9),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { /* TODO: Navigate to lessons */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF3F51B5)
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text("Explore")
                        }
                    }
                }
            }
        }
    }
}
