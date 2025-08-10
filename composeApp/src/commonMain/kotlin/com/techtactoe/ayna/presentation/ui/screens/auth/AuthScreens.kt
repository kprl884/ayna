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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.techtactoe.ayna.common.designsystem.component.button.PrimaryButton
import com.techtactoe.ayna.common.designsystem.component.button.SocialButton
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.common.designsystem.utils.Validations.EMAIL_REGEX
import com.techtactoe.ayna.presentation.navigation.Screen

@Suppress("UNUSED_PARAMETER")
@Composable
fun AuthLoginScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    val isEmailValid = remember(email) { EMAIL_REGEX.matches(email) }

    val snackbarHostState = remember { SnackbarHostState() }

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

            SocialButton(text = "Continue with Facebook")
            SocialButton(text = "Continue with Google")

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("OR", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email address") },
                isError = email.isNotEmpty() && !isEmailValid,
                singleLine = true
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

            PrimaryButton(
                text = "Continue",
                onClick = { if (isEmailValid) navController.navigate(Screen.AuthRegister) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isEmailValid
            )

            Spacer(modifier = Modifier.height(Spacing.small))

            Text(
                text = "Have a business account?",
                style = AynaTypography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Sign in as a professional",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { },
            )

            Spacer(modifier = Modifier.weight(1f))

            // Snackbar host to show logout success as a transient message
            SnackbarHost(hostState = snackbarHostState)
        }

        // Show toast-like snackbar only when logout operation succeeded and triggered navigation here
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