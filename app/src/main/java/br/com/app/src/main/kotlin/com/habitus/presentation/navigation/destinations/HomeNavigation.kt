package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.HomeScreen

internal const val HOME_ROUTE = "home"

internal fun NavGraphBuilder.homeNavigation(
    navController: NavHostController,
    user: UserEntity?
) {
    composable(
        route = HOME_ROUTE,
    ) {
        user?.let {
            HomeScreen(
                user = it,
                onNavigateToRegisterHabits = {
                    navController.navigateToRegisterHabits()
                }
            )
        } ?: run {
            navController.navigate(INITIAL_FORM_ROUTE) {
                popUpTo(HOME_ROUTE) { inclusive = true }
            }
        }
    }
}

fun NavController.navigateToHome() {
    this.navigate(HOME_ROUTE) {
        popUpTo(0)
    }
}
