package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import br.com.app.src.main.kotlin.com.habitus.presentation.screens.RankingRange
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.*
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState

    private val _barChartState = MutableStateFlow(BarChartState())
    val barChartState: StateFlow<BarChartState> = _barChartState

    fun onRangeChange(range: RankingRange) {
        _uiState.update { it.copy(selectedRange = range) }
        loadStats()
        loadChartData(range, _uiState.value.selectedDate)
    }

    fun onDateChange(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
        loadStats()
        loadChartData(_uiState.value.selectedRange, date)
    }

    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        loadStats()
    }

    fun loadStats() {
        viewModelScope.launch {
            val state = _uiState.value
            val (inicio, fim) = getPeriodoRange(state.selectedRange, state.selectedDate)
            val logs = repository.filterHabitsByPeriod(inicio, fim)

            val completados = logs.count { it.isCompleted }
            val total = logs.size
            val pulados = total - completados
            val taxa = if (total > 0) (completados * 100 / total) else 0

            // Para somar pontuação, precisamos dos hábitos
            val allHabits = repository.getAllHabits("userId") // Substitua "userId" pelo ID do usuário atual

            val habitsMap = allHabits.associateBy { it.id }
            val pontos = logs.filter { it.isCompleted }.sumOf { habitsMap[it.habitId]?.pontuation ?: 0 }

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
                start = baseDate.with(DayOfWeek.MONDAY)
                end = baseDate.with(DayOfWeek.SUNDAY)
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

    fun loadChartData(range: RankingRange, referenceDate: LocalDate) {
        viewModelScope.launch {
            val (labels, dadosAtual, dadosAnterior) = when (range) {
                RankingRange.DIARIO -> {
                    val labels = listOf("Ontem", "Hoje")
                    val atual = getSuccessRateForDay(referenceDate)
                    val anterior = getSuccessRateForDay(referenceDate.minusDays(1))
                    Triple(labels, listOf(atual), listOf(anterior))
                }
                RankingRange.SEMANAL -> {
                    val dias = listOf("Seg", "Ter", "Qua", "Qui", "Sex", "Sab", "Dom")

                    val atual = coroutineScope {
                        dias.mapIndexed { index, _ ->
                            async {
                                getSuccessRateForDay(referenceDate.with(DayOfWeek.of(index + 1)))
                            }
                        }.awaitAll()
                    }

                    val anterior = coroutineScope {
                        dias.mapIndexed { index, _ ->
                            async {
                                getSuccessRateForDay(referenceDate.minusWeeks(1).with(DayOfWeek.of(index + 1)))
                            }
                        }.awaitAll()
                    }


                    Triple(dias, atual, anterior)
                }

                RankingRange.MENSAL -> {
                    val labels = listOf("Mês Passado", "Este Mês")
                    val atual = getSuccessRateForMonth(referenceDate)
                    val anterior = getSuccessRateForMonth(referenceDate.minusMonths(1))
                    Triple(labels, listOf(atual), listOf(anterior))
                }
                RankingRange.ANUAL -> {
                    val labels = listOf("Ano Passado", "Este Ano")
                    val atual = getSuccessRateForYear(referenceDate)
                    val anterior = getSuccessRateForYear(referenceDate.minusYears(1))
                    Triple(labels, listOf(atual), listOf(anterior))
                }
            }

            _barChartState.value = BarChartState(
                labels = labels,
                entriesAtual = dadosAtual.mapIndexed { i, v -> BarEntry(i.toFloat(), v) },
                entriesPassado = dadosAnterior.mapIndexed { i, v -> BarEntry(i.toFloat(), v) }
            )
        }
    }

    private suspend fun getSuccessRateForDay(date: LocalDate): Float {
        val start = date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val end = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val logs = repository.filterHabitsByPeriod(start, end)
        val total = logs.size
        val feitos = logs.count { it.isCompleted }
        return if (total > 0) feitos * 100f / total else 0f
    }

    private suspend fun getSuccessRateForMonth(date: LocalDate): Float {
        val start = date.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val end = date.withDayOfMonth(date.lengthOfMonth()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val logs = repository.filterHabitsByPeriod(start, end)
        val total = logs.size
        val feitos = logs.count { it.isCompleted }
        return if (total > 0) feitos * 100f / total else 0f
    }

    private suspend fun getSuccessRateForYear(date: LocalDate): Float {
        val start = date.withDayOfYear(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val end = date.withDayOfYear(date.lengthOfYear()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val logs = repository.filterHabitsByPeriod(start, end)
        val total = logs.size
        val feitos = logs.count { it.isCompleted }
        return if (total > 0) feitos * 100f / total else 0f
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

data class BarChartState(
    val labels: List<String> = emptyList(),
    val entriesAtual: List<BarEntry> = emptyList(),
    val entriesPassado: List<BarEntry> = emptyList()
)
