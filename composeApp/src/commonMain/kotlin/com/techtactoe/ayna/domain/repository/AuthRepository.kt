package com.techtactoe.ayna.domain.repository

import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * Enhanced cross-platform auth repository API for Supabase-based authentication
 * Supports multiple authentication methods and comprehensive session management
 */
interface AuthRepository {
    val currentUser: Flow<UserInfo?>
    val isAuthenticated: Flow<Boolean>

    // Email/Password Authentication
    suspend fun signUp(email: String, password: String, name: String? = null): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<UserInfo>
    
    // Social Authentication
    suspend fun signInWithGoogle(idToken: String): Result<UserInfo>
    suspend fun signInWithApple(idToken: String): Result<UserInfo>
    
    // Session Management
    suspend fun signOut(): Result<Unit>
    suspend fun refreshSession(): Result<Unit>
    suspend fun getCurrentUserId(): String?
    
    // Password Management
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    
    // Magic Link & OTP Authentication
    suspend fun signInWithMagicLink(email: String): Result<Unit>
    suspend fun verifyOTP(email: String, token: String): Result<UserInfo>
    
    // Email Verification
    suspend fun isEmailConfirmed(): Boolean
    suspend fun resendConfirmationEmail(): Result<Unit>
    
    // Profile Management
    suspend fun updateUserProfile(updates: Map<String, Any>): Result<Unit>
}

/**
 * Authentication error types for better error handling
 */
sealed class AuthError : Exception() {
    data object InvalidCredentials : AuthError()
    data object EmailNotConfirmed : AuthError()
    data object WeakPassword : AuthError()
    data object EmailAlreadyExists : AuthError()
    data object UserNotFound : AuthError()
    data object NetworkError : AuthError()
    data object RateLimitExceeded : AuthError()
    data class UnknownError(override val message: String) : AuthError()
}

/**
 * Authentication state for UI
 */
data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: UserInfo? = null,
    val error: AuthError? = null
)

/**
 * Social authentication result
 */
sealed class SocialAuthResult {
    data class Success(val user: UserInfo) : SocialAuthResult()
    data class Error(val error: AuthError) : SocialAuthResult()
    data object Cancelled : SocialAuthResult()
}

/**
 * Password validation result
 */
data class PasswordValidation(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    companion object {
        fun validate(password: String): PasswordValidation {
            val errors = mutableListOf<String>()
            
            if (password.length < 8) {
                errors.add("Password must be at least 8 characters long")
            }
            if (!password.any { it.isDigit() }) {
                errors.add("Password must contain at least one number")
            }
            if (!password.any { it.isLetter() }) {
                errors.add("Password must contain at least one letter")
            }
            if (!password.any { it.isUpperCase() }) {
                errors.add("Password must contain at least one uppercase letter")
            }
            
            return PasswordValidation(
                isValid = errors.isEmpty(),
                errors = errors
            )
        }
    }
}

/**
 * Email validation utility
 */
object EmailValidator {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
    
    fun isValid(email: String): Boolean {
        return email.isNotBlank() && EMAIL_REGEX.matches(email)
    }
    
    fun validate(email: String): String? {
        return when {
            email.isBlank() -> "Email cannot be empty"
            !EMAIL_REGEX.matches(email) -> "Please enter a valid email address"
            else -> null
        }
    }
}
