package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.remote.AuthResponde
import br.com.app.src.main.kotlin.com.habitus.data.remote.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthResponde?>(null)
    val authState: StateFlow<AuthResponde?> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            firebaseAuth.loginWithEmail(email, password).collectLatest { response ->
                _authState.value = response
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            firebaseAuth.createAccountWithEmail(email, password).collectLatest { response ->
                _authState.value = response
            }
        }
    }

    fun resetState() {
        _authState.value = null
    }
}