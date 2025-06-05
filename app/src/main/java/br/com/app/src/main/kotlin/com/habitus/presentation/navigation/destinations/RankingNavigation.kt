package br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.RankingScreen

internal const val RANKING_ROUTE = "ranking"

internal fun NavGraphBuilder.rankingNavigation(navController: NavHostController) {
    composable(route = RANKING_ROUTE) {
        RankingScreen()
    }
}

fun NavController.navigateToRanking() {
    this.navigate(RANKING_ROUTE)
}
