package br.com.app.src.main.kotlin.com.habitus.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.app.src.main.kotlin.com.habitus.data.remote.AuthResponse
import br.com.app.src.main.kotlin.com.habitus.data.remote.AuthRepository
import br.com.app.src.main.kotlin.com.habitus.data.repository.UserRepository
import br.com.app.src.main.kotlin.com.habitus.presentation.toUserEntity
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthResponse?>(null)
    val authState: StateFlow<AuthResponse?> = _authState

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user

    private val _email = MutableStateFlow<String>("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow<String>("")
    val password = _password.asStateFlow()

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun onPasswordChange(password: String) {
        _password.value = password
    }

    fun validateEmail(): Boolean {
        return _email.value.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(_email.value).matches()
                && _email.value.length >= 6
    }

    fun validatePassword(): Boolean {
        return _password.value.isNotEmpty() && _password.value.length >= 6
    }

    fun validateForm(): Boolean {
        return validateEmail() && validatePassword()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.loginWithEmail(email, password).collectLatest { response ->
                _authState.value = response
                if(response is AuthResponse.Success) {
                    _user.value = response.user
                    // Após o login, insere o usuário no repositório
                    userRepository.insertUser(user.value!!.toUserEntity())
                }
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            authRepository.createAccountWithEmail(email, password).collectLatest { response ->
                _authState.value = response
                if(response is AuthResponse.Success) {
                    _user.value = response.user
                }
            }
        }
    }

    fun resetState() {
        _authState.value = null
    }

    init {
        _user.value = authRepository.auth.currentUser
    }
}