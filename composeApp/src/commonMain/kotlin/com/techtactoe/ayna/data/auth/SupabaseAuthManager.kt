package com.techtactoe.ayna.data.auth

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.providers.Apple
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.providers.builtin.IDToken
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Authentication manager using Supabase Auth
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
        // Create auth user (will send verification email if email confirmations are enabled)
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        // If a session exists immediately (no confirmation required), create or update profile
        val user = auth.currentUserOrNull()
        if (user != null) {
            // Try insert first - explicit type specification
            val profile = mapOf<String, Any?>(
                "id" to user.id,
                "name" to name,
                "email" to email
            )
            runCatching {
                db.from("profiles").insert(profile)
            }.onFailure { _ ->
                // If insert fails (e.g., row exists), try update instead
                runCatching {
                    db.from("profiles").update(
                        mapOf<String, Any?>(
                            "name" to name,
                            "email" to email
                        )
                    ) {
                        filter { eq("id", user.id) }
                    }
                }
            }
        }
        Unit
    }.mapError()

    /**
     * Sign in with email and password
     */
    override suspend fun signIn(email: String, password: String): Result<UserInfo> = runCatching {
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
        // Authenticate with Supabase using Google ID token
        auth.signInWith(IDToken) {
            this.idToken = idToken
            this.provider = Google // Use Google provider enum instead of string
        }
        auth.currentUserOrNull()
            ?: throw IllegalStateException("Unable to retrieve user after Google sign-in")
    }.mapError()

    /**
     * Sign in with Apple using ID token from native Apple Sign-In
     */
    override suspend fun signInWithApple(idToken: String): Result<UserInfo> = runCatching {
        auth.signInWith(IDToken) {
            this.idToken = idToken
            this.provider = Apple
        }
        auth.currentUserOrNull()
            ?: throw IllegalStateException("Unable to retrieve user after Apple sign-in")
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
        auth.updateUser { password = newPassword }
        Unit
    }.mapError()

    /**
     * Send password reset email
     */
    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
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
        else -> t.message ?: "Unexpected error"
    }
    return IllegalStateException(friendly, t)
}