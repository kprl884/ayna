package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techtactoe.ayna.domain.model.AuthError
import com.techtactoe.ayna.domain.model.EmailValidator
import com.techtactoe.ayna.domain.model.NameValidator
import com.techtactoe.ayna.domain.model.PasswordRequirements
import com.techtactoe.ayna.domain.model.SignUpRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * SignUpViewModel with comprehensive form validation
 * Implements real-time validation, password strength checking, and proper error handling
 */
class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpContract.UiState())
    val uiState: StateFlow<SignUpContract.UiState> = _uiState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SignUpContract.UiEffect>()
    val uiEffect: SharedFlow<SignUpContract.UiEffect> = _uiEffect.asSharedFlow()

    init {
        // Setup reactive form validation
        setupFormValidation()
    }

    fun onEvent(event: SignUpContract.UiEvent) {
        when (event) {
            is SignUpContract.UiEvent.OnFirstNameChanged -> {
                updateFirstName(event.firstName)
            }
            
            is SignUpContract.UiEvent.OnLastNameChanged -> {
                updateLastName(event.lastName)
            }
            
            is SignUpContract.UiEvent.OnEmailChanged -> {
                updateEmail(event.email)
            }
            
            is SignUpContract.UiEvent.OnPasswordChanged -> {
                updatePassword(event.password)
            }
            
            is SignUpContract.UiEvent.OnConfirmPasswordChanged -> {
                updateConfirmPassword(event.confirmPassword)
            }
            
            is SignUpContract.UiEvent.OnAcceptTermsChanged -> {
                _uiState.value = _uiState.value.copy(
                    acceptTerms = event.acceptTerms,
                    termsError = null
                )
            }
            
            is SignUpContract.UiEvent.OnSubscribeNewsletterChanged -> {
                _uiState.value = _uiState.value.copy(subscribeToNewsletter = event.subscribe)
            }
            
            is SignUpContract.UiEvent.OnTogglePasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    isPasswordVisible = !_uiState.value.isPasswordVisible
                )
            }
            
            is SignUpContract.UiEvent.OnToggleConfirmPasswordVisibility -> {
                _uiState.value = _uiState.value.copy(
                    isConfirmPasswordVisible = !_uiState.value.isConfirmPasswordVisible
                )
            }
            
            is SignUpContract.UiEvent.OnSignUpClicked -> {
                performSignUp()
            }
            
            is SignUpContract.UiEvent.OnLoginClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(SignUpContract.UiEffect.NavigateToLogin)
                }
            }
            
            is SignUpContract.UiEvent.OnTermsClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(SignUpContract.UiEffect.NavigateToTerms)
                }
            }
            
            is SignUpContract.UiEvent.OnPrivacyPolicyClicked -> {
                viewModelScope.launch {
                    _uiEffect.emit(SignUpContract.UiEffect.NavigateToPrivacyPolicy)
                }
            }
            
            is SignUpContract.UiEvent.OnGoogleSignUpClicked -> {
                performGoogleSignUp()
            }
            
            is SignUpContract.UiEvent.OnAppleSignUpClicked -> {
                performAppleSignUp()
            }
            
            is SignUpContract.UiEvent.OnErrorDismissed -> {
                _uiState.value = _uiState.value.copy(generalError = null)
            }
        }
    }

    private fun updateFirstName(firstName: String) {
        _uiState.value = _uiState.value.copy(
            firstName = firstName,
            firstNameError = null,
            generalError = null
        )
        validateFirstName(firstName)
    }

    private fun updateLastName(lastName: String) {
        _uiState.value = _uiState.value.copy(
            lastName = lastName,
            lastNameError = null,
            generalError = null
        )
        validateLastName(lastName)
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
            generalError = null,
            passwordStrength = PasswordRequirements.getStrength(password)
        )
        validatePassword(password)
        
        // Re-validate confirm password if it's already entered
        val currentState = _uiState.value
        if (currentState.confirmPassword.isNotEmpty()) {
            validateConfirmPassword(currentState.confirmPassword)
        }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null,
            generalError = null
        )
        validateConfirmPassword(confirmPassword)
    }

    private fun validateFirstName(firstName: String) {
        if (firstName.isBlank()) {
            _uiState.value = _uiState.value.copy(firstNameError = null)
            return
        }

        val validation = NameValidator.validate(firstName, "First name")
        if (!validation.isValid) {
            _uiState.value = _uiState.value.copy(firstNameError = validation.errorMessage)
        } else {
            _uiState.value = _uiState.value.copy(firstNameError = null)
        }
    }

    private fun validateLastName(lastName: String) {
        if (lastName.isBlank()) {
            _uiState.value = _uiState.value.copy(lastNameError = null)
            return
        }

        val validation = NameValidator.validate(lastName, "Last name")
        if (!validation.isValid) {
            _uiState.value = _uiState.value.copy(lastNameError = validation.errorMessage)
        } else {
            _uiState.value = _uiState.value.copy(lastNameError = null)
        }
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

        val errors = PasswordRequirements.validate(password)
        if (errors.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(passwordError = errors.first())
        } else {
            _uiState.value = _uiState.value.copy(passwordError = null)
        }
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        val currentState = _uiState.value
        
        if (confirmPassword.isBlank()) {
            _uiState.value = currentState.copy(confirmPasswordError = null)
            return
        }

        if (confirmPassword != currentState.password) {
            _uiState.value = currentState.copy(
                confirmPasswordError = "Passwords do not match"
            )
        } else {
            _uiState.value = currentState.copy(confirmPasswordError = null)
        }
    }

    private fun setupFormValidation() {
        viewModelScope.launch {
            combine(
                uiState,
                uiState
            ) { state, _ ->
                val isFirstNameValid = NameValidator.validate(state.firstName, "First name").isValid
                val isLastNameValid = NameValidator.validate(state.lastName, "Last name").isValid
                val isEmailValid = EmailValidator.isValid(state.email) && state.emailError == null
                val isPasswordValid = PasswordRequirements.isValid(state.password) && state.passwordError == null
                val isConfirmPasswordValid = state.confirmPassword == state.password && state.confirmPasswordError == null
                val areTermsAccepted = state.acceptTerms
                
                val isFormValid = isFirstNameValid && 
                    isLastNameValid && 
                    isEmailValid && 
                    isPasswordValid && 
                    isConfirmPasswordValid && 
                    areTermsAccepted && 
                    !state.isLoading

                if (state.isSignUpButtonEnabled != isFormValid) {
                    _uiState.value = state.copy(isSignUpButtonEnabled = isFormValid)
                }
            }
        }
    }

    private fun performSignUp() {
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
                val signUpRequest = SignUpRequest(
                    firstName = currentState.firstName,
                    lastName = currentState.lastName,
                    email = currentState.email,
                    password = currentState.password,
                    confirmPassword = currentState.confirmPassword,
                    acceptTerms = currentState.acceptTerms,
                    subscribeToNewsletter = currentState.subscribeToNewsletter
                )

                // Simulate API call
                kotlinx.coroutines.delay(2500)
                
                // Mock signup logic - replace with actual repository call
                if (currentState.email == "existing@example.com") {
                    throw AuthError.EmailAlreadyExists
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    _uiEffect.emit(SignUpContract.UiEffect.NavigateToEmailVerification)
                    _uiEffect.emit(SignUpContract.UiEffect.ShowSnackbar("Account created successfully! Please verify your email."))
                }
                
            } catch (error: AuthError) {
                handleAuthError(error)
            } catch (exception: Exception) {
                handleAuthError(AuthError.UnknownError(exception.message ?: "Sign up failed"))
            }
        }
    }

    private fun performGoogleSignUp() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                // Simulate Google Sign Up
                kotlinx.coroutines.delay(1500)
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEffect.emit(SignUpContract.UiEffect.NavigateToEmailVerification)
                _uiEffect.emit(SignUpContract.UiEffect.ShowSnackbar("Account created with Google"))
                
            } catch (error: AuthError) {
                handleAuthError(error)
            } catch (exception: Exception) {
                handleAuthError(AuthError.UnknownError("Google Sign Up failed"))
            }
        }
    }

    private fun performAppleSignUp() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        
        viewModelScope.launch {
            try {
                // Simulate Apple Sign Up
                kotlinx.coroutines.delay(1500)
                
                _uiState.value = _uiState.value.copy(isLoading = false)
                _uiEffect.emit(SignUpContract.UiEffect.NavigateToEmailVerification)
                _uiEffect.emit(SignUpContract.UiEffect.ShowSnackbar("Account created with Apple"))
                
            } catch (error: AuthError) {
                handleAuthError(error)
            } catch (exception: Exception) {
                handleAuthError(AuthError.UnknownError("Apple Sign Up failed"))
            }
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _uiState.value
        var isValid = true
        
        var firstNameError: String? = null
        var lastNameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null
        var termsError: String? = null

        // Validate first name
        val firstNameValidation = NameValidator.validate(currentState.firstName, "First name")
        if (!firstNameValidation.isValid) {
            firstNameError = firstNameValidation.errorMessage
            isValid = false
        }

        // Validate last name
        val lastNameValidation = NameValidator.validate(currentState.lastName, "Last name")
        if (!lastNameValidation.isValid) {
            lastNameError = lastNameValidation.errorMessage
            isValid = false
        }

        // Validate email
        val emailValidation = EmailValidator.validate(currentState.email)
        if (!emailValidation.isValid) {
            emailError = emailValidation.errorMessage
            isValid = false
        }

        // Validate password
        val passwordErrors = PasswordRequirements.validate(currentState.password)
        if (passwordErrors.isNotEmpty()) {
            passwordError = passwordErrors.first()
            isValid = false
        }

        // Validate confirm password
        if (currentState.confirmPassword != currentState.password) {
            confirmPasswordError = "Passwords do not match"
            isValid = false
        }

        // Validate terms acceptance
        if (!currentState.acceptTerms) {
            termsError = "You must accept the terms and conditions"
            isValid = false
        }

        _uiState.value = currentState.copy(
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            termsError = termsError
        )

        return isValid
    }

    private fun handleAuthError(error: AuthError) {
        _uiState.value = _uiState.value.copy(isLoading = false)
        
        viewModelScope.launch {
            when (error) {
                is AuthError.EmailAlreadyExists -> {
                    _uiState.value = _uiState.value.copy(
                        emailError = "An account with this email already exists"
                    )
                }
                is AuthError.WeakPassword -> {
                    _uiState.value = _uiState.value.copy(
                        passwordError = "Password does not meet security requirements"
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
                        "firstName" -> _uiState.value = _uiState.value.copy(firstNameError = error.message)
                        "lastName" -> _uiState.value = _uiState.value.copy(lastNameError = error.message)
                        "email" -> _uiState.value = _uiState.value.copy(emailError = error.message)
                        "password" -> _uiState.value = _uiState.value.copy(passwordError = error.message)
                        "confirmPassword" -> _uiState.value = _uiState.value.copy(confirmPasswordError = error.message)
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
            
            _uiEffect.emit(SignUpContract.UiEffect.ShowError(error))
        }
    }
}
