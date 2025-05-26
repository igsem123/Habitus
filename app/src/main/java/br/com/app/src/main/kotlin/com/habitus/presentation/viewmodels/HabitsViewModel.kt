package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits

    init {
        // Inicializa o ViewModel obtendo todos os hábitos do repositório
        viewModelScope.launch(Dispatchers.IO) {
            getAllHabits()
        }
    }

    /**
     * Obtém todos os hábitos registrados no banco de dados por meio do repositório.
     *
     * @return Uma lista de HabitEntity representando todos os hábitos.
     */
    suspend fun getAllHabits(): List<HabitEntity> {
        val habitsList = repository.getAllHabits()
        _habits.value = habitsList
        return habitsList
    }

    /**
     * Registra um novo hábito inserindo no banco de dados por meio do repositório.
     *
     * @param habit O objeto HabitEntity que representa o hábito a ser registrado.
     */
    fun registerHabit(habit: HabitEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHabit(habit)
        }
    }

    /**
     * Atualiza um hábito existente no banco de dados por meio do repositório.
     *
     * @param habit O objeto HabitEntity que contém as informações atualizadas do hábito.
     */
    suspend fun updateHabit(habit: HabitEntity) {
        repository.updateHabit(habit)
        getAllHabits() // Atualiza a lista de hábitos após a atualização
    }

    /**
     * Exclui um hábito do banco de dados por meio do repositório.
     *
     * @param habitId O ID do hábito a ser excluído.
     */
    suspend fun deleteHabit(habitId: String) {
        repository.deleteHabit(habitId)
        getAllHabits() // Atualiza a lista de hábitos após a exclusão
    }
}