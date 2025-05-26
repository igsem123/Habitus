package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.RegisterHabitsScreen

internal const val REGISTER_HABITS_ROUTE = "register_habits"

internal fun NavGraphBuilder.registerHabitsNavigation(navController: NavHostController) {
    composable(
        route = REGISTER_HABITS_ROUTE,
    ) {
        val user = UserEntity(
            email = "jota.jota@gmail.com",
            password = "password123",
            uid = "daoksdaskookasdko",
            username = "jotinha123",
        )
        RegisterHabitsScreen(
            user = user,
            onHabitRegistered = {
                navController.navigateToHome()
            }
        )
    }
}

fun NavController.navigateToRegisterHabits() {
    this.navigate(REGISTER_HABITS_ROUTE) {
        popUpTo(REGISTER_HABITS_ROUTE) {
            inclusive = true
        }
    }
}
