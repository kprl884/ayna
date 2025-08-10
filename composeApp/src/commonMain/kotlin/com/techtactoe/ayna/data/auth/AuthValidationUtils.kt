package com.techtactoe.ayna.data.auth

/**
 * Utility functions for authentication validation
 * Provides consistent validation across the app
 */
object AuthValidationUtils {
    
    /**
     * Validate email format
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Error("Email is required")
            !isValidEmailFormat(email) -> ValidationResult.Error("Please enter a valid email address")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validate password strength
     */
    fun validatePassword(password: String): ValidationResult {
        val errors = mutableListOf<String>()
        
        if (password.isBlank()) {
            return ValidationResult.Error("Password is required")
        }
        
        if (password.length < 8) {
            errors.add("At least 8 characters")
        }
        
        if (!password.any { it.isDigit() }) {
            errors.add("At least one number")
        }
        
        if (!password.any { it.isLetter() }) {
            errors.add("At least one letter")
        }
        
        if (!password.any { it.isUpperCase() }) {
            errors.add("At least one uppercase letter")
        }
        
        if (!password.any { !it.isLetterOrDigit() }) {
            errors.add("At least one special character")
        }
        
        return if (errors.isEmpty()) {
            ValidationResult.Success
        } else {
            ValidationResult.Error("Password must contain: ${errors.joinToString(", ")}")
        }
    }
    
    /**
     * Validate phone number
     */
    fun validatePhoneNumber(phone: String, countryCode: String = "+90"): ValidationResult {
        val cleanPhone = phone.filter { it.isDigit() }
        
        return when {
            phone.isBlank() -> ValidationResult.Error("Phone number is required")
            cleanPhone.length < 7 -> ValidationResult.Error("Phone number is too short")
            cleanPhone.length > 15 -> ValidationResult.Error("Phone number is too long")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Validate name fields
     */
    fun validateName(name: String, fieldName: String = "Name"): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("$fieldName is required")
            name.length < 2 -> ValidationResult.Error("$fieldName must be at least 2 characters")
            name.length > 50 -> ValidationResult.Error("$fieldName must be less than 50 characters")
            !name.all { it.isLetter() || it.isWhitespace() || it == '\'' || it == '-' } -> 
                ValidationResult.Error("$fieldName contains invalid characters")
            else -> ValidationResult.Success
        }
    }
    
    /**
     * Check if email format is valid
     */
    private fun isValidEmailFormat(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
        return emailRegex.matches(email)
    }
    
    /**
     * Get password strength score (0-4)
     */
    fun getPasswordStrength(password: String): PasswordStrength {
        var score = 0
        val feedback = mutableListOf<String>()
        
        if (password.length >= 8) score++ else feedback.add("Use at least 8 characters")
        if (password.any { it.isDigit() }) score++ else feedback.add("Add numbers")
        if (password.any { it.isLowerCase() }) score++ else feedback.add("Add lowercase letters")
        if (password.any { it.isUpperCase() }) score++ else feedback.add("Add uppercase letters")
        if (password.any { !it.isLetterOrDigit() }) score++ else feedback.add("Add special characters")
        
        val strength = when (score) {
            0, 1 -> PasswordStrengthLevel.VERY_WEAK
            2 -> PasswordStrengthLevel.WEAK
            3 -> PasswordStrengthLevel.MEDIUM
            4 -> PasswordStrengthLevel.STRONG
            else -> PasswordStrengthLevel.VERY_STRONG
        }
        
        return PasswordStrength(strength, feedback)
    }
}

/**
 * Validation result sealed class
 */
sealed class ValidationResult {
    data object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    val isValid: Boolean get() = this is Success
    val errorMessage: String? get() = (this as? Error)?.message
}

/**
 * Password strength levels
 */
enum class PasswordStrengthLevel {
    VERY_WEAK, WEAK, MEDIUM, STRONG, VERY_STRONG
}

/**
 * Password strength result
 */
data class PasswordStrength(
    val level: PasswordStrengthLevel,
    val feedback: List<String>
) {
    val color: androidx.compose.ui.graphics.Color
        @Composable get() = when (level) {
            PasswordStrengthLevel.VERY_WEAK -> androidx.compose.material3.MaterialTheme.colorScheme.error
            PasswordStrengthLevel.WEAK -> androidx.compose.ui.graphics.Color(0xFFFF9800)
            PasswordStrengthLevel.MEDIUM -> androidx.compose.ui.graphics.Color(0xFFFFC107)
            PasswordStrengthLevel.STRONG -> androidx.compose.ui.graphics.Color(0xFF8BC34A)
            PasswordStrengthLevel.VERY_STRONG -> androidx.compose.material3.MaterialTheme.colorScheme.primary
        }
    
    val text: String get() = when (level) {
        PasswordStrengthLevel.VERY_WEAK -> "Very Weak"
        PasswordStrengthLevel.WEAK -> "Weak"
        PasswordStrengthLevel.MEDIUM -> "Medium"
        PasswordStrengthLevel.STRONG -> "Strong"
        PasswordStrengthLevel.VERY_STRONG -> "Very Strong"
    }
}