package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.presentation.components.CalendarioComponent
import br.com.app.src.main.kotlin.com.habitus.presentation.components.CardHabits
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.HabitsViewModel
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.SettingsViewModel
import br.com.app.src.main.kotlin.com.habitus.ui.theme.HabitusTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    viewModel: HabitsViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel
) {
    val habitsForDay by viewModel.habitsForSelectedDay.collectAsState()
    val completed by viewModel.completedTasksCount.collectAsState()
    val total by viewModel.totalTasksCount.collectAsState()
    val darkTheme by settingsViewModel.darkTheme.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberLazyListState(),
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            CalendarioComponent(
                modifier = Modifier.padding(horizontal = 8.dp),
                onSelectedDay = { selectedDate ->
                    // Atualiza o estado do ViewModel com a data selecionada
                    viewModel.viewModelScope.launch {
                        viewModel.onDateSelected(selectedDate)
                    }
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Meta diária
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                val progress by animateFloatAsState(
                    targetValue = if (total > 0) completed.toFloat() / total else 0f,
                    label = "progress"
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularWavyProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp),
                        trackColor = if (darkTheme)
                            MaterialTheme.colorScheme.tertiary
                        else
                            MaterialTheme.colorScheme.primaryContainer,
                        color = MaterialTheme.colorScheme.onPrimary,
                        waveSpeed = 8.dp
                    )
                    Text(
                        text = "Sua meta diária!\n$completed de $total hábitos",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Lista de hábitos
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(2.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = MaterialTheme.colorScheme.surfaceBright,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hábitos",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                habitsForDay.forEach { habit ->
                    CardHabits(
                        habit = habit,
                        //comentei pq a isso atualiza todos os habitos e nao só o habito de hoje
//                        onCheckHabit = {
//                            viewModel.checkHabit(it, habit)
//                        },
                        onCheckHabit = { isChecked ->
                            viewModel.checkHabitForToday(habit.id, isChecked)
                        },
                        onDeleteHabit = {
                            viewModel.deleteHabit(habit.id)
                        },
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HabitsScreenPreview() {
    HabitusTheme {
        HomeScreen(
            settingsViewModel = hiltViewModel<SettingsViewModel>()
        )
    }
}
