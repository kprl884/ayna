package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.AuthError
import com.techtactoe.ayna.domain.model.EmailValidator
import com.techtactoe.ayna.domain.model.LoginRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * LoginViewModel with comprehensive state management
 * Implements MVVM pattern with reactive form validation and proper error handling
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginContract.UiState())
    val uiState: StateFlow<LoginContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<LoginContract.UiEffect>()
    val uiEffect: SharedFlow<LoginContract.UiEffect> = _uiEffect.asSharedFlow()

    init {
        // Setup reactive form validation
        setupFormValidation()
    }

    fun onEvent(event: LoginContract.UiEvent) {
        when (event) {
            is LoginContract.UiEvent.OnEmailChanged -> {
                updateEmail(event.email)
            }
            
            is LoginContract.UiEvent.OnPasswordChanged -> {
                updatePassword(event.password)
            }
            
            is LoginContract.UiEvent.OnRememberMeChanged -> {
                _uiState.value = _uiState.value.copy(rememberMe = event.rememberMe)
            }
            
            is LoginContract.UiEvent.OnTogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    isPasswordVisible = !_uiState.value.isPasswordVisible
                )
            }
            
            is LoginContract.UiEvent.OnLoginClicked -> {
                performLogin()
            }
            
            is LoginContract.UiEvent.OnForgotPasswordClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(LoginContract.UiEffect.NavigateToForgotPassword)
                }
            }
            
            is LoginContract.UiEvent.OnSignUpClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(LoginContract.UiEffect.NavigateToSignUp)
                }
            }
            
            is LoginContract.UiEvent.OnGoogleSignInClicked -> {
                performGoogleSignIn()
            }
            
            is LoginContract.UiEvent.OnAppleSignInClicked -> {
                performAppleSignIn()
            }
            
            is LoginContract.UiEvent.OnErrorDismissed -> {
                _uiState.value = _uiState.value.copy(generalError = null)
            }
        }
    }

    private fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            emailError = null,
            generalError = null
        )
        validateEmail(email)
    }

    private fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            passwordError = null,
            generalError = null
        )
        validatePassword(password)
    }

    private fun validateEmail(email: String) {
        if (email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = null)
            return
        }

        val validation = EmailValidator.validate(email)
        if (!validation.isValid) {
            _uiState.value = _uiState.value.copy(emailError = validation.errorMessage)
        } else {
            _uiState.value = _uiState.value.copy(emailError = null)
        }
    }

    private fun validatePassword(password: String) {
        if (password.isBlank()) {
            _uiState.value = _uiState.value.copy(passwordError = null)
            return
        }

        if (password.length < 6) {
            _uiState.value = _uiState.value.copy(
                passwordError = "Password must be at least 6 characters"
            )
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
    }

    private fun setupFormValidation() {
        viewModelScope.launch {
            combine(
                uiState,
                uiState
            ) { state, _ ->
                val isEmailValid = EmailValidator.isValid(state.email) && state.emailError == null
                val isPasswordValid = state.password.length >= 6 && state.passwordError == null
                val isFormValid = isEmailValid && isPasswordValid && !state.isLoading

                if (state.isLoginButtonEnabled != isFormValid) {
                    _uiState.value = state.copy(isLoginButtonEnabled = isFormValid)
                }
            }
        }
    }

    private fun performLogin() {
        val currentState = _uiState.value
        
        // Validate all fields before proceeding
        if (!validateForm()) {
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            generalError = null
        )

        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(
                    email = currentState.email,
                    password = currentState.password,
                    rememberMe = currentState.rememberMe
                )

                // Simulate API call
                kotlinx.coroutines.delay(2000)
                
                // Mock login logic - replace with actual repository call
                if (currentState.email == "user@example.com" && currentState.password == "password123") {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _uiEffect.emit(LoginContract.UiEffect.NavigateToHome)
                    _uiEffect.emit(LoginContract.UiEffect.ShowSnackbar("Welcome back!"))
                } else {
                    throw AuthError.InvalidCredentials
                }
                
            } catch (error: AuthError) {
                handleAuthError(error)
            } catch (exception: Exception) {
                handleAuthError(AuthError.UnknownError(exception.message ?: "Login failed"))
            }
        }
    }

    private fun performGoogleSignIn() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                // Simulate Google Sign In
                kotlinx.coroutines.delay(1500)
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEffect.emit(LoginContract.UiEffect.NavigateToHome)
                _uiEffect.emit(LoginContract.UiEffect.ShowSnackbar("Signed in with Google"))
                
            } catch (error: AuthError) {
                handleAuthError(error)
            } catch (exception: Exception) {
                handleAuthError(AuthError.UnknownError("Google Sign In failed"))
            }
        }
    }

    private fun performAppleSignIn() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                // Simulate Apple Sign In
                kotlinx.coroutines.delay(1500)
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEffect.emit(LoginContract.UiEffect.NavigateToHome)
                _uiEffect.emit(LoginContract.UiEffect.ShowSnackbar("Signed in with Apple"))
                
            } catch (error: AuthError) {
                handleAuthError(error)
            } catch (exception: Exception) {
                handleAuthError(AuthError.UnknownError("Apple Sign In failed"))
            }
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _uiState.value
        var isValid = true
        var emailError: String? = null
        var passwordError: String? = null

        // Validate email
        val emailValidation = EmailValidator.validate(currentState.email)
        if (!emailValidation.isValid) {
            emailError = emailValidation.errorMessage
            isValid = false
        }

        // Validate password
        if (currentState.password.isBlank()) {
            passwordError = "Password is required"
            isValid = false
        } else if (currentState.password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        }

        _uiState.value = currentState.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        return isValid
    }

    private fun handleAuthError(error: AuthError) {
        _uiState.value = _uiState.value.copy(isLoading = false)
        
        viewModelScope.launch {
            when (error) {
                is AuthError.InvalidCredentials -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = "Invalid email or password"
                    )
                }
                is AuthError.UserNotFound -> {
                    _uiState.value = _uiState.value.copy(
                        emailError = "No account found with this email"
                    )
                }
                is AuthError.EmailNotVerified -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = "Please verify your email before signing in"
                    )
                }
                is AuthError.AccountLocked -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = "Account is temporarily locked. Please try again later."
                    )
                }
                is AuthError.NetworkError -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = "Network error. Please check your connection."
                    )
                }
                is AuthError.ServerError -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = "Server error. Please try again later."
                    )
                }
                is AuthError.ValidationError -> {
                    when (error.field) {
                        "email" -> _uiState.value = _uiState.value.copy(emailError = error.message)
                        "password" -> _uiState.value = _uiState.value.copy(passwordError = error.message)
                        else -> _uiState.value = _uiState.value.copy(generalError = error.message)
                    }
                }
                is AuthError.UnknownError -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = error.message
                    )
                }
                else -> {
                    _uiState.value = _uiState.value.copy(
                        generalError = "An unexpected error occurred"
                    )
                }
            }
            
            _uiEffect.emit(LoginContract.UiEffect.ShowError(error))
        }
    }
}
