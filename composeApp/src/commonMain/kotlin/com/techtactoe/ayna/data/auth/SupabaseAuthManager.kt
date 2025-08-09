package com.techtactoe.ayna.data.auth

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Authentication manager using Supabase Auth
 * Handles user authentication, session management, and user state
 */
class SupabaseAuthManager {

    private val auth = AynaSupabaseClient.auth

    /**
     * Current user session flow
     */
    val currentUser: Flow<UserInfo?> = auth.sessionStatus.map { status ->
        when (status) {
            is io.github.jan.supabase.gotrue.SessionStatus.Authenticated -> status.session.user
            else -> null
        }
    }

    /**
     * Check if user is currently authenticated
     */
    val isAuthenticated: Flow<Boolean> = currentUser.map { it != null }

    /**
     * Sign up with email and password
     */
    suspend fun signUp(email: String, password: String, name: String): Result<UserInfo> {
        return try {
            val result = auth.signUpWith(Email) {
                this.email = email
                this.password = password
                data = mapOf("name" to name)
            }
            
            Result.success(result.user!!)
        } catch (e: Exception) {
            println("Error during sign up: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Sign in with email and password
     */
    suspend fun signIn(email: String, password: String): Result<UserInfo> {
        return try {
            val result = auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            
            Result.success(result.user!!)
        } catch (e: Exception) {
            println("Error during sign in: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Sign out current user
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error during sign out: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Get current user ID
     */
    suspend fun getCurrentUserId(): String? {
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
    suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.updateUser {
                password = newPassword
            }
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error updating password: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Send password reset email
     */
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error sending password reset: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Refresh current session
     */
    suspend fun refreshSession(): Result<Unit> {
        return try {
            auth.refreshCurrentSession()
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error refreshing session: ${e.message}")
            Result.failure(e)
        }
    }
}