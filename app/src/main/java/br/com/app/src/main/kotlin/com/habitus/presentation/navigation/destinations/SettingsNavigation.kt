package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.SettingsScreen
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel


internal const val SETTINGS_ROUTE = "settings"

internal fun NavGraphBuilder.settingsNavigation(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    composable(route = SETTINGS_ROUTE) {
        SettingsScreen(navController = navController, viewModel = settingsViewModel)
    }
}

fun NavController.navigateToSettings() {
    this.navigate(SETTINGS_ROUTE) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
