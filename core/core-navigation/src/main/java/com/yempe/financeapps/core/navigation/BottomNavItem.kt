package com.yempe.financeapps.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector


data class BottomNavItem(
    val navDestination: NavDestination.BottomBar,
    val label: String,
    val icon: ImageVector
)