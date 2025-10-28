package com.example.vidyaksha.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.spec.DestinationSpec

@Composable
fun RoundedFloatingBottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry.value?.destination

    val containerHeight = 100.dp // taller for floating feel

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(containerHeight)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.TopCenter)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(24.dp)
                ),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEachIndexed { index, item ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == item.destination.route
                    } == true

                    // Animated color and scale
                    val tint by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                    val scale by animateFloatAsState(
                        targetValue = if (selected) 1.25f else 1.0f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )

                    // Center icon gets slight lift
                    val lift by animateDpAsState(
                        targetValue = if (index == 1) (-1).dp else 0.dp,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .offset(y = lift)
                            .clickable {
                                val route = item.destination.route
                                if (route != null && currentDestination?.route != route) {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                    ) {
                        val iconScale = if (index == 1) 1.3f else 1.0f // center slightly bigger
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = tint,
                            modifier = Modifier.scale(scale * iconScale)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = item.title,
                            color = tint,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
