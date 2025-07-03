package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.HomeScreen
import br.com.app.src.main.kotlin.com.habitus.presentation.toUserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.AuthViewModel
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel

internal const val HOME_ROUTE = "home"

internal fun NavGraphBuilder.homeNavigation(
    settingsViewModel: SettingsViewModel,
) {
    composable(route = HOME_ROUTE) {
        val authViewModel = hiltViewModel<AuthViewModel>()
        val firebaseUser = authViewModel.user.collectAsState()
        val userEntity = firebaseUser.value?.toUserEntity()

        HomeScreen(
            settingsViewModel = settingsViewModel,
            user = userEntity ?: throw IllegalStateException("User must not be null in HomeScreen")
        )
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

