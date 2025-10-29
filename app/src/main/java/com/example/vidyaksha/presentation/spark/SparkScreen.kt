package com.example.vidyaksha.presentation.spark

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.R
import com.example.vidyaksha.presentation.destinations.ModuleDetailScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

data class CarouselItem(val imageRes: Int, val title: String)
data class GridItem(val imageRes: Int, val label: String)

@Destination
@Composable
fun SparkScreen(
    navigator: DestinationsNavigator,
    viewModel: SparkViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFF9E3), Color(0xFFF8F8FF))
                )
            )
    ) {
        val carouselItems = listOf(
            CarouselItem(R.drawable.sample, "Market Trends"),
            CarouselItem(R.drawable.sample, "Top Gainers"),
            CarouselItem(R.drawable.sample, "Latest News")
        )

        val gridItems = listOf(
            GridItem(R.drawable.bull_logo, "Basics"),
            GridItem(R.drawable.bull_logo, "Fundamentals"),
            GridItem(R.drawable.bull_logo, "Technicals"),
            GridItem(R.drawable.bull_logo, "Personal Finance"),
            GridItem(R.drawable.bull_logo, "Commodity"),
            GridItem(R.drawable.bull_logo, "F & O"),
            GridItem(R.drawable.bull_logo, "Others"),
            GridItem(R.drawable.bull_logo, "Coming Soon")
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AutoSlidingCarousel(carouselItems)
            Spacer(Modifier.height(24.dp))
            GridSection(gridItems, navigator)
            Spacer(Modifier.height(24.dp))
            Text(
                text = "Crafted with 💛",
                color = Color(0xFFFFC107),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AutoSlidingCarousel(items: List<CarouselItem>) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(4000)
        currentIndex = (currentIndex + 1) % items.size
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentAlignment = Alignment.Center
        ) {
            val item = items[currentIndex]

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .padding(horizontal = 40.dp)
                    .clickable { /* navigate later */ }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 0)
                                currentIndex =
                                    if (currentIndex - 1 < 0) items.lastIndex else currentIndex - 1
                            else
                                currentIndex = (currentIndex + 1) % items.size
                        }
                    }
            ) {
                Column {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF7C4DFF))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            IconButton(
                onClick = { currentIndex = if (currentIndex - 1 < 0) items.lastIndex else currentIndex - 1 },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Previous",
                    tint = Color(0xFF6C63FF)
                )
            }

            IconButton(
                onClick = { currentIndex = (currentIndex + 1) % items.size },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Next",
                    tint = Color(0xFF6C63FF)
                )
            }
        }

        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(items.size) { index ->
                val color by animateColorAsState(
                    if (index == currentIndex) Color(0xFF7C4DFF) else Color.LightGray
                )
                Box(
                    Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .background(color, shape = RoundedCornerShape(50))
                )
            }
        }
    }
}

@Composable
fun GridSection(items: List<GridItem>, navigator: DestinationsNavigator) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        val flat = items
        for (rowItems in flat.chunked(2)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                val startIndex = flat.indexOf(rowItems.first())
                for ((colIndex, item) in rowItems.withIndex()) {
                    val moduleIndex = startIndex + colIndex
                    GridItemCard(
                        item = item,
                        moduleIndex = moduleIndex,
                        navigator = navigator,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun GridItemCard(
    item: GridItem,
    moduleIndex: Int,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                navigator.navigate(
                    ModuleDetailScreenDestination(
                        moduleNumber = moduleIndex,
                        moduleTitle = item.label
                    )
                )
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.label,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.label,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
