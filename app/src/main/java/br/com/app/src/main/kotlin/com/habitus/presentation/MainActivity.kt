@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.app.src.main.kotlin.com.habitus.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.app.src.main.kotlin.com.habitus.R
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.INITIAL_FORM_ROUTE
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.homeNavigation
import br.com.app.src.main.kotlin.com.habitus.presentation.navigation.destinations.initialFormNavigation
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Habitus(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Habitus(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navRoute = navController.currentDestination?.route

    /**
     * Essa variável controla a exibição do TopAppBar, se o valor for false, o TopAppBar não será exibido.
     */
    val isShowTopAppBar = when (navRoute) {
        INITIAL_FORM_ROUTE -> false
        else -> true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (!isShowTopAppBar) {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_app),
                            contentDescription = "Logo Habitus",
                            modifier = Modifier
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }

        NavHost(
            navController = navController,
            startDestination = INITIAL_FORM_ROUTE,
        ) {
            initialFormNavigation(navController)
            homeNavigation(navController)
        }
    }
}
