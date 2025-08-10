package com.techtactoe.ayna.domain.repository

import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * Cross-platform auth repository API for Supabase-based authentication
 */
interface AuthRepository {
    val currentUser: Flow<UserInfo?>
    val isAuthenticated: Flow<Boolean>

    suspend fun signUp(email: String, password: String, name: String? = null): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<UserInfo>
    suspend fun signOut(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun refreshSession(): Result<Unit>
    suspend fun getCurrentUserId(): String?
}
