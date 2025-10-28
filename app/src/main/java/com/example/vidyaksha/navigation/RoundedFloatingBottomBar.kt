package com.example.vidyaksha.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ramcosta.composedestinations.spec.DestinationSpec

@Composable
fun RoundedFloatingBottomBar(navController: NavHostController, items: List<BottomNavItem>) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry.value?.destination

    // Bar container height (so it visually floats)
    val containerHeight = 64.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(containerHeight)
            .padding(horizontal = 16.dp)
    ) {
        Surface(modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-12).dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp)), color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(20.dp), tonalElevation = 8.dp) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    // Check whether current destination matches this item's destination.route
                    val selected = currentDestination?.hierarchy?.any { navDest ->
                        // Compare using the generated DestinationSpec route string
                        navDest.route == item.destination.route
                    } == true

                    val tint by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.primary else Color.Gray
                    )


                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                // Navigate using the destination's route string
                                val route = item.destination.route
                                if (route != null && currentDestination?.route != route) {
                                    navController.navigate(route) {
                                        // popUpTo start to avoid stacking many copies
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                    ) {
                        Icon(imageVector = item.icon, contentDescription = item.title, tint = tint)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.title,
                            color = tint,
                            style = MaterialTheme.typography.labelSmall  // or labelMedium/bodySmall as per design
                        )
                    }
                }
            }
        }
    }
}
