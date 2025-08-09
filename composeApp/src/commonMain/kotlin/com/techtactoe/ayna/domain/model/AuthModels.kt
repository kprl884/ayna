package com.techtactoe.ayna.domain.model

import androidx.compose.runtime.Stable

/**
 * Authentication domain models following SOLID principles
 * Clean architecture with proper separation of concerns
 */

@Stable
data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long? = null
) {
    val fullName: String get() = "$firstName $lastName"
    val initials: String get() = "${firstName.firstOrNull()?.uppercase()}${lastName.firstOrNull()?.uppercase()}"
}

@Stable
data class LoginRequest(
    val email: String,
    val password: String,
    val rememberMe: Boolean = false
)

@Stable
data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val acceptTerms: Boolean = false,
    val subscribeToNewsletter: Boolean = false
)

@Stable
data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long,
    val scope: String? = null
)

@Stable
data class AuthResponse(
    val user: User,
    val token: AuthToken,
    val isFirstLogin: Boolean = false
)

sealed class AuthError : Exception() {
    data object InvalidCredentials : AuthError()
    data object UserNotFound : AuthError()
    data object EmailAlreadyExists : AuthError()
    data object WeakPassword : AuthError()
    data object NetworkError : AuthError()
    data object ServerError : AuthError()
    data object EmailNotVerified : AuthError()
    data object AccountLocked : AuthError()
    data object InvalidToken : AuthError()
    data object TokenExpired : AuthError()
    data class ValidationError(val field: String, override val message: String) : AuthError()
    data class UnknownError(override val message: String) : AuthError()
}

// Validation Results
@Stable
data class FieldValidation(
    val isValid: Boolean,
    val errorMessage: String? = null
)

@Stable
data class FormValidation(
    val isValid: Boolean,
    val fieldErrors: Map<String, String> = emptyMap()
) {
    fun getFieldError(field: String): String? = fieldErrors[field]
    fun hasFieldError(field: String): Boolean = fieldErrors.containsKey(field)
}

// Auth Session State
@Stable
data class AuthSession(
    val user: User? = null,
    val token: AuthToken? = null,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val lastActivity: Long = System.currentTimeMillis()
) {
    val isExpired: Boolean
        get() = token?.let { 
            System.currentTimeMillis() > (lastActivity + it.expiresIn * 1000) 
        } ?: true
    
    val isActive: Boolean
        get() = isAuthenticated && !isExpired
}

// Password Requirements
object PasswordRequirements {
    const val MIN_LENGTH = 8
    const val MAX_LENGTH = 128
    
    fun validate(password: String): List<String> {
        val errors = mutableListOf<String>()
        
        if (password.length < MIN_LENGTH) {
            errors.add("Password must be at least $MIN_LENGTH characters long")
        }
        
        if (password.length > MAX_LENGTH) {
            errors.add("Password must be less than $MAX_LENGTH characters long")
        }
        
        if (!password.any { it.isUpperCase() }) {
            errors.add("Password must contain at least one uppercase letter")
        }
        
        if (!password.any { it.isLowerCase() }) {
            errors.add("Password must contain at least one lowercase letter")
        }
        
        if (!password.any { it.isDigit() }) {
            errors.add("Password must contain at least one number")
        }
        
        if (!password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) }) {
            errors.add("Password must contain at least one special character")
        }
        
        return errors
    }
    
    fun isValid(password: String): Boolean = validate(password).isEmpty()
    
    fun getStrength(password: String): PasswordStrength {
        val score = calculateScore(password)
        return when {
            score < 2 -> PasswordStrength.WEAK
            score < 4 -> PasswordStrength.MEDIUM
            score < 6 -> PasswordStrength.STRONG
            else -> PasswordStrength.VERY_STRONG
        }
    }
    
    private fun calculateScore(password: String): Int {
        var score = 0
        
        if (password.length >= MIN_LENGTH) score++
        if (password.length >= 12) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isLowerCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) }) score++
        
        return score
    }
}

enum class PasswordStrength(val color: androidx.compose.ui.graphics.Color, val label: String) {
    WEAK(androidx.compose.ui.graphics.Color(0xFFE53935), "Weak"),
    MEDIUM(androidx.compose.ui.graphics.Color(0xFFFB8C00), "Medium"),
    STRONG(androidx.compose.ui.graphics.Color(0xFF43A047), "Strong"),
    VERY_STRONG(androidx.compose.ui.graphics.Color(0xFF2E7D32), "Very Strong")
}

// Email Validation
object EmailValidator {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    
    fun isValid(email: String): Boolean {
        return email.isNotBlank() && email.matches(emailRegex)
    }
    
    fun validate(email: String): FieldValidation {
        return when {
            email.isBlank() -> FieldValidation(false, "Email is required")
            !isValid(email) -> FieldValidation(false, "Please enter a valid email address")
            else -> FieldValidation(true)
        }
    }
}

// Name Validation
object NameValidator {
    fun validate(name: String, fieldName: String = "Name"): FieldValidation {
        return when {
            name.isBlank() -> FieldValidation(false, "$fieldName is required")
            name.length < 2 -> FieldValidation(false, "$fieldName must be at least 2 characters")
            name.length > 50 -> FieldValidation(false, "$fieldName must be less than 50 characters")
            !name.all { it.isLetter() || it.isWhitespace() || it == '\'' || it == '-' } -> 
                FieldValidation(false, "$fieldName can only contain letters, spaces, hyphens, and apostrophes")
            else -> FieldValidation(true)
        }
    }
}
