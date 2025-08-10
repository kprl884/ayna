package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.data.auth.SocialAuthManager
import com.techtactoe.ayna.data.auth.SupabaseAuthManager
import com.techtactoe.ayna.domain.repository.AuthError
import com.techtactoe.ayna.domain.repository.AuthRepository
import com.techtactoe.ayna.domain.repository.EmailValidator
import com.techtactoe.ayna.domain.repository.PasswordValidation
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Enhanced AuthViewModel with comprehensive authentication features
 * Supports email/password, social auth, magic links, and OTP verification
 */
class AuthViewModel(
    private val auth: AuthRepository = SupabaseAuthManager(),
    private val socialAuth: SocialAuthManager = SocialAuthManager()
) : ViewModel() {

    data class UiState(
        val email: String = "",
        val password: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isEmailValid: Boolean = true,
        val passwordValidation: PasswordValidation = PasswordValidation(true),
        val showPassword: Boolean = false,
        val authMethod: AuthMethod = AuthMethod.EMAIL_PASSWORD,
        val otpSent: Boolean = false,
        val otpCode: String = "",
        val magicLinkSent: Boolean = false
    )
    
    enum class AuthMethod {
        EMAIL_PASSWORD,
        MAGIC_LINK,
        OTP,
        GOOGLE,
        APPLE
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    @Suppress("unused")
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

    @Suppress("unused")
    val navigationEvents = MutableSharedFlow<String>(extraBufferCapacity = 1)

    fun onEmailChange(value: String) {
        val isValid = EmailValidator.isValid(value)
        _uiState.value = _uiState.value.copy(
            email = value,
            isEmailValid = isValid,
            errorMessage = if (!isValid && value.isNotEmpty()) EmailValidator.validate(value) else null
        )
    }

    fun onPasswordChange(value: String) {
        val validation = PasswordValidation.validate(value)
        _uiState.value = _uiState.value.copy(
            password = value,
            passwordValidation = validation
        )
    }

    fun onFirstNameChange(value: String) {
        _uiState.value = _uiState.value.copy(firstName = value)
    }

    fun onLastNameChange(value: String) {
        _uiState.value = _uiState.value.copy(lastName = value)
    }

    fun onOtpCodeChange(value: String) {
        _uiState.value = _uiState.value.copy(otpCode = value)
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(showPassword = !_uiState.value.showPassword)
    }

    fun setAuthMethod(method: AuthMethod) {
        _uiState.value = _uiState.value.copy(authMethod = method)
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
                _uiState.value =
                    _uiState.value.copy(errorMessage = e.message ?: "Registration failed")
            }
        }
    }

    fun signInWithMagicLink(onSuccess: () -> Unit = {}) {
        val email = _uiState.value.email
        if (!EmailValidator.isValid(email)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Cast to SupabaseAuthManager to access magic link method
            val supabaseAuth = auth as? SupabaseAuthManager
            val res = supabaseAuth?.signInWithMagicLink(email) ?: Result.failure(Exception("Magic link not supported"))
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess {
                _uiState.value = _uiState.value.copy(magicLinkSent = true)
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Magic link failed")
            }
        }
    }

    fun verifyOTP(onSuccess: () -> Unit = {}) {
        val email = _uiState.value.email
        val otpCode = _uiState.value.otpCode
        
        if (otpCode.length != 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter a valid 6-digit code")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Cast to SupabaseAuthManager to access OTP method
            val supabaseAuth = auth as? SupabaseAuthManager
            val res = supabaseAuth?.verifyOTP(email, otpCode) ?: Result.failure(Exception("OTP not supported"))
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess {
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "OTP verification failed")
            }
        }
    }

    fun resendConfirmationEmail(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Cast to SupabaseAuthManager to access resend method
            val supabaseAuth = auth as? SupabaseAuthManager
            val res = supabaseAuth?.resendConfirmationEmail() ?: Result.failure(Exception("Resend not supported"))
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            res.onSuccess {
                onSuccess()
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Failed to resend email")
            }
        }
    }

    fun checkEmailConfirmation(): Boolean {
        return viewModelScope.launch {
            val supabaseAuth = auth as? SupabaseAuthManager
            supabaseAuth?.isEmailConfirmed() ?: false
        }.let { false } // Simplified for now
    }

    fun signInWithGoogle(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val idToken = socialAuth.requestSocialSignIn()
                val res = auth.signInWithGoogle(idToken)
                _uiState.value = _uiState.value.copy(isLoading = false)
                res.onSuccess {
                    onSuccess()
                }.onFailure { e ->
                    _uiState.value =
                        _uiState.value.copy(errorMessage = e.message ?: "Google sign-in failed")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Google sign-in failed"
                )
            }
        }
    }

    fun signInWithApple(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val idToken = socialAuth.requestSocialSignIn()
                val res = auth.signInWithApple(idToken)
                _uiState.value = _uiState.value.copy(isLoading = false)
                res.onSuccess {
                    onSuccess()
                }.onFailure { e ->
                    _uiState.value =
                        _uiState.value.copy(errorMessage = e.message ?: "Apple sign-in failed")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Apple sign-in failed"
                )
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
            res.onSuccess {
                socialAuth.signOutFromSocial()
                onSuccess()
            }.onFailure { e ->
                _uiState.value =
                    _uiState.value.copy(errorMessage = e.message ?: "Logout failed")
            }
        }
    }

    /**
     * Validate form before submission
     */
    fun validateForm(): Boolean {
        val state = _uiState.value
        val emailValid = EmailValidator.isValid(state.email)
        val passwordValid = state.passwordValidation.isValid
        
        if (!emailValid) {
            _uiState.value = state.copy(errorMessage = "Please enter a valid email address")
            return false
        }
        
        if (!passwordValid && state.authMethod == AuthMethod.EMAIL_PASSWORD) {
            _uiState.value = state.copy(errorMessage = state.passwordValidation.errors.firstOrNull())
            return false
        }
        
        return true
    }

    /**
     * Get user-friendly error message
     */
    private fun getErrorMessage(error: Throwable): String {
        return when {
            error.message?.contains("invalid", ignoreCase = true) == true -> "Invalid email or password"
            error.message?.contains("network", ignoreCase = true) == true -> "Network error. Please check your connection."
            error.message?.contains("rate limit", ignoreCase = true) == true -> "Too many attempts. Please try again later."
            else -> error.message ?: "An unexpected error occurred"
        }
    }
}
