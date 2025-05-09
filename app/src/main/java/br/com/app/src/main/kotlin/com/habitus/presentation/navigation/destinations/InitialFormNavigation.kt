package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.InitialFormScreen

internal const val INITIAL_FORM_ROUTE = "initial_form"

internal fun NavGraphBuilder.initialFormNavigation(navController: NavHostController) {
    composable(
        route = INITIAL_FORM_ROUTE,
    ) {
        InitialFormScreen(
            modifier = Modifier,
            onNavigateToHome = {
                navController.navigateToHome()
            },
        )
    }
}

fun NavController.navigateToInitialForm() {
    this.navigate(INITIAL_FORM_ROUTE) {
        popUpTo(INITIAL_FORM_ROUTE) {
            inclusive = true
        }
    }
}