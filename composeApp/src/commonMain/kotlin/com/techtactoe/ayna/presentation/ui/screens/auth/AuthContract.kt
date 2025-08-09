package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.runtime.Stable
import com.techtactoe.ayna.domain.model.AuthError
import com.techtactoe.ayna.domain.model.User

/**
 * Authentication Contracts following MVVM pattern with Clean Architecture
 * Comprehensive state management for Login and SignUp flows
 */

// MARK: - Login Contract
interface LoginContract {
    
    @Stable
    data class UiState(
        val email: String = "",
        val password: String = "",
        val rememberMe: Boolean = false,
        val isLoading: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val generalError: String? = null,
        val isLoginButtonEnabled: Boolean = false
    ) {
        val hasErrors: Boolean = emailError != null || passwordError != null || generalError != null
    }
    
    sealed interface UiEvent {
        // Input Events
        data class OnEmailChanged(val email: String) : UiEvent
        data class OnPasswordChanged(val password: String) : UiEvent
        data class OnRememberMeChanged(val rememberMe: Boolean) : UiEvent
        data object OnTogglePasswordVisibility : UiEvent
        
        // Action Events  
        data object OnLoginClicked : UiEvent
        data object OnForgotPasswordClicked : UiEvent
        data object OnSignUpClicked : UiEvent
        data object OnGoogleSignInClicked : UiEvent
        data object OnAppleSignInClicked : UiEvent
        
        // Error Events
        data object OnErrorDismissed : UiEvent
    }
    
    sealed interface UiEffect {
        data object NavigateToHome : UiEffect
        data object NavigateToSignUp : UiEffect
        data object NavigateToForgotPassword : UiEffect
        data class ShowSnackbar(val message: String) : UiEffect
        data class ShowError(val error: AuthError) : UiEffect
    }
}

// MARK: - SignUp Contract
interface SignUpContract {
    
    @Stable
    data class UiState(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val acceptTerms: Boolean = false,
        val subscribeToNewsletter: Boolean = false,
        val isLoading: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val isConfirmPasswordVisible: Boolean = false,
        val firstNameError: String? = null,
        val lastNameError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null,
        val confirmPasswordError: String? = null,
        val termsError: String? = null,
        val generalError: String? = null,
        val passwordStrength: com.techtactoe.ayna.domain.model.PasswordStrength? = null,
        val isSignUpButtonEnabled: Boolean = false
    ) {
        val hasErrors: Boolean = listOfNotNull(
            firstNameError, lastNameError, emailError, passwordError, 
            confirmPasswordError, termsError, generalError
        ).isNotEmpty()
    }
    
    sealed interface UiEvent {
        // Input Events
        data class OnFirstNameChanged(val firstName: String) : UiEvent
        data class OnLastNameChanged(val lastName: String) : UiEvent
        data class OnEmailChanged(val email: String) : UiEvent
        data class OnPasswordChanged(val password: String) : UiEvent
        data class OnConfirmPasswordChanged(val confirmPassword: String) : UiEvent
        data class OnAcceptTermsChanged(val acceptTerms: Boolean) : UiEvent
        data class OnSubscribeNewsletterChanged(val subscribe: Boolean) : UiEvent
        data object OnTogglePasswordVisibility : UiEvent
        data object OnToggleConfirmPasswordVisibility : UiEvent
        
        // Action Events
        data object OnSignUpClicked : UiEvent
        data object OnLoginClicked : UiEvent
        data object OnTermsClicked : UiEvent
        data object OnPrivacyPolicyClicked : UiEvent
        data object OnGoogleSignUpClicked : UiEvent
        data object OnAppleSignUpClicked : UiEvent
        
        // Error Events
        data object OnErrorDismissed : UiEvent
    }
    
    sealed interface UiEffect {
        data object NavigateToLogin : UiEffect
        data object NavigateToEmailVerification : UiEffect
        data object NavigateToTerms : UiEffect
        data object NavigateToPrivacyPolicy : UiEffect
        data class ShowSnackbar(val message: String) : UiEffect
        data class ShowError(val error: AuthError) : UiEffect
    }
}

// MARK: - Forgot Password Contract
interface ForgotPasswordContract {
    
    @Stable
    data class UiState(
        val email: String = "",
        val isLoading: Boolean = false,
        val emailError: String? = null,
        val generalError: String? = null,
        val isEmailSent: Boolean = false,
        val isSendButtonEnabled: Boolean = false
    )
    
    sealed interface UiEvent {
        data class OnEmailChanged(val email: String) : UiEvent
        data object OnSendResetEmailClicked : UiEvent
        data object OnBackToLoginClicked : UiEvent
        data object OnResendEmailClicked : UiEvent
        data object OnErrorDismissed : UiEvent
    }
    
    sealed interface UiEffect {
        data object NavigateToLogin : UiEffect
        data class ShowSnackbar(val message: String) : UiEffect
        data class ShowError(val error: AuthError) : UiEffect
    }
}

// MARK: - Email Verification Contract
interface EmailVerificationContract {
    
    @Stable
    data class UiState(
        val email: String,
        val isLoading: Boolean = false,
        val isVerified: Boolean = false,
        val generalError: String? = null,
        val canResend: Boolean = true,
        val resendCountdown: Int = 0
    )
    
    sealed interface UiEvent {
        data object OnResendEmailClicked : UiEvent
        data object OnChangeEmailClicked : UiEvent
        data object OnSkipClicked : UiEvent
        data object OnContinueClicked : UiEvent
        data object OnErrorDismissed : UiEvent
    }
    
    sealed interface UiEffect {
        data object NavigateToLogin : UiEffect
        data object NavigateToHome : UiEffect
        data class ShowSnackbar(val message: String) : UiEffect
        data class ShowError(val error: AuthError) : UiEffect
    }
}

// MARK: - Common Authentication State
@Stable
data class AuthenticationState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: AuthError? = null
)

// MARK: - Validation State
@Stable
data class ValidationState(
    val isValid: Boolean = false,
    val errors: Map<String, String> = emptyMap()
) {
    fun getError(field: String): String? = errors[field]
    fun hasError(field: String): Boolean = errors.containsKey(field)
}
