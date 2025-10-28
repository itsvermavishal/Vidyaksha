package com.example.vidyaksha.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vidyaksha.presentation.destinations.HomeScreenDestination
import com.example.vidyaksha.presentation.destinations.SessionScreenRouteDestination
import com.example.vidyaksha.presentation.destinations.SparkScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec

data class BottomNavItem(
    val destination: DestinationSpec<*>,
    val title: String,
    val icon: ImageVector
)

val BottomNavItems = listOf(
    BottomNavItem(destination = HomeScreenDestination, title = "Home", icon = Icons.Default.Home),
    BottomNavItem(destination = SparkScreenDestination, title = "Spark", icon = Icons.Default.Star),
    BottomNavItem(destination = SessionScreenRouteDestination, title = "Session", icon = Icons.Default.List)
)
