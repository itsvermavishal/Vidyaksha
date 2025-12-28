package com.example.vidyaksha.presentation.spark

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.R
import com.example.vidyaksha.presentation.destinations.LatestNewsScreenDestination
import com.example.vidyaksha.presentation.destinations.MarketTrendsScreenDestination
import com.example.vidyaksha.presentation.destinations.ModuleDetailScreenDestination
import com.example.vidyaksha.presentation.destinations.TopGainersScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max

data class CarouselItem(val imageRes: Int, val title: String)
data class GridItem(val imageRes: Int, val label: String)


// Simple item model used by the pager
data class CardItem(
    val imageRes: Int,
    val heading: String,
    val description: String
)

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

        val RubikMono = FontFamily(
            Font(R.font.rubik_mono_one_regular, FontWeight.Normal)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top carousel — clicking a card navigates to respective destination
            AutoSlidingCarousel(
                items = carouselItems,
                onItemClick = { index ->
                    when (index) {
                        0 -> navigator.navigate(MarketTrendsScreenDestination())
                        1 -> navigator.navigate(TopGainersScreenDestination())
                        2 -> navigator.navigate(LatestNewsScreenDestination())
                    }
                }
            )
            Spacer(Modifier.height(24.dp))
            GridSection(gridItems, navigator)
            Spacer(Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Crafted with ",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = RubikMono,
                        fontSize = 25.sp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF3B82F6), // Blue
                                Color(0xFF8B5CF6)  // Purple
                            )
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

/**
 * Reusable swipeable pager. Supports:
 *  - arrows (left/right) — show/hide automatically at edges
 *  - optional horizontal drag gestures (swipe by finger)
 *  - simple scale/parallax effect
 */
@Composable
fun SwipeableCardPager(
    title: String? = null,
    cards: List<CardItem>,
    enableArrows: Boolean = true,
    enableGestures: Boolean = false,
    pagerHeight: Dp = 360.dp,
    onCardClick: (index: Int) -> Unit = {}
) {
    var currentIndex by remember { mutableStateOf(0) }
    var dragOffset by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Divider(Modifier.padding(vertical = 8.dp))
        }

        Text(
            text = cards[currentIndex].heading,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF6C63FF),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(pagerHeight)
                .padding(horizontal = 12.dp)
                .pointerInput(enableGestures) {
                    if (enableGestures) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                // settle swipe
                                if (dragOffset < -120 && currentIndex < cards.lastIndex) currentIndex++
                                else if (dragOffset > 120 && currentIndex > 0) currentIndex--
                                dragOffset = 0f
                            }
                        ) { change, dragAmount ->
                            dragOffset += dragAmount
                            change.consume()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            val item = cards[currentIndex]

            // Animate scale & offset for subtle parallax
            val animatedScale by animateFloatAsState(
                targetValue = 1f - (abs(dragOffset) / 2000f).coerceIn(0f, 0.1f),
                label = "scale"
            )
            val animatedTranslationX by animateFloatAsState(
                targetValue = dragOffset * 0.2f,
                label = "translate"
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .shadow(10.dp, shape = MaterialTheme.shapes.large)
                    .clip(MaterialTheme.shapes.large)
                    .graphicsLayer {
                        scaleX = animatedScale
                        scaleY = animatedScale
                        translationX = animatedTranslationX
                    }
                    .clickable { onCardClick(currentIndex) },
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.heading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(pagerHeight * 0.55f),
                        contentScale = ContentScale.Crop
                    )
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = item.heading,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 4
                        )
                    }
                }
            }

            // Left arrow
            if (enableArrows && currentIndex > 0) {
                IconButton(
                    onClick = { if (currentIndex > 0) currentIndex-- },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "Prev",
                        tint = Color(0xFF6C63FF)
                    )
                }
            }

            // Right arrow
            if (enableArrows && currentIndex < cards.lastIndex) {
                IconButton(
                    onClick = { if (currentIndex < cards.lastIndex) currentIndex++ },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "Next",
                        tint = Color(0xFF6C63FF)
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${currentIndex + 1}/${cards.size}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(Modifier.width(12.dp))
            Row {
                repeat(cards.size) { idx ->
                    val dotColor = if (idx == currentIndex) Color(0xFF6C63FF) else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(8.dp)
                            .background(dotColor, shape = MaterialTheme.shapes.small)
                    )
                }
            }
        }
    }
}

