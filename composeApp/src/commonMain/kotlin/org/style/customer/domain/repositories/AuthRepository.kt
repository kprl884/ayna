package org.style.customer.domain.repositories

import org.style.customer.domain.models.User

/**
 * Authentication Repository Interface
 * Handles user authentication operations
 */
interface AuthRepository {
    /**
     * Sign in with email and password
     */
    suspend fun signInWithEmail(email: String, password: String): Result<User>
    
    /**
     * Sign up with email and password
     */
    suspend fun signUpWithEmail(email: String, password: String, name: String): Result<User>
    
    /**
     * Sign in with Google
     */
    suspend fun signInWithGoogle(idToken: String): Result<User>
    
    /**
     * Sign out current user
     */
    suspend fun signOut(): Result<Unit>
    
    /**
     * Get current user
     */
    suspend fun getCurrentUser(): User?
    
    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    
    /**
     * Update user profile
     */
    suspend fun updateUserProfile(user: User): Result<User>
    
    /**
     * Delete user account
     */
    suspend fun deleteUserAccount(): Result<Unit>
} 