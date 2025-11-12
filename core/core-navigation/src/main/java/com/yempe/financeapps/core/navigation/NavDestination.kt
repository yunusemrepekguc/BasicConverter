package com.yempe.financeapps.core.navigation

sealed interface NavDestination {

    val route: String

    sealed class BottomBar(override val route: String) : NavDestination {

        data object Converter : BottomBar("converter_graph")
        data object Settings : BottomBar("settings_graph")
    }

    sealed class Converter(override val route: String) : NavDestination {

        data object Assets : Converter("converter_assets")
        data class Detail(val assetId: String) : Converter("converter_asset_detail/{id}") {
            companion object {
                fun createRoute(id: String) = "converter_asset_detail/$id"
            }
        }
    }

    sealed class Settings(override val route: String) : NavDestination {

        data object Main : Settings("settings_main")

        data object About: Settings("settings_about")
    }
}