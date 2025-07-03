@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.app.src.main.kotlin.com.habitus.presentation

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.components.BottomAppBar
import br.com.app.src.main.kotlin.com.habitus.presentation.components.TopAppBarForHomeScreen
import br.com.app.src.main.kotlin.com.habitus.presentation.components.TopAppBarForOtherScreens
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.HOME_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.INITIAL_FORM_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.RANKING_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.REGISTER_HABITS_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.SETTINGS_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.homeNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.initialFormNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToHome
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToRanking
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToRegisterHabits
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToSettings
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.rankingNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.registerHabitsNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.AuthViewModel
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.settingsNavigation
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val prefs = getSharedPreferences("settings", Context.MODE_PRIVATE)
            val darkMode = prefs.getBoolean("dark_theme", false)

            HabitusTheme(darkTheme = darkMode) {
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

    val authViewModel: AuthViewModel = hiltViewModel()
    val firebaseUser by authViewModel.user.collectAsState()

    val user = firebaseUser?.toUserEntity()

    val startDestination = if (firebaseUser != null) HOME_ROUTE else INITIAL_FORM_ROUTE

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            when (navRoute) {
                INITIAL_FORM_ROUTE -> TopAppBarForOtherScreens(isBackIconVisible = false)
                HOME_ROUTE -> {
                    if(user != null) {
                        TopAppBarForHomeScreen(
                            user = user,
                            onNavigateToRegisterHabits = {
                                navController.navigateToRegisterHabits()
                            }
                        )
                    } else {
                        TopAppBarForOtherScreens()
                    }
                }

                REGISTER_HABITS_ROUTE -> TopAppBarForOtherScreens(
                    title = "Novo Hábito",
                    onIconClick = {
                        navController.popBackStack()
                    },
                    isBackIconVisible = true
                )

                RANKING_ROUTE -> TopAppBarForOtherScreens(
                    title = "Ranking",
                    isBackIconVisible = false
                )

                SETTINGS_ROUTE -> TopAppBarForOtherScreens(
                    title = "Configurações",
                    isBackIconVisible = false
                )

                else -> TopAppBarForOtherScreens(
                    isBackIconVisible = false
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                initialFormNavigation(navController)
                homeNavigation(navController)
                registerHabitsNavigation(navController)
                rankingNavigation(navController)
                settingsNavigation(navController)
            }

            if (navRoute != INITIAL_FORM_ROUTE) {
                BottomAppBar(
                    onHomeClick = {
                        navController.navigateToHome()
                    },
                    onRankingClick = {
                        navController.navigateToRanking()
                    },
                    onSettingsClick = {
                        navController.navigateToSettings()
                    }
                )
            }
        }
    }
}

fun FirebaseUser.toUserEntity(): UserEntity {
    return UserEntity(
        email = email.orEmpty(),
        password = "", // Nunca armazene senha real!
        uid = uid,
        username = displayName?.takeIf { it.isNotBlank() }
            ?: email.orEmpty().substringBefore("@")
    )
}