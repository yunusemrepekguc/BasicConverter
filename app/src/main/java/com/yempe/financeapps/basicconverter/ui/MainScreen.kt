package com.yempe.financeapps.basicconverter.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yempe.financeapps.core.navigation.NavRoute
import com.yempe.financeapps.feature.converter.presenter.ui.AssetListScreen
import com.yempe.financeapps.feature.setting.presentation.ui.SettingsScreen

@Composable
fun MainScreen(
    modifier: Modifier
) {
    val navController = rememberNavController()
    val items = listOf(
        NavRoute.Converter,
        NavRoute.Settings
    )

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    ),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = Color(0xFFF5F5F5)
            ) {
                NavigationBar(
                    modifier = Modifier.height(80.dp),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp
                ) {
                    val currentDestination =
                        navController.currentBackStackEntryAsState().value?.destination

                    items.forEach { item ->
                        val isSelected = currentDestination?.route == item.route

                        val iconScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.1f else 1f,
                            animationSpec = tween(durationMillis = 200),
                            label = "iconScale"
                        )

                        val iconColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF1976D2) else Color(0xFF616161),
                            animationSpec = tween(durationMillis = 200),
                            label = "iconColor"
                        )

                        val labelColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF1565C0) else Color(0xFF424242),
                            animationSpec = tween(durationMillis = 200),
                            label = "labelColor"
                        )

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label,
                                        modifier = Modifier
                                            .size(26.dp)
                                            .scale(iconScale),
                                        tint = iconColor
                                    )
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(24.dp)
                                                .height(3.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(Color(0xFF1976D2))
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    color = labelColor,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF1976D2),
                                unselectedIconColor = Color(0xFF616161),
                                selectedTextColor = Color(0xFF1565C0),
                                unselectedTextColor = Color(0xFF424242),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.Converter.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoute.Converter.route) {
                AssetListScreen()
            }
            composable(NavRoute.Settings.route) {
                SettingsScreen()
            }
        }
    }
}