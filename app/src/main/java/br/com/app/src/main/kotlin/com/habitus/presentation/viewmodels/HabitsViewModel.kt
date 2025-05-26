package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.entity.Days
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import br.com.app.src.main.kotlin.com.habitus.presentation.states.RegisterHabitUiState
import compose.icons.AllIcons
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Smile
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _habits = MutableStateFlow<List<HabitEntity>>(emptyList())
    val habits: StateFlow<List<HabitEntity>> = _habits.asStateFlow()

    // StateFlow para o UiState da tela de registro
    private val _registerUiState = MutableStateFlow(RegisterHabitUiState())
    val registerUiState: StateFlow<RegisterHabitUiState> = _registerUiState.asStateFlow()

    private val allIcons = LineAwesomeIcons.AllIcons


    init {
        // Inicializa o ViewModel obtendo todos os hábitos do repositório
        viewModelScope.launch(Dispatchers.IO) {
            getAllHabits()
        }
    }

    // -- Funções para manipular o estado do registro de hábitos --

    fun onHabitNameChange(name: String) {
        _registerUiState.update { it.copy(habitName = name, error = null) }
    }

    fun onDescriptionChange(description: String) {
        _registerUiState.update { it.copy(habitDescription = description, error = null) }
    }

    fun onPontuationChange(pontuation: String) {
        _registerUiState.update { it.copy(pontuation = pontuation, error = null) }
    }

    fun onFrequencyChange(frequency: String) {
        _registerUiState.update { it.copy(selectedFrequency = frequency, error = null) }
    }

    fun onDaySelected(day: Days) {
        _registerUiState.update { currentState ->
            val currentDays = currentState.selectedDays.toMutableList()
            if (currentDays.contains(day)) {
                currentDays.remove(day)
            } else {
                currentDays.add(day)
            }
            currentState.copy(selectedDays = currentDays)
        }
    }

    fun onToggleDaily(isChecked: Boolean) {
        _registerUiState.update { currentState ->
            currentState.copy(selectedDays = if (isChecked) Days.entries.toList() else emptyList())
        }
    }

    fun onCategoryChanged(category: String) {
        _registerUiState.update { it.copy(selectedCategory = category, error = null) }
    }

    fun onToggleCategoryDropdown(expanded: Boolean) {
        _registerUiState.update { it.copy(isDropdownMenuExpanded = expanded) }
    }

    fun onIconSelected(icon: ImageVector) {
        _registerUiState.update {
            it.copy(
                selectedIcon = icon,
                selectedIconName = icon.name, // Assume que ImageVector tem um 'name'
                showIconSelectorDialog = false
            )
        }
    }

    fun showIconDialog() {
        _registerUiState.update { it.copy(showIconSelectorDialog = true) }
    }

    fun dismissIconDialog() {
        _registerUiState.update { it.copy(showIconSelectorDialog = false) }
    }

    fun resetError() {
        _registerUiState.update { it.copy(error = null) }
    }

    // --- Funções de Lógica de Negócio (Registro) ---

    /**
     * Registra um novo hábito inserindo no banco de dados por meio do repositório.
     *
     * @param habit O objeto HabitEntity que representa o hábito a ser registrado.
     */
    fun attemptRegisterHabit(userId: String) {
        val state = _registerUiState.value
        val pontuationInt = state.pontuation.toIntOrNull()

        // Validação
        if (state.habitName.isBlank()) {
            _registerUiState.update { it.copy(error = "O nome do hábito é obrigatório.") }
            return
        }
        if (pontuationInt == null || pontuationInt <= 0) {
            _registerUiState.update { it.copy(error = "A pontuação deve ser um número maior que zero.") }
            return
        }
        if (state.selectedDays.isEmpty()) {
            _registerUiState.update { it.copy(error = "Selecione pelo menos um dia da semana.") }
            return
        }

        // Se passar na validação, inicia o processo de salvar
        _registerUiState.update { it.copy(isSaving = true, error = null) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val habit = HabitEntity(
                    title = state.habitName,
                    description = state.habitDescription,
                    category = state.selectedCategory,
                    frequency = state.selectedFrequency,
                    pontuation = pontuationInt,
                    isCompleted = false, // Inicialmente, o hábito não está completo
                    days = state.selectedDays.map { it.value },
                    icon = state.selectedIconName ?: LineAwesomeIcons.Smile.name, // Fallback para um ícone padrão
                    userId = userId
                )
                repository.insertHabit(habit)
                _registerUiState.update { it.copy(isSaving = false, saveSuccess = true) }
                getAllHabits() // Atualiza a lista de hábitos após o registro
            } catch (e: Exception) {
                _registerUiState.update { it.copy(isSaving = false, error = "Falha ao salvar o hábito: ${e.message}") }
            }
        }
    }

    /**
     * Reseta o estado da tela de registro para os valores padrão.
     * Pode ser chamado após o salvamento bem-sucedido ou quando o usuário sai da tela.
     */
    fun resetRegisterState() {
        _registerUiState.value = RegisterHabitUiState()
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