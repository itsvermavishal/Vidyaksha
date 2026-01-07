package com.example.vidyaksha.presentation.spark

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.R
import com.example.vidyaksha.data.local.Module
import com.example.vidyaksha.presentation.destinations.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay

/* ---------- UI MODELS (ONLY FOR CAROUSEL) ---------- */

data class CarouselItem(
    val imageRes: Int,
    val title: String
)

/* ---------- SCREEN ---------- */

@Destination
@Composable
fun SparkScreen(
    navigator: DestinationsNavigator,
    viewModel: SparkViewModel = hiltViewModel()
) {
    val modules = viewModel.modules   // ✅ JSON driven

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
            CarouselItem(R.drawable.markettrendd, "Market Trends"),
            CarouselItem(R.drawable.bullvsbear, "Top Gainers"),
            CarouselItem(R.drawable.latestnews, "Latest News")
        )

        val rubikMono = FontFamily(
            Font(R.font.rubik_mono_one_regular, FontWeight.Normal)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔴 DEBUG LINE — ADD THIS
            Text(
                text = "Modules count: ${modules.size}",
                color = Color.Red,
                fontSize = 16.sp
            )

            Spacer(Modifier.height(12.dp))

            // 🔵 Your existing UI continues here
            /* ---------- TOP CAROUSEL (UNCHANGED) ---------- */
            AutoSlidingCarousel(
                items = carouselItems,
                onItemClick = { index ->
//                    when (index) {
//                        0 -> navigator.navigate(MarketTrendsScreenDestination())
//                        1 -> navigator.navigate(TopGainersScreenDestination())
//                        2 -> navigator.navigate(LatestNewsScreenDestination())
//                    }
                }
            )

            Spacer(Modifier.height(24.dp))

            /* ---------- GRID FROM JSON MODULES ---------- */
            GridSection(
                modules = modules,
                navigator = navigator
            )

            Spacer(Modifier.height(24.dp))

            /* ---------- FOOTER ---------- */
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Crafted with ",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = rubikMono,
                        fontSize = 25.sp,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                        )
                    )
                )
                Text(
                    text = "💛",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .animateContentSize()
                )
            }
        }
    }
}

/* ---------- AUTO SLIDING CAROUSEL (UNCHANGED) ---------- */

@Composable
fun AutoSlidingCarousel(
    items: List<CarouselItem>,
    onItemClick: (index: Int) -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(3000)
        currentIndex = (currentIndex + 1) % items.size
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                    .clickable { onItemClick(currentIndex) }
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            currentIndex =
                                if (dragAmount > 0)
                                    if (currentIndex - 1 < 0) items.lastIndex else currentIndex - 1
                                else (currentIndex + 1) % items.size
                        }
                    }
            ) {
                Column {
                    Image(
                        painter = painterResource(item.imageRes),
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
                        Text(item.title, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            /* ⬅ LEFT ARROW */
            IconButton(
                onClick = {
                    currentIndex =
                        if (currentIndex - 1 < 0) items.lastIndex else currentIndex - 1
                },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = "Previous",
                    tint = Color(0xFF6C63FF)
                )
            }

            /* ➡ RIGHT ARROW */
            IconButton(
                onClick = { currentIndex = (currentIndex + 1) % items.size },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_right),
                    contentDescription = "Next",
                    tint = Color(0xFF6C63FF)
                )
            }
        }

        Row(modifier = Modifier.padding(top = 10.dp)) {
            repeat(items.size) { index ->
                val color by animateColorAsState(
                    if (index == currentIndex) Color(0xFF7C4DFF) else Color.LightGray
                )
                Box(
                    Modifier
                        .padding(4.dp)
                        .size(10.dp)
                        .background(color, RoundedCornerShape(50))
                )
            }
        }
    }
}


/* ---------- GRID SECTION (JSON DRIVEN) ---------- */

@Composable
fun GridSection(
    modules: List<Module>,
    navigator: DestinationsNavigator
) {
    Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        modules.chunked(2).forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                row.forEach { module ->
                    GridItemCard(
                        module = module,
                        navigator = navigator,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

/* ---------- GRID CARD (MODULE BASED) ---------- */

@Composable
fun GridItemCard(
    module: Module,
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
                        moduleNumber = module.id,
                        moduleTitle = module.title
                    )
                )
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = com.example.vidyaksha.data.local.ContentMapper.imageRes(module.image)
                ),
                contentDescription = module.title,
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
                    text = module.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
