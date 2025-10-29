package com.example.vidyaksha.presentation.modules

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun ModuleLevelDetailScreen(
    moduleNumber: Int,
    moduleTitle: String
) {
    val levels = listOf("Beginner", "Intermediate", "Advanced", "Certificate")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FFF4))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // 🟢 Top card — module info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDFFFD9)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Module : ${"%02d".format(moduleNumber)}",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = moduleTitle,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Start with this module if you are new to the topic. Learn key fundamentals, progress through levels, and earn your certificate.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 🟢 Levels list with vertical dashed line
        Box(modifier = Modifier.fillMaxSize()) {
            // Draw dashed vertical line
            Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 24.dp)
            ) {
                val dashHeight = 20f
                val spaceHeight = 16f
                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = Color(0xFF4CAF50),
                        start = Offset(0f, y),
                        end = Offset(0f, y + dashHeight),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                    y += dashHeight + spaceHeight
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                levels.forEachIndexed { index, level ->
                    LevelCard(
                        levelName = level,
                        progress = when (index) {
                            0 -> 35
                            1 -> 60
                            2 -> 80
                            else -> 0
                        },
                        isLocked = (level == "Certificate")
                    )
                }
            }
        }
    }
}

@Composable
fun LevelCard(levelName: String, progress: Int, isLocked: Boolean = false) {
    var expanded by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(targetValue = if (expanded) progress / 100f else 0f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(
                    if (isLocked) Color.Gray else Color(0xFF4CAF50),
                    shape = RoundedCornerShape(50)
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isLocked) Color(0xFFF3F3F3) else Color(0xFFFFF3F0)
            ),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = levelName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1A237E)
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF1A237E)
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text(
                            text = "Level progress: ${progress}%",
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
                            onClick = { /* navigate to lessons */ },
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
