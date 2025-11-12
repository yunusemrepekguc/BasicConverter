package com.yempe.financeapps.basicconverter.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yempe.financeapps.basicconverter.ui.components.AppBottomBar
import com.yempe.financeapps.core.navigation.BottomNavConfig
import com.yempe.financeapps.core.navigation.NavDestination
import com.yempe.financeapps.feature.converter.navigation.converterNavGraph
import com.yempe.financeapps.feature.setting.navigation.settingsNavGraph

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()


    Scaffold(
        bottomBar = {
            AppBottomBar(
                items = BottomNavConfig.items,
                navController = navController,
                onItemClick = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavDestination.BottomBar.Converter.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            converterNavGraph(navController)
            settingsNavGraph(navController)
        }
    }
}