package com.techtactoe.ayna.data.auth

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.providers.Apple
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Enhanced authentication manager using Supabase Auth
 * Handles user authentication, session management, and user state
 */
class SupabaseAuthManager : AuthRepository {

    private val auth = AynaSupabaseClient.auth
    private val db = AynaSupabaseClient.database

    /**
     * Current user session flow
     */
    override val currentUser: Flow<UserInfo?> = auth.sessionStatus.map { status: SessionStatus ->
        when (status) {
            is SessionStatus.Authenticated -> status.session.user
            else -> null
        }
    }

    /**
     * Check if user is currently authenticated
     */
    override val isAuthenticated: Flow<Boolean> = currentUser.map { it != null }

    /**
     * Sign up with email and password
     */
    override suspend fun signUp(
        email: String,
        password: String,
        name: String?
    ): Result<Unit> = runCatching {
        // Validate input
        validateEmail(email)
        validatePassword(password)

        // Create auth user
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            if (name != null) {
                this.data = mapOf("name" to name)
            }
        }

        // Create or update profile if session exists immediately
        val user = auth.currentUserOrNull()
        if (user != null) {
            createOrUpdateProfile(user.id, name, email)
        }
        Unit
    }.mapError()

    /**
     * Sign in with email and password
     */
    override suspend fun signIn(email: String, password: String): Result<UserInfo> = runCatching {
        validateEmail(email)
        
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        
        auth.currentUserOrNull()
            ?: throw IllegalStateException("Unable to retrieve user after sign-in")
    }.mapError()

    /**
     * Sign in with Google using ID token from native Google Sign-In
     */
    override suspend fun signInWithGoogle(idToken: String): Result<UserInfo> = runCatching {
        auth.signInWith(IDToken) {
            this.idToken = idToken
            this.provider = Google
        }
        
        val user = auth.currentUserOrNull()
            ?: throw IllegalStateException("Unable to retrieve user after Google sign-in")
        
        // Create profile if it doesn't exist
        createOrUpdateProfile(user.id, user.userMetadata?.get("name") as? String, user.email)
        
        user
    }.mapError()

    /**
     * Sign in with Apple using ID token from native Apple Sign-In
     */
    override suspend fun signInWithApple(idToken: String): Result<UserInfo> = runCatching {
        auth.signInWith(IDToken) {
            this.idToken = idToken
            this.provider = Apple
        }
        
        val user = auth.currentUserOrNull()
            ?: throw IllegalStateException("Unable to retrieve user after Apple sign-in")
        
        // Create profile if it doesn't exist
        createOrUpdateProfile(user.id, user.userMetadata?.get("name") as? String, user.email)
        
        user
    }.mapError()

    /**
     * Sign in with magic link
     */
    suspend fun signInWithMagicLink(email: String): Result<Unit> = runCatching {
        validateEmail(email)
        
        auth.signInWith(OTP) {
            this.email = email
        }
        Unit
    }.mapError()

    /**
     * Verify OTP code
     */
    suspend fun verifyOTP(email: String, token: String): Result<UserInfo> = runCatching {
        auth.verifyEmailOTP(
            type = io.github.jan.supabase.gotrue.providers.builtin.OTP.Type.EMAIL,
            email = email,
            token = token
        )
        
        auth.currentUserOrNull()
            ?: throw IllegalStateException("Unable to retrieve user after OTP verification")
    }.mapError()

    /**
     * Sign out current user
     */
    override suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error during sign out: ${e.message}")
            Result.failure(mapThrowable(e))
        }
    }

    /**
     * Get current user ID
     */
    override suspend fun getCurrentUserId(): String? {
        return try {
            auth.currentUserOrNull()?.id
        } catch (e: Exception) {
            println("Error getting current user ID: ${e.message}")
            null
        }
    }

    /**
     * Update user password
     */
    override suspend fun updatePassword(newPassword: String): Result<Unit> = runCatching {
        validatePassword(newPassword)
        auth.updateUser { 
            password = newPassword 
        }
        Unit
    }.mapError()

    /**
     * Send password reset email
     */
    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            validateEmail(email)
            auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error sending password reset: ${e.message}")
            Result.failure(mapThrowable(e))
        }
    }

    /**
     * Refresh current session
     */
    override suspend fun refreshSession(): Result<Unit> {
        return try {
            auth.refreshCurrentSession()
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error refreshing session: ${e.message}")
            Result.failure(mapThrowable(e))
        }
    }

    /**
     * Update user profile data
     */
    suspend fun updateUserProfile(updates: Map<String, Any>): Result<Unit> = runCatching {
        auth.updateUser {
            data = updates
        }
        Unit
    }.mapError()

    /**
     * Check if email is confirmed
     */
    suspend fun isEmailConfirmed(): Boolean {
        return auth.currentUserOrNull()?.emailConfirmedAt != null
    }

    /**
     * Resend confirmation email
     */
    suspend fun resendConfirmationEmail(): Result<Unit> = runCatching {
        val user = auth.currentUserOrNull()
            ?: throw IllegalStateException("No authenticated user")
        
        auth.resend(
            type = io.github.jan.supabase.gotrue.providers.builtin.OTP.Type.SIGNUP,
            email = user.email ?: throw IllegalStateException("User email not available")
        )
        Unit
    }.mapError()

    // Private helper functions
    private suspend fun createOrUpdateProfile(userId: String, name: String?, email: String?) {
        try {
            val profile = mapOf<String, Any?>(
                "id" to userId,
                "name" to (name ?: "User"),
                "email" to (email ?: "")
            )
            
            runCatching {
                db.from("profiles").insert(profile)
            }.onFailure { _ ->
                // If insert fails (e.g., row exists), try update instead
                runCatching {
                    db.from("profiles").update(
                        mapOf<String, Any?>(
                            "name" to (name ?: "User"),
                            "email" to (email ?: "")
                        )
                    ) {
                        filter { eq("id", userId) }
                    }
                }
            }
        } catch (e: Exception) {
            println("Error creating/updating profile: ${e.message}")
        }
    }

    private fun validateEmail(email: String) {
        if (email.isBlank()) {
            throw IllegalArgumentException("Email cannot be empty")
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            throw IllegalArgumentException("Invalid email format")
        }
    }

    private fun validatePassword(password: String) {
        if (password.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters long")
        }
        if (!password.any { it.isDigit() }) {
            throw IllegalArgumentException("Password must contain at least one number")
        }
        if (!password.any { it.isLetter() }) {
            throw IllegalArgumentException("Password must contain at least one letter")
        }
    }
}

private fun <T> Result<T>.mapError(): Result<T> = this.fold(
    onSuccess = { Result.success(it) },
    onFailure = { Result.failure(mapThrowable(it)) }
)

private fun mapThrowable(t: Throwable): Throwable {
    val msg = t.message?.lowercase() ?: ""
    val friendly = when {
        "invalid" in msg && "credential" in msg -> "Invalid email or password"
        "email" in msg && "not" in msg && "confirmed" in msg -> "Email not confirmed. Check your inbox."
        "rate" in msg && "limit" in msg -> "Too many attempts. Please try again later."
        "network" in msg || "timeout" in msg -> "Network error. Check your connection and try again."
        "weak" in msg && "password" in msg -> "Password is too weak. Use a stronger password."
        "already" in msg && "registered" in msg -> "This email is already registered. Try signing in instead."
        "user" in msg && "not" in msg && "found" in msg -> "No account found with this email address."
        else -> t.message ?: "Unexpected error occurred"
    }
    return IllegalStateException(friendly, t)
}