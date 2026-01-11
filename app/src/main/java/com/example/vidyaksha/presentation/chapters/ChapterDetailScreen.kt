package com.example.vidyaksha.presentation.chapters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.presentation.destinations.SlideReaderScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import com.example.vidyaksha.data.local.ChapterProgressUI


@OptIn(ExperimentalAnimationApi::class)
@Destination
@Composable
fun ChapterDetailScreen(
    moduleNumber: Int,
    levelId: Int,
    navigator: DestinationsNavigator,
    viewModel: ChapterViewModel = hiltViewModel()
) {

    /* ---------- JSON DATA ---------- */
    val module = viewModel.getModule(moduleNumber)
    val level = viewModel.getLevel(moduleNumber, levelId)
    val chapters = level.chapters
    val listState = rememberLazyListState()
    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val currentIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val viewportCenter =
                (layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset) / 2

            layoutInfo.visibleItemsInfo.minByOrNull { item ->
                kotlin.math.abs(
                    (item.offset + item.size / 2) - viewportCenter
                )
            }?.index ?: 0
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FFF4))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        /* ---------- TOP BANNER (UNCHANGED UI) ---------- */
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFDFFFD9)),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Module : ${"%02d".format(module.id)}",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = level.topCard.title,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = level.topCard.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        /* ---------- CHAPTER LIST (JSON DRIVEN) ---------- */
        LazyRow(
            state = listState,
            flingBehavior = snapBehavior,
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ){
            items(chapters) { chapter ->
                val progressUI = remember(chapter) {
                    viewModel.buildChapterProgress(chapter)
                }
                ChapterCard(
                    chapterNo = "Chapter:${chapter.id}",
                    title = chapter.title,
                    description = chapter.description,
                    progressUI = progressUI,
                    onUnleashClick = {
                        navigator.navigate(
                            SlideReaderScreenDestination(
                                moduleId = moduleNumber,
                                chapterId = chapter.id
                            )
                        )
                    }
                )
            }
        }

        DotsIndicator(
            totalDots = chapters.size,
            selectedIndex = currentIndex
        )

    }
}

/* ---------- CARD UI (UNCHANGED) ---------- */

@Composable
fun ChapterCard(
    chapterNo: String,
    title: String,
    description: String,
    progressUI: ChapterProgressUI,
    onUnleashClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(450.dp)
    ) {
        Card(
            modifier = Modifier
                .matchParentSize()
                .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                /* ---------- TOP ROW : TITLE ---------- */
                Box(
                    modifier = Modifier
                        .padding(top = 25.dp, bottom = 25.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    this@Column.AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically(initialOffsetY = { -20 })
                    ) {
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            maxLines = 2,
                            lineHeight = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = true
                        )
                    }
                }


                /* ---------- SECOND ROW ---------- */
                Row(
                    modifier = Modifier.weight(1f)
                ) {

                    /* IMAGE COLUMN */
                    Image(
                        painter = painterResource(id = R.drawable.stock),
                        contentDescription = null,
                        modifier = Modifier
                            .width(110.dp)
                            .fillMaxHeight()              // 👈 reaches bottom
                            .padding(top = 4.dp)          // 👈 small top margin
                            .clip(
                                RoundedCornerShape(
                                    topStart = 12.dp,
                                    bottomStart = 12.dp
                                )
                            ),
                        contentScale = ContentScale.Crop // 👈 crops from right if needed
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    /* TEXT DESCRIPTION */
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = description,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    /* LEFT — SLIDES COUNT */
                    Text(
                        text = "${progressUI.totalSlides} Slides",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF455A64)
                    )

                    /* CENTER — UNLEASH */
                    Text(
                        text = "Unleash",
                        color = Color.DarkGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(
                                brush = Brush.horizontalGradient(
                                    listOf(Color(0xFFFCD7FB), Color(0xFFAEC6FF))
                                ),
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 22.dp, vertical = 9.dp)
                            .clickable { onUnleashClick() }
                    )

                    /* RIGHT — CIRCULAR PROGRESS */
                    CircularProgressWithText(
                        progress = progressUI.progress
                    )
                }

            }
        }

        /* ---------- CHAPTER TAG (UNCHANGED) ---------- */
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = (-10).dp)
                .background(Color(0xFF5C6BC0), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 5.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = chapterNo,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (index == selectedIndex) 10.dp else 8.dp)
                    .background(
                        color = if (index == selectedIndex)
                            Color(0xFF3F51B5)
                        else
                            Color.LightGray,
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

@Composable
fun CircularProgressWithText(
    progress: Float
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(44.dp)
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 4.dp,
            color = Color(0xFF3F51B5),
            trackColor = Color(0xFFE0E0E0),
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5)
        )
    }
}
