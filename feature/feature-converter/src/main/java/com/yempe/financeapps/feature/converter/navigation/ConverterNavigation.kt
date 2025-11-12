package com.yempe.financeapps.feature.converter.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.yempe.financeapps.core.navigation.NavDestination
import com.yempe.financeapps.feature.converter.presenter.ui.AssetListScreen

fun NavGraphBuilder.converterNavGraph(
    navController: NavHostController
) {

    navigation(
        startDestination = NavDestination.Converter.Assets.route,
        route = NavDestination.BottomBar.Converter.route
    ) {
        composable(route = NavDestination.Converter.Assets.route) {
            AssetListScreen(
                // onClick event de navController.navigate(Destination.Converter.Detail.createRoute(id))
            )
        }

        composable(route = NavDestination.Converter.Detail("").route) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("id")
            // AssetDetailScreen(assetId = assetId, onBack = { navController.navigateUp() })
        }
    }
}