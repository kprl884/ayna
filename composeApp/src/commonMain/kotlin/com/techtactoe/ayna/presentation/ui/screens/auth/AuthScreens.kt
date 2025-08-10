package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.techtactoe.ayna.common.designsystem.component.button.PrimaryButton
import com.techtactoe.ayna.common.designsystem.component.button.SocialButton
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.repository.EmailValidator
import com.techtactoe.ayna.presentation.navigation.Screen

@Composable
fun AuthLoginScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val uiState by authViewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    val isEmailValid = remember(email) { EmailValidator.isValid(email) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate away when authenticated
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(Screen.Profile) {
                popUpTo<Screen.AuthLogin> { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Handle authentication errors
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            authViewModel.clearError()
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val topPad = maxHeight / 20

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = Spacing.large, end = Spacing.large, bottom = Spacing.large)
                .padding(top = topPad),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = "Log in or sign up",
                    style = AynaTypography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = "Create an account or log in to book and manage your appointments",
                style = AynaTypography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )

            // Social authentication buttons
            SocialButton(
                text = "Continue with Facebook",
                onClick = { 
                    // TODO: Implement Facebook auth when needed
                    snackbarHostState.showSnackbar("Facebook login coming soon")
                }
            )

            // Show Google only on Android
            if (com.techtactoe.ayna.util.Platform.isAndroid) {
                SocialButton(
                    text = "Continue with Google",
                    onClick = {
                        if (uiState.isLoading) return@SocialButton
                        
                        viewModelScope.launch {
                            try {
                                // Start ViewModel flow first (sets up pending continuation)
                                authViewModel.signInWithGoogle {
                                    // Success callback
                                    navController.navigate(Screen.Profile) {
                                        popUpTo<Screen.AuthLogin> { inclusive = true }
                                    }
                                }
                                // Then launch Android Google Sign-In UI
                                com.techtactoe.ayna.util.launchGoogleSignIn()
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Google Sign-In failed: ${e.message}")
                            }
                        }
                    }
                )
            }

            // Show Apple only on iOS
            if (com.techtactoe.ayna.util.Platform.isIos) {
                SocialButton(
                    text = "Continue with Apple",
                    onClick = { 
                        if (uiState.isLoading) return@SocialButton
                        
                        authViewModel.signInWithApple {
                            navController.navigate(Screen.Profile) {
                                popUpTo<Screen.AuthLogin> { inclusive = true }
                            }
                        }
                    }
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("OR", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            // Email input with validation
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email address") },
                isError = email.isNotEmpty() && !isEmailValid,
                singleLine = true,
                enabled = !uiState.isLoading
            )

            AnimatedVisibility(
                visible = email.isNotEmpty() && !isEmailValid,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Please enter a valid email address",
                    style = AynaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // Continue button with loading state
            PrimaryButton(
                text = if (uiState.isLoading) "Loading..." else "Continue",
                onClick = { 
                    if (isEmailValid && !uiState.isLoading) {
                        authViewModel.onEmailChange(email)
                        navController.navigate(Screen.AuthRegister)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEmailValid && !uiState.isLoading
            )

            // Magic link option
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Or send me a magic link",
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        if (isEmailValid && !uiState.isLoading) {
                            authViewModel.onEmailChange(email)
                            authViewModel.signInWithMagicLink {
                                snackbarHostState.showSnackbar("Magic link sent! Check your email.")
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(Spacing.small))

            Text(
                text = "Have a business account?",
                style = AynaTypography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Sign in as a professional",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { 
                    // TODO: Navigate to business login
                    snackbarHostState.showSnackbar("Business login coming soon")
                },
            )

            Spacer(modifier = Modifier.weight(1f))

            // Snackbar host for messages
            SnackbarHost(hostState = snackbarHostState)
        }

        // Show logout success message
        LaunchedEffect(Unit) {
            val shouldShow = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.remove<Boolean>("logout_success") ?: false
            if (shouldShow) {
                snackbarHostState.showSnackbar("Successfully logged out")
            }
        }
    }
}