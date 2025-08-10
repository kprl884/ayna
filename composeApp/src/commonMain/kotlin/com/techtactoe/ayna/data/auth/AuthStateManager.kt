package com.techtactoe.ayna.data.auth

import com.techtactoe.ayna.domain.repository.AuthRepository
import com.techtactoe.ayna.domain.repository.AuthState
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

/**
 * Centralized authentication state manager
 * Provides a single source of truth for authentication state across the app
 */
class AuthStateManager(
    private val authRepository: AuthRepository
) {
    private val scope = CoroutineScope(SupervisorJob())
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        observeAuthState()
    }
    
    /**
     * Observe authentication state changes
     */
    private fun observeAuthState() {
        combine(
            authRepository.isAuthenticated,
            authRepository.currentUser,
            _isLoading
        ) { isAuthenticated, user, isLoading ->
            AuthState(
                isLoading = isLoading,
                isAuthenticated = isAuthenticated,
                user = user,
                error = null
            )
        }.launchIn(scope)
    }
    
    /**
     * Set loading state
     */
    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
    
    /**
     * Set error state
     */
    fun setError(error: com.techtactoe.ayna.domain.repository.AuthError?) {
        _authState.value = _authState.value.copy(error = error)
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
    
    /**
     * Get current user
     */
    fun getCurrentUser(): UserInfo? {
        return _authState.value.user
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        return _authState.value.isAuthenticated
    }
    
    /**
     * Check if authentication is in progress
     */
    fun isLoading(): Boolean {
        return _authState.value.isLoading
    }
}