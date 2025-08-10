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
 * Android-specific social authentication manager for Google Sign-In
 */
actual class SocialAuthManager {
    
    private lateinit var context: Context
    private lateinit var googleSignInClient: GoogleSignInClient
    
    companion object {
        // One-shot continuation to resume when Activity result arrives (shared across instances)
        private var pendingContinuation: kotlin.coroutines.Continuation<String>? = null
    }
    
    fun initialize(context: Context) {
        this.context = context
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID") // TODO: Replace with your Web Client ID from Google Console
            .requestEmail()
            .build()
        
        this.googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    
    /**
     * Get Google Sign-In intent
     */
    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }
    
    /**
     * Request Google Sign-In: waits until Activity result provides an ID token
     */
    actual suspend fun requestSocialSignIn(): String {
        return suspendCancellableCoroutine { continuation ->
            // store continuation and wait for onActivityResult to resume
            pendingContinuation = continuation
        }
    }
    
    /**
     * Handle Google Sign-In result and resume waiting continuation
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
     * Sign out from Google
     */
    actual fun signOutFromSocial() {
        if (this::googleSignInClient.isInitialized) {
            googleSignInClient.signOut()
        }
    }
}
