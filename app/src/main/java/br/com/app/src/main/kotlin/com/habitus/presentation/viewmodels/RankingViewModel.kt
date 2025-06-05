package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.RankingRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState

    fun onRangeChange(range: RankingRange) {
        _uiState.update { it.copy(selectedRange = range) }
        loadStats()
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        loadStats()
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            val state = _uiState.value

            val (inicio, fim) = getPeriodoRange(state.selectedRange, state.selectedDate)

            // Busca logs do banco
            val logs = repository.filterHabitsByPeriod(inicio, fim)

            // Agrupa por hábito
            val completados = logs.count { it.isCompleted }
            val total = logs.size
            val pulados = total - completados
            val taxa = if (total > 0) (completados * 100 / total) else 0

            // Para somar pontuação, precisamos dos hábitos
            val allHabits = repository.getAllHabits()
            val habitsMap = allHabits.associateBy { it.id }

            val pontos = logs
                .filter { it.isCompleted }
                .sumOf { log -> habitsMap[log.habitId]?.pontuation ?: 0 }

            _uiState.update {
                it.copy(
                    successRate = taxa,
                    points = pontos,
                    completed = completados,
                    skipped = pulados
                )
            }
        }
    }

    private fun getPeriodoRange(range: RankingRange, baseDate: LocalDate): Pair<Long, Long> {
        val zone = ZoneId.systemDefault()
        val start: LocalDate
        val end: LocalDate

        when (range) {
            RankingRange.DIARIO -> {
                start = baseDate
                end = baseDate
            }

            RankingRange.SEMANAL -> {
                start = baseDate.with(java.time.DayOfWeek.MONDAY)
                end = baseDate.with(java.time.DayOfWeek.SUNDAY)
            }

            RankingRange.MENSAL -> {
                start = baseDate.withDayOfMonth(1)
                end = baseDate.withDayOfMonth(baseDate.lengthOfMonth())
            }

            RankingRange.ANUAL -> {
                start = baseDate.withDayOfYear(1)
                end = baseDate.withDayOfYear(baseDate.lengthOfYear())
            }
        }

        val inicioMillis = start.atStartOfDay(zone).toEpochSecond() * 1000
        val fimMillis = end.atStartOfDay(zone).toEpochSecond() * 1000
        return Pair(inicioMillis, fimMillis)
    }
}

data class RankingUiState(
    val selectedRange: RankingRange = RankingRange.SEMANAL,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedCategory: String = "Todos os Hábitos",
    val successRate: Int = 0,
    val points: Int = 0,
    val completed: Int = 0,
    val skipped: Int = 0
)
