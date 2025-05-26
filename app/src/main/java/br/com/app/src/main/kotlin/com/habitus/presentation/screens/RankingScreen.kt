package br.com.app.src.main.kotlin.com.habitus.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import java.time.LocalDate


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter


@Composable
fun RankingScreen(
    selectedRange: RankingRange = RankingRange.SEMANAL,
    selectedDate: LocalDate = LocalDate.now(),
    selectedHabit: String = "Todos os hábitos",
    onRangeChange: (RankingRange) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onHabitChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Selecionador de tipo de ranking
        RankingTypeSelector(selectedRange, onRangeChange)

        Spacer(modifier = Modifier.height(16.dp))

        // Texto com data atual e setas para navegar
        DateNavigator(selectedRange, selectedDate, onDateChange)

        Spacer(modifier = Modifier.height(16.dp))

        // Card com estatísticas
        StatsCard(
            selectedHabit = selectedHabit,
            onHabitChange = onHabitChange,
            stats = HabitStats(85, 120, 12, 3) // Dados fictícios
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card para gráfico (placeholder)
        Card(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Text("Gráfico será implementado aqui")
            }
        }
    }
}

@Composable
fun RankingTypeSelector(
    selected: RankingRange,
    onSelect: (RankingRange) -> Unit
) {
    val options = listOf(RankingRange.DIARIO, RankingRange.SEMANAL, RankingRange.MENSAL, RankingRange.ANUAL)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        options.forEach {
            Button(
                onClick = { onSelect(it) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (it == selected) Color.Blue else Color.LightGray
                )
            ) {
                Text(it.displayName)
            }
        }
    }
}

@Composable
fun DateNavigator(
    range: RankingRange,
    currentDate: LocalDate,
    onDateChange: (LocalDate) -> Unit
) {
    val formatter = range.getFormatter()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { onDateChange(range.previous(currentDate)) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Anterior")
        }
        Text("Essa ${range.displayName.lowercase()} - ${currentDate.format(formatter)}")
        IconButton(onClick = { onDateChange(range.next(currentDate)) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Próxima")
        }
    }
}

@Composable
fun StatsCard(
    selectedHabit: String,
    onHabitChange: (String) -> Unit,
    stats: HabitStats
) {
    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Dropdown de hábito
            var expanded by remember { mutableStateOf(false) }
            Box {
                Text(selectedHabit, modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true })
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Todos os hábitos", "Exercício", "Estudo", "Meditação").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            onHabitChange(it)
                            expanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Linha 1
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Taxa de sucesso: ${stats.successRate}%")
                Text("Pontos: ${stats.points}")
            }

            // Linha 2
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Completados: ${stats.completed}")
                Text("Pulados: ${stats.skipped}")
            }
        }
    }
}

enum class RankingRange(val displayName: String) {
    DIARIO("Diário"),
    SEMANAL("Semanal"),
    MENSAL("Mensal"),
    ANUAL("Anual");

    fun previous(date: LocalDate): LocalDate = when (this) {
        DIARIO -> date.minusDays(1)
        SEMANAL -> date.minusWeeks(1)
        MENSAL -> date.minusMonths(1)
        ANUAL -> date.minusYears(1)
    }

    fun next(date: LocalDate): LocalDate = when (this) {
        DIARIO -> date.plusDays(1)
        SEMANAL -> date.plusWeeks(1)
        MENSAL -> date.plusMonths(1)
        ANUAL -> date.plusYears(1)
    }

    fun getFormatter(): DateTimeFormatter = when (this) {
        DIARIO -> DateTimeFormatter.ofPattern("dd/MM/yyyy")
        SEMANAL -> DateTimeFormatter.ofPattern("'semana de' dd/MM")
        MENSAL -> DateTimeFormatter.ofPattern("MMMM/yyyy")
        ANUAL -> DateTimeFormatter.ofPattern("yyyy")
    }
}

data class HabitStats(
    val successRate: Int,
    val points: Int,
    val completed: Int,
    val skipped: Int
)