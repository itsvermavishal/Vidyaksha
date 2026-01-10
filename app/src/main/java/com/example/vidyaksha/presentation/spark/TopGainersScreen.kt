package com.example.vidyaksha.presentation.spark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vidyaksha.presentation.slide.SwipeableSlidePager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun TopGainersScreen(
    viewModel: SparkViewModel = hiltViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val isDark = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = if (isDark) Color(0xFF020617) else Color(0xFFF1F5FF),
            darkIcons = !isDark
        )
    }

    SwipeableSlidePager(
        title = "Top Gainers",
        slides = viewModel.getTopGainers()
    )
}
