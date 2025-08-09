package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Apple
import androidx.compose.material.icons.outlined.Google
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.button.PrimaryButton
import com.techtactoe.ayna.designsystem.button.SecondaryButton
import com.techtactoe.ayna.designsystem.button.TextButton
import com.techtactoe.ayna.designsystem.textfield.EmailTextField
import com.techtactoe.ayna.designsystem.textfield.PasswordTextField
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Modern Login Screen with Material 3 Design
 * Enterprise-grade authentication UI with comprehensive form validation
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = LoginViewModel(),
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    // Handle UI effects
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is LoginContract.UiEffect.NavigateToHome -> onNavigateToHome()
                is LoginContract.UiEffect.NavigateToSignUp -> onNavigateToSignUp()
                is LoginContract.UiEffect.NavigateToForgotPassword -> onNavigateToForgotPassword()
                is LoginContract.UiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is LoginContract.UiEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.error.message ?: "An error occurred")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(paddingValues)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo Section
                LoginHeader()
                
                Spacer(modifier = Modifier.height(Spacing.xlarge))
                
                // Login Form Card
                LoginFormCard(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    passwordFocusRequester = passwordFocusRequester,
                    focusManager = focusManager
                )
                
                Spacer(modifier = Modifier.height(Spacing.large))
                
                // Social Login Section
                SocialLoginSection(
                    onGoogleClick = { viewModel.onEvent(LoginContract.UiEvent.OnGoogleSignInClicked) },
                    onAppleClick = { viewModel.onEvent(LoginContract.UiEvent.OnAppleSignInClicked) },
                    isLoading = uiState.isLoading
                )
                
                Spacer(modifier = Modifier.height(Spacing.large))
                
                // Footer
                LoginFooter(
                    onSignUpClick = { viewModel.onEvent(LoginContract.UiEvent.OnSignUpClicked) }
                )
            }
            
            // Loading overlay
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo/Icon
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(AynaShapes.large)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "💇‍♀️",
                style = AynaTypography.headlineLarge
            )
        }
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        Text(
            text = "Welcome to Ayna",
            style = AynaTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Text(
            text = "Sign in to your account to continue",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun LoginFormCard(
    uiState: LoginContract.UiState,
    onEvent: (LoginContract.UiEvent) -> Unit,
    passwordFocusRequester: FocusRequester,
    focusManager: androidx.compose.ui.focus.FocusManager
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = AynaShapes.large
    ) {
        Column(
            modifier = Modifier.padding(Spacing.large),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            // Email Field
            EmailTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginContract.UiEvent.OnEmailChanged(it)) },
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Password Field
            PasswordTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginContract.UiEvent.OnPasswordChanged(it)) },
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                enabled = !uiState.isLoading,
                focusRequester = passwordFocusRequester,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Remember Me & Forgot Password Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = uiState.rememberMe,
                        onCheckedChange = { onEvent(LoginContract.UiEvent.OnRememberMeChanged(it)) },
                        enabled = !uiState.isLoading,
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.width(Spacing.small))
                    Text(
                        text = "Remember me",
                        style = AynaTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                TextButton(
                    text = "Forgot Password?",
                    onClick = { onEvent(LoginContract.UiEvent.OnForgotPasswordClicked) },
                    enabled = !uiState.isLoading
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // Login Button
            PrimaryButton(
                text = "Sign In",
                onClick = { 
                    focusManager.clearFocus()
                    onEvent(LoginContract.UiEvent.OnLoginClicked)
                },
                enabled = uiState.isLoginButtonEnabled && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SocialLoginSection(
    onGoogleClick: () -> Unit,
    onAppleClick: () -> Unit,
    isLoading: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
            Text(
                text = "Or continue with",
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = Spacing.medium)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }
        
        Spacer(modifier = Modifier.height(Spacing.medium))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            SocialLoginButton(
                icon = Icons.Outlined.Google,
                text = "Google",
                onClick = onGoogleClick,
                enabled = !isLoading,
                modifier = Modifier.weight(1f)
            )
            
            SocialLoginButton(
                icon = Icons.Outlined.Apple,
                text = "Apple",
                onClick = onAppleClick,
                enabled = !isLoading,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SocialLoginButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = AynaShapes.medium,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(Spacing.small))
            Text(
                text = text,
                style = AynaTypography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun LoginFooter(
    onSignUpClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Don't have an account? ",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(
            text = "Sign Up",
            onClick = onSignUpClick,
            contentColor = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// LoginViewModel is now imported from the separate file

@Preview
@Composable
private fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}

@Preview
@Composable
private fun LoginHeaderPreview() {
    MaterialTheme {
        LoginHeader()
    }
}
