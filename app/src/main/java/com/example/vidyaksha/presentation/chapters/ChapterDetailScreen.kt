package com.example.vidyaksha.presentation.chapters

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.vidyaksha.presentation.destinations.SlideReaderScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.example.vidyaksha.R

@Destination
@Composable
fun ChapterDetailScreen(
    moduleNumber: Int,
    moduleTitle: String,
    navigator: DestinationsNavigator
) {
    val chapters = listOf(
        Triple("Chapter:1", "Stock Market Basics", "Understand what stocks are, how they work, and how they are traded."),
        Triple("Chapter:2", "IPO", "Learn what an Initial Public Offering is and how companies get listed."),
        Triple("Chapter:3", "Investing", "Grasp key investing principles that drive financial growth."),
        Triple("Chapter:4", "Compounding", "Explore the power of compounding to multiply wealth."),
        Triple("Chapter:5", "Budget Planning", "Master the art of managing your income and expenses smartly.")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FFF4))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // 🟢 Static top banner card
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

        Spacer(modifier = Modifier.height(5.dp))

        // 🟣 Scrollable chapter list
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())// 🔧 Add top padding to allow the first badge to show above
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            chapters.forEachIndexed { index, (chapterNo, title, description) ->
                ChapterCard(
                    chapterNo = chapterNo,
                    title = title,
                    description = description,
                    onUnleashClick = {
                        navigator.navigate(
                            SlideReaderScreenDestination(
                                moduleId = 1,
                                chapterId = index + 1
                            )
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ChapterCard(
    chapterNo: String,
    title: String,
    description: String,
    onUnleashClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
    ) {
        // 🟣 Main card
        Card(
            modifier = Modifier
                .matchParentSize()
                .border(1.dp, Color(0xFF90CAF9), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 🟡 Left vertical image strip
                Image(
                    painter = painterResource(id = R.drawable.stock),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                    contentScale = ContentScale.Crop
                )

                // 🟢 Right section
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = title,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = description,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            lineHeight = 18.sp
                        )
                    }

                    // 🟣 Unleash button at bottom
                    Text(
                        text = "Unleash",
                        color = Color(0xFF3F51B5),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clickable { onUnleashClick() }
                    )
                }
            }
        }

        // 🟦 Floating chapter badge (overlapping top-left border)
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
