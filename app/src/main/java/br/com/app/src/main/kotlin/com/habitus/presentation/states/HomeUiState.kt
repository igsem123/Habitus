package br.com.app.src.main.kotlin.com.habitus.presentation.states

import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity

data class HomeUiState(
    var habits: List<HabitEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)