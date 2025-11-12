package com.yempe.financeapps.feature.setting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yempe.financeapps.core.navigation.NavDestination
import com.yempe.financeapps.feature.setting.presentation.ui.SettingsScreen

fun NavGraphBuilder.settingsNavGraph(
    navController: NavController
) {

    navigation(
        startDestination = NavDestination.Settings.Main.route,
        route = NavDestination.BottomBar.Settings.route
    ) {

        composable(route = NavDestination.Settings.Main.route) {
            SettingsScreen(
                // onAboutClick event de navController.navigate(Destination.Settings.About.route)
            )
        }

        composable(route = NavDestination.Settings.About.route) {
            // AboutScreen()
        }
    }
}