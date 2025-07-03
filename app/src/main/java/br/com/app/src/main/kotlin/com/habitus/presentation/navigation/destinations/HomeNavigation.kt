package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.HomeScreen
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel

internal const val HOME_ROUTE = "home"

internal fun NavGraphBuilder.homeNavigation(
    settingsViewModel: SettingsViewModel
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(settingsViewModel = settingsViewModel)
    }
}

fun NavController.navigateToHome() {
    this.navigate(HOME_ROUTE) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

