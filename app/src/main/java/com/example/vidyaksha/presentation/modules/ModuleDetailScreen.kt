package com.example.vidyaksha.presentation.modules

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.data.local.Level
import com.example.vidyaksha.presentation.destinations.ChapterDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ModuleDetailScreen(
    navigator: DestinationsNavigator,
    moduleNumber: Int,
    moduleTitle: String,
    viewModel: ModuleViewModel = hiltViewModel()
) {

    /* ---------- JSON DATA ---------- */
    val module = viewModel.getModule(moduleNumber)
    val levels = module.levels   // ✅ from JSON

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

            /* ---------- HEADER CARD (UNCHANGED UI) ---------- */
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
                                listOf(Color(0xFFDFFFD9), Color.White)
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Module : ${"%02d".format(module.id)}",
                            fontSize = 14.sp,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = module.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1B1B),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color(0xFFBDBDBD))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = module.description,
                            fontSize = 14.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /* ---------- CONNECTOR + LEVELS ---------- */
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {

                Canvas(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(start = 7.dp, top = 10.dp, bottom = 10.dp)
                ) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 12f))
                    drawLine(
                        color = Color(0xFF66BB6A),
                        start = Offset(0f, 0f),
                        end = Offset(0f, size.height),
                        strokeWidth = 3f,
                        pathEffect = pathEffect,
                        cap = StrokeCap.Round
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(56.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    levels.forEachIndexed { index, level ->
                        LevelExpandableCard(
                            level = level,
                            navigator = navigator,
                            moduleNumber = module.id,
                            levelId = level.id
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/* ---------- LEVEL CARD (UI SAME) ---------- */

@Composable
fun LevelExpandableCard(
    level: Level,
    navigator: DestinationsNavigator,
    moduleNumber: Int,
    levelId: Int
) {
    var expanded by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label = ""
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .size(14.dp)
                .background(Color(0xFF66BB6A), RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
        ) {
            Column(Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(
                                ContentMapper.imageRes(level.image)
                            ),
                            contentDescription = level.name.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF3F3F3))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = level.name.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1A237E)
                            )
                            Text(
                                text = "Fixed level",
                                fontSize = 12.sp,
                                color = Color(0xFF757575),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector =
                            if (expanded) Icons.Filled.KeyboardArrowUp
                            else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF1A237E)
                    )
                }

                AnimatedVisibility(visible = expanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {

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
                            onClick = {
                                navigator.navigate(
                                    ChapterDetailScreenDestination(
                                        moduleNumber = moduleNumber,
                                        moduleTitle = level.name.name
                                    )
                                )
                            },
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
