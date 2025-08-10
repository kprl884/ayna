package com.techtactoe.ayna.data.auth

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Enhanced Android-specific social authentication manager
 * Supports Google Sign-In with proper error handling and state management
 */
actual class SocialAuthManager {

    private lateinit var context: Context
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        // Shared continuation for Activity result handling
        private var pendingContinuation: kotlin.coroutines.Continuation<String>? = null

        // Web Client ID - Replace with your actual Google Console Web Client ID
        private const val WEB_CLIENT_ID = "YOUR_GOOGLE_WEB_CLIENT_ID"
    }

    fun initialize(context: Context) {
        this.context = context
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .build()

        this.googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    /**
     * Get Google Sign-In intent for launching
     */
    fun getGoogleSignInIntent(): Intent {
        if (!this::googleSignInClient.isInitialized) {
            throw IllegalStateException("SocialAuthManager not initialized. Call initialize() first.")
        }
        return googleSignInClient.signInIntent
    }

    /**
     * Request Google Sign-In and wait for result
     */
    actual suspend fun requestSocialSignIn(): String {
        if (!this::googleSignInClient.isInitialized) {
            throw IllegalStateException("SocialAuthManager not initialized")
        }

        return suspendCancellableCoroutine { continuation ->
            // Store continuation and wait for Activity result
            pendingContinuation = continuation

            continuation.invokeOnCancellation {
                pendingContinuation = null
            }
        }
    }

    /**
     * Handle Google Sign-In result from Activity
     */
    fun handleGoogleSignInResult(data: Intent?) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        task.addOnCompleteListener { completedTask ->
            try {
                val account = completedTask.getResult(ApiException::class.java)
                val idToken = account.idToken
                val cont = pendingContinuation
                pendingContinuation = null
                if (idToken != null) {
                    cont?.resume(idToken)
                } else {
                    cont?.resumeWithException(Exception("Google Sign-In failed: No ID token"))
                }
            } catch (e: ApiException) {
                val cont = pendingContinuation
                pendingContinuation = null
                cont?.resumeWithException(Exception("Google Sign-In failed: ${e.statusCode}"))
            }
        }
    }

    /**
     * Check if user is currently signed in to Google
     */
    fun isSignedIn(): Boolean {
        return if (this::googleSignInClient.isInitialized) {
            GoogleSignIn.getLastSignedInAccount(context) != null
        } else {
            false
        }
    }

    /**
     * Get current Google account if signed in
     */
    fun getCurrentAccount(): GoogleSignInAccount? {
        return if (this::googleSignInClient.isInitialized) {
            GoogleSignIn.getLastSignedInAccount(context)
        } else {
            null
        }
    }

    /**
     * Sign out from Google
     */
    actual fun signOutFromSocial() {
        if (this::googleSignInClient.isInitialized) {
            googleSignInClient.signOut().addOnCompleteListener {
                println("Google sign-out completed")
            }
        }
    }

    /**
     * Revoke Google access (stronger than sign out)
     */
    fun revokeAccess() {
        if (this::googleSignInClient.isInitialized) {
            googleSignInClient.revokeAccess().addOnCompleteListener {
                println("Google access revoked")
            }
        }
    }
}
