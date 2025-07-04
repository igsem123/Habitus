package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.app.src.main.kotlin.com.habitus.data.entity.UserEntity
import br.com.app.src.main.kotlin.com.habitus.presentation.helpers.toWeekdayNamesPtList
import br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels.HabitsViewModel
import br.com.app.src.main.kotlin.com.habitus.sample.sampleHabits
import compose.icons.AllIcons
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.AngleDoubleRightSolid
import compose.icons.lineawesomeicons.Smile
import androidx.compose.runtime.getValue
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun PreRegisterHabitsScreen(
    onNavigateToRegisterHabits: () -> Unit = { },
    viewModel: HabitsViewModel = hiltViewModel(),
    user: UserEntity?,
    onHabitRegistered: () -> Unit = { },
    @ApplicationContext context: Context = androidx.compose.ui.platform.LocalContext.current
) {

    val uiState by viewModel.registerUiState.collectAsStateWithLifecycle() // Coleta o estado do ViewModel

    // Efeito para lidar com o sucesso do salvamento ou erros
    LaunchedEffect(uiState.saveSuccess, uiState.error) {
        if (uiState.saveSuccess) {
            Toast.makeText(context, "Hábito registrado com sucesso!", Toast.LENGTH_SHORT).show()
            onHabitRegistered() // Executa a ação de callback
            viewModel.resetRegisterState()
        }
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.resetError() // Limpa o erro após exibição
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp), // Espaçamento entre os itens
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Deseja registrar seus hábitos?",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    OutlinedButton(
                        onClick = { onNavigateToRegisterHabits() },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary.copy(0.4f)
                        )
                    ) {
                        Text(
                            text = "Registrar Hábitos",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = LineAwesomeIcons.AngleDoubleRightSolid,
                            contentDescription = "Ir para registro de hábitos",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(24.dp), // Tamanho do ícone
                            tint = MaterialTheme.colorScheme.primary.copy(0.4f)
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Ou deseja selecionar hábitos pré-definidos?",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        items(sampleHabits.count()) { index ->
            val habit = sampleHabits[index]
            val iconMap = LineAwesomeIcons.AllIcons.associateBy { it.name }
            val icon = iconMap[habit.icon] ?: LineAwesomeIcons.Smile // Fallback to Smile icon if not found

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.elevatedCardElevation(1.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                onClick = { viewModel.registerPredefinedHabit(user?.uid ?: "", habit) }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = "Ícone do hábito",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(end = 8.dp),
                            tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                        )
                        Text(
                            text = habit.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = habit.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start,
                    )
                    val days = habit.days.toWeekdayNamesPtList()
                    Text(
                        text = "Dias: ${days.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Pontuação: ${habit.pontuation}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}