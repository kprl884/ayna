package com.techtactoe.ayna.data.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Handles deep links for authentication flows
 * Supports magic links, OAuth callbacks, and email verification
 */
object DeepLinkHandler {
    
    private val _authDeepLinks = MutableSharedFlow<AuthDeepLink>()
    val authDeepLinks: SharedFlow<AuthDeepLink> = _authDeepLinks.asSharedFlow()
    
    /**
     * Handle incoming deep link
     */
    suspend fun handleDeepLink(url: String) {
        try {
            val uri = parseUrl(url)
            
            when {
                // Magic link authentication
                url.contains("/auth/callback") -> {
                    val accessToken = uri.getQueryParameter("access_token")
                    val refreshToken = uri.getQueryParameter("refresh_token")
                    val type = uri.getQueryParameter("type")
                    
                    if (accessToken != null && refreshToken != null) {
                        _authDeepLinks.emit(
                            AuthDeepLink.MagicLink(
                                accessToken = accessToken,
                                refreshToken = refreshToken,
                                type = type ?: "magiclink"
                            )
                        )
                    }
                }
                
                // Email verification
                url.contains("/auth/confirm") -> {
                    val token = uri.getQueryParameter("token")
                    val type = uri.getQueryParameter("type")
                    
                    if (token != null) {
                        _authDeepLinks.emit(
                            AuthDeepLink.EmailConfirmation(
                                token = token,
                                type = type ?: "signup"
                            )
                        )
                    }
                }
                
                // Password reset
                url.contains("/auth/reset") -> {
                    val accessToken = uri.getQueryParameter("access_token")
                    val refreshToken = uri.getQueryParameter("refresh_token")
                    
                    if (accessToken != null && refreshToken != null) {
                        _authDeepLinks.emit(
                            AuthDeepLink.PasswordReset(
                                accessToken = accessToken,
                                refreshToken = refreshToken
                            )
                        )
                    }
                }
                
                // OAuth callback
                url.contains("/auth/oauth") -> {
                    val provider = uri.getQueryParameter("provider")
                    val accessToken = uri.getQueryParameter("access_token")
                    val refreshToken = uri.getQueryParameter("refresh_token")
                    
                    if (provider != null && accessToken != null && refreshToken != null) {
                        _authDeepLinks.emit(
                            AuthDeepLink.OAuthCallback(
                                provider = provider,
                                accessToken = accessToken,
                                refreshToken = refreshToken
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("Error handling deep link: ${e.message}")
            _authDeepLinks.emit(AuthDeepLink.Error("Invalid deep link format"))
        }
    }
    
    /**
     * Simple URL parser (replace with actual URL parsing in production)
     */
    private fun parseUrl(url: String): MockUri {
        return MockUri(url)
    }
}

/**
 * Authentication deep link types
 */
sealed class AuthDeepLink {
    data class MagicLink(
        val accessToken: String,
        val refreshToken: String,
        val type: String
    ) : AuthDeepLink()
    
    data class EmailConfirmation(
        val token: String,
        val type: String
    ) : AuthDeepLink()
    
    data class PasswordReset(
        val accessToken: String,
        val refreshToken: String
    ) : AuthDeepLink()
    
    data class OAuthCallback(
        val provider: String,
        val accessToken: String,
        val refreshToken: String
    ) : AuthDeepLink()
    
    data class Error(val message: String) : AuthDeepLink()
}

/**
 * Simple URI parser for deep links
 * In production, use platform-specific URI parsing
 */
private class MockUri(private val url: String) {
    fun getQueryParameter(key: String): String? {
        val regex = Regex("$key=([^&]*)")
        return regex.find(url)?.groupValues?.get(1)
    }
}