/* ---------- Destination screens for each banner ---------- */

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun MarketTrendsScreen() {
    val dummy = remember {
        listOf(
            CardItem(R.drawable.sample, "Global Market Rally", "Short description..."),
            CardItem(R.drawable.sample, "Sector Rotation", "Short description..."),
            CardItem(R.drawable.sample, "Economic Outlook", "Short description...")
        )
    }

    val pagerState = rememberPagerState(pageCount = { dummy.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFCF9))
            .padding(top = 15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Market Trends",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color(0xFF1C1C1C)
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Start)
        )

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) {page ->
            val item = dummy[page]

            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue
                        scaleX = 1f - (pageOffset * 0.05f)
                        scaleY = 1f - (pageOffset * 0.05f)
                    }
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.heading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                    Column(Modifier.padding(16.dp).fillMaxWidth()) {
                        Text(
                            text = item.heading,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color(0xFF212121)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF6D6D6D),
                                fontSize = 15.sp
                            ),
                            maxLines = 15
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "${pagerState.currentPage + 1} / ${dummy.size}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun TopGainersScreen() {
    val dummy = remember {
        listOf(
            CardItem(R.drawable.sample, "Top Gainer - A", "Short description..."),
            CardItem(R.drawable.sample, "Top Gainer - B", "Short description..."),
            CardItem(R.drawable.sample, "Top Gainer - C", "Short description...")
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { dummy.size }
    )
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFCF9))
            .padding(top = 15.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Top Gainers",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color(0xFF1C1C1C)
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.Start)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    pageSpacing = 16.dp
                ) { page ->
                    val item = dummy[page]
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                    ) {
                        Column {
                            Image(
                                painter = painterResource(id = item.imageRes),
                                contentDescription = item.heading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f),
                                contentScale = ContentScale.Crop
                            )
                            Column(Modifier.padding(16.dp)) {
                                Text(
                                    text = item.heading,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )
                                )
                            }
                        }
                    }
                }

                if (pagerState.currentPage > 0) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Previous",
                            tint = Color(0xFF6C63FF)
                        )
                    }
                }

                if (pagerState.currentPage < dummy.lastIndex) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "Next",
                            tint = Color(0xFF6C63FF)
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(
                text = "${pagerState.currentPage + 1} / ${dummy.size}",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Destination
@Composable
fun LatestNewsScreen() {
    val dummy = remember {
        listOf(
            CardItem(R.drawable.sample, "RBI Policy update", "News headline short body..."),
            CardItem(R.drawable.sample, "Important Market News", "Short news summary..."),
            CardItem(R.drawable.sample, "Global Economy", "Highlights from financial reports...")
        )
    }

    val pagerState = rememberPagerState(pageCount = { dummy.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFCF9))
            .padding(top = 15.dp, bottom = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Latest News",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = Color(0xFF1C1C1C)
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Start)
        )

        // Pager
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 16.dp,
            modifier = Modifier.fillMaxHeight(0.9f)
        ) { page ->
            val item = dummy[page]

            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                ).absoluteValue
                        scaleX = 1f - (pageOffset * 0.05f)
                        scaleY = 1f - (pageOffset * 0.05f)
                    }
                    .fillMaxWidth()
                    .height(800.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.heading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = item.heading,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = Color(0xFF212121)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF6D6D6D),
                                fontSize = 15.sp
                            ),
                            maxLines = 15
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp)) // reduced gap

        Text(
            text = "${pagerState.currentPage + 1} / ${dummy.size}",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}



@Composable
fun AutoSlidingCarousel(
    items: List<CarouselItem>,
    onItemClick: (index: Int) -> Unit = {}
) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(currentIndex) {
        delay(3000)
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
                    .clickable { onItemClick(currentIndex) }
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
                onClick = {
                    currentIndex = if (currentIndex - 1 < 0) items.lastIndex else currentIndex - 1
                },
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
