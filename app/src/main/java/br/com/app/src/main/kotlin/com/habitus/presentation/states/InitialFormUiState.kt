package br.com.app.src.main.kotlin.com.habitus.presentation.states

data class FormState(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val senha: String = "",
    val senhaConfirmacao: String = ""
)

sealed class InitialFormUiState {
    object Loading : InitialFormUiState()
    object Success : InitialFormUiState()
    data class Error(val message: String) : InitialFormUiState()
    data class FormError(
        val nomeError: String? = null,
        val emailError: String? = null,
        val telefoneError: String? = null,
        val senhaError: String? = null,
        val senhaConfirmacaoError: String? = null
    ) : InitialFormUiState()
}

