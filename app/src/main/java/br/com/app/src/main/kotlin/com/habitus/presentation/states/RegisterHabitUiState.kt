package br.com.app.src.main.kotlin.com.habitus.presentation.states

import androidx.compose.ui.graphics.vector.ImageVector
import br.com.app.src.main.kotlin.com.habitus.data.entity.Days
import compose.icons.AllIcons
import compose.icons.LineAwesomeIcons
import compose.icons.lineawesomeicons.Smile

val listOfCategories = listOf(
    "Desenvolvimento Pessoal",
    "Saúde e Bem-estar",
    "Organização e Produtividade",
    "Trabalho e Estudo",
    "Finanças Pessoais",
    "Relacionamentos e Vida Social",
    "Criatividade e Hobbies",
    "Autocuidado e Bem-estar Emocional",
    "Espiritualidade e Crescimento Espiritual",
)

data class RegisterHabitUiState(
    val habitName: String = "",
    val habitDescription: String = "",
    val selectedIconName: String? = null,
    val selectedIcon: ImageVector = LineAwesomeIcons.Smile,
    val selectedFrequency: String = "Diariamente",
    val selectedDays: List<Days> = emptyList(),
    val pontuation: String = "0",
    val selectedCategory: String = listOfCategories.first(),
    val showIconSelectorDialog: Boolean = false,
    val isDropdownMenuExpanded: Boolean = false,
    val availableIcons: List<ImageVector> = LineAwesomeIcons.AllIcons,
    val frequencyOptions: List<String> = listOf("Diariamente", "Semanalmente", "Mensalmente"),
    val categories: List<String> = listOfCategories,
    val isSaving: Boolean = false, // Para feedback de carregamento
    val saveSuccess: Boolean = false, // Para navegação ou mensagem
    val error: String? = null // Para exibir erros
)