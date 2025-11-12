package com.yempe.financeapps.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.Settings

object BottomNavConfig {

    val items = listOf(
        BottomNavItem(
            navDestination = NavDestination.BottomBar.Converter,
            label = "Converter",
            icon = Icons.Rounded.CurrencyExchange
        ),
        BottomNavItem(
            navDestination = NavDestination.BottomBar.Settings,
            label = "Settings",
            icon = Icons.Rounded.Settings
        )
    )
}