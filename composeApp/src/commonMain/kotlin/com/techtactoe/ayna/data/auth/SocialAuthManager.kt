package com.techtactoe.ayna.data.auth

/**
 * Common interface for platform-specific social authentication managers
 */
expect class SocialAuthManager() {
    /**
     * Request Google Sign-In (Android) or Apple Sign-In (iOS)
     */
    suspend fun requestSocialSignIn(): String
    
    /**
     * Sign out from social authentication
     */
    fun signOutFromSocial()
}
