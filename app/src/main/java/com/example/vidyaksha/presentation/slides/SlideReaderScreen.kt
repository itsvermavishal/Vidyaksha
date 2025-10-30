package com.example.vidyaksha.presentation.slides

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vidyaksha.data.local.ModuleRepository
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SlideReaderScreen(
    moduleId: Int,
    chapterId: Int,
    navigator: DestinationsNavigator
) {
    val module = ModuleRepository.modules.first { it.id == moduleId }
    val chapter = module.chapters.first { it.id == chapterId }
    val slides = chapter.slides

    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    // ✅ Hide the main bottom nav bar (if using one)
    // You can control this via shared state if your app uses a bottom nav

    Scaffold(
        topBar = {
            // Centered top bar with back button + title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { navigator.popBackStack() },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }

                    Text(
                        text = "Chapter: ${chapter.id.toString().padStart(2, '0')} | ${chapter.title}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },
        bottomBar = {
            val currentPage = pagerState.currentPage

            // Floating bottom navigation bar style
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left button: Exit or Previous
                    TextButton(
                        onClick = {
                            if (currentPage > 0) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage - 1)
                                }
                            } else {
                                navigator.popBackStack()
                            }
                        }
                    ) {
                        Text(
                            text = if (currentPage > 0) "Previous" else "Exit",
                            color = if (currentPage > 0) Color(0xFF1A237E) else Color.Red,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Center slide indicator
                    Text(
                        text = "${currentPage + 1} / ${slides.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )

                    // Right button: Next or Next Topic
                    TextButton(
                        onClick = {
                            if (currentPage < slides.size - 1) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                }
                            } else {
                                // Navigate to next chapter/topic here
                                // For now, go back or show message
                                navigator.popBackStack()
                            }
                        }
                    ) {
                        Text(
                            text = if (currentPage < slides.size - 1) "Next" else "Next Topic",
                            color = if (currentPage < slides.size - 1) Color(0xFF1A237E) else Color(0xFF2E7D32),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            val slide = slides[page]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Slide title
                Text(
                    text = slide.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Slide image
                slide.imageRes?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = slide.title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(180.dp)
                            .padding(vertical = 16.dp)
                    )
                }

                // Slide content
                Text(
                    text = slide.textContent,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(bottom = 120.dp)
                )
            }
        }
    }
}
