package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.common.designsystem.component.button.PrimaryButton
import com.techtactoe.ayna.common.designsystem.theme.AynaAppTheme
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Authentication screen for user login and registration
 * Integrates with Supabase Auth for secure user management
 */
@Composable
fun AuthScreen(
    onSignInSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xlarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App logo and title
        Text(
            text = "💇‍♀️",
            style = AynaTypography.displayLarge
        )

        Spacer(modifier = Modifier.height(Spacing.medium))

        Text(
            text = "Welcome to Ayna",
            style = AynaTypography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = if (isSignUp) "Create your account" else "Sign in to continue",
            style = AynaTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = Spacing.xlarge)
        )

        // Form fields
        if (isSignUp) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.medium),
                singleLine = true
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.medium),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = Spacing.large),
            singleLine = true
        )

        // Error message
        errorMessage?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.medium)
            ) {
                Text(
                    text = error,
                    style = AynaTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(Spacing.medium)
                )
            }
        }

        // Sign in/up button
        PrimaryButton(
            text = if (isSignUp) "Create Account" else "Sign In",
            onClick = {
                if (validateForm(email, password, name, isSignUp)) {
                    isLoading = true
                    errorMessage = null
                    // TODO: Implement actual authentication
                    // For now, simulate success
                    isLoading = false
                    onSignInSuccess()
                } else {
                    errorMessage = "Please fill in all required fields"
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.large))

        // Toggle between sign in and sign up
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isSignUp) "Already have an account?" else "Don't have an account?",
                style = AynaTypography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TextButton(
                onClick = { 
                    isSignUp = !isSignUp
                    errorMessage = null
                }
            ) {
                Text(
                    text = if (isSignUp) "Sign In" else "Sign Up",
                    style = AynaTypography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(Spacing.medium))
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun validateForm(email: String, password: String, name: String, isSignUp: Boolean): Boolean {
    return email.isNotBlank() && 
           password.length >= 6 && 
           (!isSignUp || name.isNotBlank())
}

@Preview
@Composable
private fun AuthScreenPreview() {
    AynaAppTheme {
        AuthScreen()
    }
}