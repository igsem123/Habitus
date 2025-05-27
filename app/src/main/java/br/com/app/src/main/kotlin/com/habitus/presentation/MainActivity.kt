@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.app.src.main.kotlin.com.habitus.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.components.TopAppBarForHomeScreen
import br.com.app.src.main.kotlin.com.habitus.presentation.components.TopAppBarForOtherScreens
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.HOME_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.INITIAL_FORM_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.REGISTER_HABITS_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.homeNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.initialFormNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToRegisterHabits
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.registerHabitsNavigation
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitusTheme {
                Habitus()
            }
        }
    }
}

@Composable
fun Habitus(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navRoute = navBackStackEntry?.destination?.route
    val user = UserEntity(
        email = "jota.jota@gmail.com",
        password = "password123",
        uid = "daoksdaskookasdko",
        username = "jotinha123",
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            when (navRoute) {
                INITIAL_FORM_ROUTE -> TopAppBarForOtherScreens(isBackIconVisible = false)
                HOME_ROUTE -> TopAppBarForHomeScreen(
                    user = user,
                    onNavigateToRegisterHabits = {
                        navController.navigateToRegisterHabits()
                    }
                )
                REGISTER_HABITS_ROUTE -> TopAppBarForOtherScreens(
                    title = "Novo Hábito",
                    onIconClick = {
                        navController.popBackStack()
                    },
                    isBackIconVisible = true
                )
                else -> TopAppBarForOtherScreens()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = INITIAL_FORM_ROUTE,
            ) {
                initialFormNavigation(navController)
                homeNavigation(navController)
                registerHabitsNavigation(navController)
            }
        }
    }
}