package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.entity.Days
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitEntity
import br.com.app.src.main.kotlin.com.habitus.data.entity.HabitLogEntity
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitLogRepository
import br.com.app.src.main.kotlin.com.habitus.data.repository.HabitRepository
import br.com.app.src.main.kotlin.com.habitus.data.repository.UserRepository
import br.com.app.src.main.kotlin.com.habitus.presentation.states.RegisterHabitUiState
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Smile
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class HabitsViewModel @Inject constructor(
    private val repository: HabitRepository,
    private val userRepository: UserRepository,
    private val habitLogRepository: HabitLogRepository
) : ViewModel() {
    // StateFlow para o UiState da tela de registro
    private val _registerUiState = MutableStateFlow(RegisterHabitUiState())
    val registerUiState: StateFlow<RegisterHabitUiState> = _registerUiState.asStateFlow()

    // 1. Flow para todos os hábitos vindos do repositório.
    private val _allHabits = MutableStateFlow<List<HabitEntity>>(emptyList())

    // 2. Flow para a data selecionada no calendário.
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    // 3. Flow que combina os dois acima para gerar a lista de hábitos do dia.
    //    Recalculado AUTOMATICAMENTE sempre que _allHabits ou _selectedDate mudar.
    val habitsForSelectedDay: StateFlow<List<HabitEntity>> =
        combine(_allHabits, _selectedDate) { habits, date ->
            // --- CORREÇÃO APLICADA AQUI ---

            // 1. Pega o dia da semana do calendário (ex: MONDAY, TUESDAY, etc.)
            val javaDay = date.dayOfWeek

            // 2. Converte o dia do calendário para o valor correspondente no seu Enum Days
            val dayValueInYourSystem = when (javaDay) {
                java.time.DayOfWeek.SUNDAY -> 1
                java.time.DayOfWeek.MONDAY -> 2
                java.time.DayOfWeek.TUESDAY -> 3
                java.time.DayOfWeek.WEDNESDAY -> 4
                java.time.DayOfWeek.THURSDAY -> 5
                java.time.DayOfWeek.FRIDAY -> 6
                java.time.DayOfWeek.SATURDAY -> 7
            }

            Log.d(
                "ViewModel_Filter",
                "Data: $date. Convertido para o dia do nosso sistema: $dayValueInYourSystem"
            )

            // 3. Filtra usando o valor convertido e correto
            habits.filter { it.days.contains(dayValueInYourSystem) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 4. Contadores derivados DIRETAMENTE do Flow de hábitos filtrados.
    val totalTasksCount: StateFlow<Int> = habitsForSelectedDay.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completedTasksCount: StateFlow<Int> =
        habitsForSelectedDay.map { it.count { habit -> habit.isCompleted } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Inicializa o ViewModel obtendo o ID do usuário atual
    private var currentUserId: String? = null

    init {
        viewModelScope.launch {
            // 1. Observa o fluxo de usuário do repositório
            userRepository.currentUser.collect { user ->
                // 2. Apenas executa a lógica se o usuário não for nulo
                if (user != null) {
                    // Guarda o ID do usuário para uso em outras funções
                    currentUserId = user.uid
                    Log.d(
                        "HabitsViewModel",
                        "Usuário válido detectado: ${user.uid}. Carregando hábitos..."
                    )
                    // 3. Chama o loadAllHabits com o ID correto
                    loadAllHabits(user.uid)
                } else {
                    // Caso o usuário faça logout
                    currentUserId = null
                    _allHabits.value = emptyList() // Limpa a lista de hábitos
                    Log.d("HabitsViewModel", "Usuário tornou-se nulo. Limpando hábitos.")
                }
            }

            generateDailyLogsIfNeeded() // <-- para popular a tabela HabitWithLogs com os hábitos que existem naquele dia
        }
    }

    // Função para a UI chamar quando uma nova data for selecionada.
    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    private fun loadAllHabits(userId: String) {
        viewModelScope.launch {
            Log.d("HabitsViewModel", "Carregando hábitos para o usuário: $userId")
            repository.getAllHabits(userId).collect { habitsFromDb ->
                _allHabits.value = habitsFromDb
                Log.d("HabitsViewModel", "Hábitos carregados: ${habitsFromDb.size}")
            }
        }
    }

    // Funções para manipular o estado do registro de hábitos
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

    // Funções de Lógica de Negócio (Registro)

    /** Função para registrar diretamente um hábito pré-definido
     * @param userId O ID do usuário que está registrando o hábito.
     * @param habit O objeto HabitEntity que representa o hábito a ser registrado.
     */
    fun registerPredefinedHabit(userId: String, habit: HabitEntity) {
        _registerUiState.update { it.copy(isSaving = true, error = null) }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Cria uma nova instância com o userId correto
                val habitToInsert = habit.copy(userId = userId)

                repository.insertHabit(habitToInsert)
                _registerUiState.update { it.copy(isSaving = false, saveSuccess = true) }

                // Recarrega todos os hábitos após o registro
                Log.d("HabitsViewModel", "Recarregando hábitos após registro")

                loadAllHabits(userId)
            } catch (e: Exception) {
                _registerUiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Falha ao salvar o hábito: ${e.message}"
                    )
                }
            }
        }
    }

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
                    icon = state.selectedIconName
                        ?: LineAwesomeIcons.Smile.name, // Fallback para um ícone padrão
                    userId = userId
                )
                repository.insertHabit(habit)
                _registerUiState.update { it.copy(isSaving = false, saveSuccess = true) }

                loadAllHabits(userId)
            } catch (e: Exception) {
                _registerUiState.update {
                    it.copy(
                        isSaving = false,
                        error = "Falha ao salvar o hábito: ${e.message}"
                    )
                }
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
     * Atualiza um hábito existente no banco de dados por meio do repositório.
     *
     * @param habit O objeto HabitEntity que contém as informações atualizadas do hábito.
     */
    fun updateHabit(habit: HabitEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateHabit(habit)

                currentUserId?.let { uid ->
                    loadAllHabits(uid)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Exclui um hábito do banco de dados por meio do repositório.
     *
     * @param habitId O ID do hábito a ser excluído.
     */
    fun deleteHabit(habitId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Chama o repositório para excluir o hábito
                repository.deleteHabit(habitId)

                currentUserId?.let { uid ->
                    loadAllHabits(uid)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // FUNCAO QUE MARCA COMO FEITO O HABITO DE HOJE
    fun checkHabitForToday(habit: HabitEntity, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val today = getTodayDateMillis()
            val log = habitLogRepository.getLogForHabitAndDate(habit.id, today)

            // Crie uma cópia do hábito com o estado de conclusão atualizado
            val updatedHabit = habit.copy(isCompleted = isCompleted)
            // Atualiza o hábito no repositório com o objeto atualizado
            repository.updateHabit(updatedHabit)

            currentUserId?.let { uid ->
                loadAllHabits(uid)
            }

            if (log != null) {
                val updatedLog = log.copy(isCompleted = isCompleted)
                habitLogRepository.insertLog(updatedLog) // substitui (porque é onConflict = REPLACE)
            } else {
                // fallback: cria um log se não existir (opcional)
                habitLogRepository.insertLog(
                    HabitLogEntity(
                        habitId = habit.id,
                        date = today,
                        isCompleted = isCompleted
                    )
                )
            }
        }
    }


    // FUNCOES PARA REGISTAR E POPULAR A TABELA  HabitWithLogs

    // Oq a função  "generateDailyLogsIfNeeded()" pretende fazer:
    //Ao iniciar o app, ele gera os logs do dia automaticamente;
    //Ele verifica quais hábitos devem ser feitos hoje;
    //Evita duplicatas (checa se o log já existe);
    //Salva com isCompleted = false e aguarda o usuário marcar como feito.

    fun generateDailyLogsIfNeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            val todayMillis = getTodayDateMillis()

            currentUserId?.let { uid ->
                val allHabits = repository.getAllHabits(uid)

                val currentDayOfWeek = java.util.Calendar.getInstance()
                    .get(java.util.Calendar.DAY_OF_WEEK) // 1 = Domingo

                allHabits.collect { habit ->
                    habit.forEach {
                        if (currentDayOfWeek in it.days) {
                            val existing =
                                habitLogRepository.getLogForHabitAndDate(it.id, todayMillis)
                            if (existing == null) {
                                habitLogRepository.insertLog(
                                    HabitLogEntity(
                                        habitId = it.id,
                                        date = todayMillis,
                                        isCompleted = false
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    //ver SE/ONDE já existe alguma função parecida
    private fun getTodayDateMillis(): Long {
        val calendar = java.util.Calendar.getInstance()
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
        calendar.set(java.util.Calendar.MINUTE, 0)
        calendar.set(java.util.Calendar.SECOND, 0)
        calendar.set(java.util.Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}