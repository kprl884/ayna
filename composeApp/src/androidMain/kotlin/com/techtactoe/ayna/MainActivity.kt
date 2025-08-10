package com.techtactoe.ayna

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.techtactoe.ayna.data.auth.SocialAuthManager
import com.techtactoe.ayna.util.GoogleSignInLauncher

class MainActivity : ComponentActivity() {
    
    private lateinit var socialAuthManager: SocialAuthManager
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Forward the result to SocialAuthManager; it will resume the pending continuation
        socialAuthManager.handleGoogleSignInResult(result.data)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize social auth manager
        socialAuthManager = SocialAuthManager()
        socialAuthManager.initialize(this)
        // Provide launcher to common code
        GoogleSignInLauncher.launch = {
            val signInIntent = socialAuthManager.getGoogleSignInIntent()
            googleSignInLauncher.launch(signInIntent)
        }
        
        enableEdgeToEdge()
        setContent {
            // Remove when https://issuetracker.google.com/issues/364713509 is fixed
            LaunchedEffect(isSystemInDarkTheme()) {
                enableEdgeToEdge()
            }
            App()
        }
    }
    
    fun launchGoogleSignIn() {
        val signInIntent = socialAuthManager.getGoogleSignInIntent()
        googleSignInLauncher.launch(signInIntent)
    }
}
