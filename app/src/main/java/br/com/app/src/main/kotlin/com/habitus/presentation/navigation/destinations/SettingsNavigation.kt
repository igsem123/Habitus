package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.SettingsScreen

internal const val SETTINGS_ROUTE = "settings"

internal fun NavGraphBuilder.settingsNavigation(navController: NavHostController) {
    composable(route = SETTINGS_ROUTE) {
        SettingsScreen()
    }
}

fun NavController.navigateToSettings() {
    this.navigate(SETTINGS_ROUTE) {
        popUpTo(SETTINGS_ROUTE) {
            inclusive = true
        }
    }
}
