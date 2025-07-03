package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.PreRegisterHabitsScreen

internal const val PRE_REGISTER_HABITS_ROUTE = "pre_register_habits"

internal fun NavGraphBuilder.preRegisterHabitsNavigation(
    navController: NavHostController,
    user: UserEntity?
) {
    composable(route = PRE_REGISTER_HABITS_ROUTE) {
        PreRegisterHabitsScreen(
            onNavigateToRegisterHabits = {
                navController.navigateToRegisterHabits()
            },
            user = user,
            onHabitRegistered = {
                navController.navigateToHome()
            }
        )
    }
}

fun NavController.navigateToPreRegisterHabits() {
    this.navigate(PRE_REGISTER_HABITS_ROUTE)
}
