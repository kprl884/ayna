package com.techtactoe.ayna

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.techtactoe.ayna.data.auth.SocialAuthManager
import com.techtactoe.ayna.data.auth.SupabaseAuthManager
import com.techtactoe.ayna.util.GoogleSignInLauncher

class MainActivity : ComponentActivity() {
    
    private lateinit var socialAuthManager: SocialAuthManager
    private lateinit var authManager: SupabaseAuthManager
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Forward the result to SocialAuthManager
        socialAuthManager.handleGoogleSignInResult(result.data)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize authentication managers
        socialAuthManager = SocialAuthManager()
        socialAuthManager.initialize(this)
        authManager = SupabaseAuthManager()
        
        // Provide launcher to common code
        GoogleSignInLauncher.launch = {
            try {
                val signInIntent = socialAuthManager.getGoogleSignInIntent()
                googleSignInLauncher.launch(signInIntent)
            } catch (e: Exception) {
                println("Error launching Google Sign-In: ${e.message}")
            }
        }
        
        enableEdgeToEdge()
        setContent {
            // Remove when https://issuetracker.google.com/issues/364713509 is fixed
            LaunchedEffect(isSystemInDarkTheme()) {
                enableEdgeToEdge()
            }
            
            // Monitor authentication state
            val isAuthenticated by authManager.isAuthenticated.collectAsState(initial = false)
            LaunchedEffect(isAuthenticated) {
                if (isAuthenticated) {
                    println("User authenticated successfully")
                }
            }
            
            App()
        }
    }
    
    /**
     * Launch Google Sign-In (called from common code)
     */
    fun launchGoogleSignIn() {
        try {
            val signInIntent = socialAuthManager.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        } catch (e: Exception) {
            println("Error launching Google Sign-In: ${e.message}")
        }
    }
    
    override fun onStart() {
        super.onStart()
        // Check if user is already signed in to Google
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            println("User already signed in to Google: ${account.email}")
        }
    }
}
