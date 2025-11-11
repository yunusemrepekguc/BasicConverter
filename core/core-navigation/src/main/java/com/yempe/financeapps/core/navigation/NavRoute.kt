package com.yempe.financeapps.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CurrencyExchange
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class NavRoute(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    data object Converter : NavRoute(
        route = "converter",
        label = "Converter",
        icon = Icons.Rounded.CurrencyExchange
    )

    data object Settings : NavRoute(
        route = "settings",
        label = "Settings",
        icon = Icons.Rounded.Settings
    )
}