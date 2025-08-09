package com.techtactoe.ayna.data.auth

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
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
    val currentUser: Flow<UserInfo?> = auth.sessionStatus.map { status: io.github.jan.supabase.gotrue.SessionStatus ->
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
            // NOTE: The supabase-kt API differs between versions. The current project setup
            // doesn't expose Email DSL properties (email/password/data). We avoid compile errors
            // by returning a clear failure until the Supabase credentials and library version
            // are finalized.
            throw UnsupportedOperationException("Auth signUp not implemented for current supabase-kt version")
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
            throw UnsupportedOperationException("Auth signIn not implemented for current supabase-kt version")
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
            throw UnsupportedOperationException("Update password not implemented for current supabase-kt version")
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