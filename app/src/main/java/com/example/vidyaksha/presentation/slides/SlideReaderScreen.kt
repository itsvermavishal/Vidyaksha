package com.example.vidyaksha.presentation.slides

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.vidyaksha.data.local.ContentMapper
import com.example.vidyaksha.data.local.LevelType
import com.example.vidyaksha.data.local.SlideBlock
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun SlideReaderScreen(
    moduleId: Int,
    levelType: LevelType,
    chapterId: Int,
    navigator: DestinationsNavigator,
    viewModel: SlideViewModel = hiltViewModel()
) {

    /* ---------- DATA FROM JSON ---------- */

    val slides = viewModel.getSlides(
        moduleId = moduleId,
        level = levelType,
        chapterId = chapterId
    )

    val pagerState = rememberPagerState(pageCount = { slides.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
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
                        text = "Chapter: ${chapterId.toString().padStart(2, '0')}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        },

        bottomBar = {
            val currentPage = pagerState.currentPage

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

                    Text(
                        text = "${currentPage + 1} / ${slides.size}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.DarkGray
                    )

                    TextButton(
                        onClick = {
                            if (currentPage < slides.lastIndex) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                }
                            } else {
                                navigator.popBackStack()
                            }
                        }
                    ) {
                        Text(
                            text = if (currentPage < slides.lastIndex) "Next" else "Next Topic",
                            color = if (currentPage < slides.lastIndex)
                                Color(0xFF1A237E)
                            else
                                Color(0xFF2E7D32),
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

                /* ---------- SLIDE TITLE ---------- */
                Text(
                    text = slide.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                /* ---------- SLIDE BLOCKS ---------- */
                slide.blocks.forEach { block ->
                    when (block) {

                        is SlideBlock.Text -> {
                            Text(
                                text = block.text,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        is SlideBlock.Markdown -> {
                            Text(
                                text = block.markdown,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }

                        is SlideBlock.Image -> {
                            block.images.forEach { imageName ->
                                AsyncImage(
                                    model = ContentMapper.imageRes(imageName),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(180.dp)
                                        .padding(vertical = 16.dp)
                                )
                            }
                        }

                        is SlideBlock.Table -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            ) {
                                block.headers.forEach { header ->
                                    Text(
                                        text = header,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                block.rows.forEach { row ->
                                    Text(
                                        text = row.joinToString("  |  "),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}
