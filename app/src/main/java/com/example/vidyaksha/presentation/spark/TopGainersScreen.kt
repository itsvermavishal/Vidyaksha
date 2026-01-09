package com.example.vidyaksha.presentation.spark

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.data.mapper.toCardItem
import com.example.vidyaksha.presentation.common.SwipeableCardPager
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun TopGainersScreen(
    viewModel: SparkViewModel = hiltViewModel()
) {
    val cards = viewModel.getTopGainers()
        .map { it.toCardItem() }

    SwipeableCardPager(
        title = "Top Gainers",
        cards = cards
    )
}
