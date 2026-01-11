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
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.data.local.LearningChapterProgress
import com.example.vidyaksha.data.local.LearningLevelProgress
import com.example.vidyaksha.data.local.LearningSlideProgress
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
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = module.topCard.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B1B1B),
                            modifier = Modifier.padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color(0xFFBDBDBD))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = module.topCard.description,
                            fontSize = 14.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /* ---------- CONNECTOR + LEVELS ---------- */
            Box(modifier = Modifier.fillMaxWidth()) {
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
    var explored by rememberSaveable(level.id) { mutableStateOf(false) }

    // 1️⃣ Build learning progress model for THIS level
    val learningProgress = remember(level) {
        LearningLevelProgress(
            levelId = level.id,
            chapters = level.chapters.map { chapter ->
                LearningChapterProgress(
                    chapterId = chapter.id,
                    slides = chapter.slides.map { slide ->
                        LearningSlideProgress(
                            slideId = slide.id,
                            isCompleted = false // later: real value
                        )
                    }
                )
            }
        )
    }
    // ✅ Step B: get ViewModel
    val viewModel: ModuleViewModel = hiltViewModel()

    // ✅ Step C: calculate real progress
    val progress = remember(learningProgress) {
        viewModel.calculateLevelProgress(learningProgress)
    }
    val chapterCount = remember(level) {
        viewModel.calculateChapterCount(level)
    }

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

                /* ---------- HEADER ROW ---------- */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

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

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = level.topCard.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1A237E)
                        )

                        Text(
                            text = level.topCard.description,
                            fontSize = 12.sp,
                            color = Color(0xFF757575),
                            maxLines = if (expanded) 2 else 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Icon(
                        imageVector =
                            if (expanded) Icons.Filled.KeyboardArrowUp
                            else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF1A237E)
                    )
                }

                /* ---------- EXPANDED CONTENT ---------- */
                AnimatedVisibility(visible = expanded) {

                    Column(modifier = Modifier.padding(top = 16.dp)) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = progress,
                                color = Color(0xFF81C784),
                                trackColor = Color(0xFFD7FFD9),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "${(progress * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF388E3C)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            // 🔥 LEFT — CHAPTER COUNT (ONLY WHEN EXPANDED)
                            FireGradientText(
                                text = "$chapterCount Chapters",
                                fontSize = 14
                            )

                            // 👉 RIGHT — Explore / Continue
                            Text(
                                text = if (explored) "Continue" else "Explore",
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .background(
                                        brush = if (explored)
                                            Brush.horizontalGradient(
                                                listOf(
                                                    Color(0xFF66BB6A),
                                                    Color(0xFF43A047)
                                                )
                                            )
                                        else
                                            Brush.horizontalGradient(
                                                listOf(Color.LightGray, Color.Gray)
                                            ),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(horizontal = 26.dp, vertical = 10.dp)
                                    .clickable {
                                        explored = true
                                        navigator.navigate(
                                            ChapterDetailScreenDestination(
                                                moduleNumber = moduleNumber,
                                                levelId = level.id
                                            )
                                        )
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FireGradientText(
    text: String,
    fontSize: Int = 14
) {
    Text(
        text = text,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Bold,
        style = androidx.compose.ui.text.TextStyle(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFFF3D00), // Fire Red
                    Color(0xFFFFC107)  // Fire Yellow
                )
            )
        )
    )
}



