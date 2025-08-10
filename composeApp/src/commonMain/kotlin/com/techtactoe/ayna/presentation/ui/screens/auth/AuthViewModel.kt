package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.data.auth.SupabaseAuthManager
import com.techtactoe.ayna.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val auth: AuthRepository = SupabaseAuthManager()
) : ViewModel() {

    data class UiState(
        val email: String = "",
        val password: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<UserInfo?> = auth.currentUser.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        null
    )
    val isAuthenticated: StateFlow<Boolean> = auth.isAuthenticated.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        false
    )

    val navigationEvents = MutableSharedFlow<String>(extraBufferCapacity = 1)

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstName = value)
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastName = value)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun refreshSession() {
        viewModelScope.launch {
            auth.refreshSession()
        }
    }

    fun signIn(onSuccess: () -> Unit = {}) {
        val (email, password) = _uiState.value.let { it.email to it.password }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val res = auth.signIn(email, password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess {
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Login failed")
            }
        }
    }

    fun signUp(onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        val email: String = state.email
        val password: String = state.password
        val firstName: String = state.firstName.trim()
        val lastName: String = state.lastName.trim()

        val name: String? = listOf(firstName, lastName)
            .filter { it.isNotBlank() }
            .joinToString(" ")
            .ifBlank { null }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val res = auth.signUp(email, password, name)
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess {
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Registration failed")
            }
        }
    }

    fun resetPassword(onSuccess: () -> Unit = {}) {
        val email = _uiState.value.email
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val res = auth.resetPassword(email)
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess { onSuccess() }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Reset failed")
                }
        }
    }

    fun signOut(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val res = auth.signOut()
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess { onSuccess() }
                .onFailure { e ->
                    _uiState.value =
                        _uiState.value.copy(errorMessage = e.message ?: "Logout failed")
                }
        }
    }
}
