@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.app.src.main.kotlin.com.habitus.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.PRE_REGISTER_HABITS_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.RANKING_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.REGISTER_HABITS_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.SETTINGS_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.homeNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.initialFormNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToHome
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToInitialForm
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToPreRegisterHabits
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToRanking
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.navigateToSettings
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.preRegisterHabitsNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.rankingNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.registerHabitsNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.AuthViewModel
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.settingsNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel = hiltViewModel<SettingsViewModel>()
            val darkMode by settingsViewModel.darkTheme.collectAsState()

            HabitusTheme(darkTheme = darkMode) {
                Habitus(settingsViewModel = settingsViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Habitus(
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val navRoute = navBackStackEntry?.destination?.route

    val authViewModel: AuthViewModel = hiltViewModel()
    val firebaseUser by authViewModel.user.collectAsState()

    val user = firebaseUser?.toUserEntity()

    val startDestination = if (firebaseUser != null) HOME_ROUTE else INITIAL_FORM_ROUTE
    val scrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

    // Uso do LaunchedEffect para navegar quando o estado de autenticação mudar
    LaunchedEffect(firebaseUser) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (firebaseUser != null && currentRoute != HOME_ROUTE) {
            navController.navigateToHome()
        } else if (firebaseUser == null && currentRoute != INITIAL_FORM_ROUTE) {
            navController.navigateToInitialForm()
        }
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            when (navRoute) {
                INITIAL_FORM_ROUTE -> TopAppBarForOtherScreens(isBackIconVisible = false)
                HOME_ROUTE -> {
                    if(user != null) {
                        TopAppBarForHomeScreen(
                            user = user,
                            onNavigateToPreRegisterHabits = {
                                navController.navigateToPreRegisterHabits()
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

                PRE_REGISTER_HABITS_ROUTE -> TopAppBarForOtherScreens(
                    title = "Pré-Registro de Hábitos",
                    onIconClick = {
                        navController.popBackStack()
                    },
                    isBackIconVisible = true
                )

                else -> TopAppBarForOtherScreens(
                    isBackIconVisible = false
                )
            }
        },
        bottomBar = {
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
                    },
                    scrollBehavior = scrollBehavior,
                )
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.BottomCenter
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                initialFormNavigation(navController)
                homeNavigation(settingsViewModel)
                registerHabitsNavigation(navController, user)
                rankingNavigation(navController)
                settingsNavigation(navController, settingsViewModel)
                preRegisterHabitsNavigation(navController, user)
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