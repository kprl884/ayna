package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.outlined.Apple
import androidx.compose.material.icons.outlined.Google
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techtactoe.ayna.designsystem.button.PrimaryButton
import com.techtactoe.ayna.designsystem.button.TextButton
import com.techtactoe.ayna.designsystem.textfield.EmailTextField
import com.techtactoe.ayna.designsystem.textfield.NameTextField
import com.techtactoe.ayna.designsystem.textfield.PasswordTextField
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.model.PasswordStrength
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Modern SignUp Screen with comprehensive form validation
 * Enterprise-grade authentication UI with real-time validation feedback
 */
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = SignUpViewModel(),
    onNavigateToLogin: () -> Unit = {},
    onNavigateToEmailVerification: (String) -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    
    val lastNameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    // Handle UI effects
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SignUpContract.UiEffect.NavigateToLogin -> onNavigateToLogin()
                is SignUpContract.UiEffect.NavigateToEmailVerification -> onNavigateToEmailVerification(uiState.email)
                is SignUpContract.UiEffect.NavigateToTerms -> onNavigateToTerms()
                is SignUpContract.UiEffect.NavigateToPrivacyPolicy -> onNavigateToPrivacyPolicy()
                is SignUpContract.UiEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is SignUpContract.UiEffect.ShowError -> {
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
                // Header
                SignUpHeader()
                
                Spacer(modifier = Modifier.height(Spacing.large))
                
                // SignUp Form Card
                SignUpFormCard(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    lastNameFocusRequester = lastNameFocusRequester,
                    emailFocusRequester = emailFocusRequester,
                    passwordFocusRequester = passwordFocusRequester,
                    confirmPasswordFocusRequester = confirmPasswordFocusRequester,
                    focusManager = focusManager
                )
                
                Spacer(modifier = Modifier.height(Spacing.large))
                
                // Social SignUp Section
                SocialSignUpSection(
                    onGoogleClick = { viewModel.onEvent(SignUpContract.UiEvent.OnGoogleSignUpClicked) },
                    onAppleClick = { viewModel.onEvent(SignUpContract.UiEvent.OnAppleSignUpClicked) },
                    isLoading = uiState.isLoading
                )
                
                Spacer(modifier = Modifier.height(Spacing.large))
                
                // Footer
                SignUpFooter(
                    onLoginClick = { viewModel.onEvent(SignUpContract.UiEvent.OnLoginClicked) }
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
private fun SignUpHeader() {
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
            text = "Create Account",
            style = AynaTypography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(Spacing.small))
        
        Text(
            text = "Join Ayna to book your beauty appointments",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SignUpFormCard(
    uiState: SignUpContract.UiState,
    onEvent: (SignUpContract.UiEvent) -> Unit,
    lastNameFocusRequester: FocusRequester,
    emailFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    confirmPasswordFocusRequester: FocusRequester,
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
            // Name Fields Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.small)
            ) {
                NameTextField(
                    value = uiState.firstName,
                    onValueChange = { onEvent(SignUpContract.UiEvent.OnFirstNameChanged(it)) },
                    label = "First Name",
                    placeholder = "Enter first name",
                    isError = uiState.firstNameError != null,
                    errorMessage = uiState.firstNameError,
                    enabled = !uiState.isLoading,
                    modifier = Modifier.weight(1f)
                )
                
                NameTextField(
                    value = uiState.lastName,
                    onValueChange = { onEvent(SignUpContract.UiEvent.OnLastNameChanged(it)) },
                    label = "Last Name",
                    placeholder = "Enter last name",
                    isError = uiState.lastNameError != null,
                    errorMessage = uiState.lastNameError,
                    enabled = !uiState.isLoading,
                    focusRequester = lastNameFocusRequester,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Email Field
            EmailTextField(
                value = uiState.email,
                onValueChange = { onEvent(SignUpContract.UiEvent.OnEmailChanged(it)) },
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                enabled = !uiState.isLoading,
                focusRequester = emailFocusRequester,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Password Field with Strength Indicator
            Column {
                PasswordTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(SignUpContract.UiEvent.OnPasswordChanged(it)) },
                    label = "Password",
                    placeholder = "Create a strong password",
                    isError = uiState.passwordError != null,
                    errorMessage = uiState.passwordError,
                    enabled = !uiState.isLoading,
                    focusRequester = passwordFocusRequester,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Password Strength Indicator
                AnimatedVisibility(
                    visible = uiState.password.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    PasswordStrengthIndicator(
                        strength = uiState.passwordStrength,
                        modifier = Modifier.padding(top = Spacing.small)
                    )
                }
            }
            
            // Confirm Password Field
            PasswordTextField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(SignUpContract.UiEvent.OnConfirmPasswordChanged(it)) },
                label = "Confirm Password",
                placeholder = "Confirm your password",
                isError = uiState.confirmPasswordError != null,
                errorMessage = uiState.confirmPasswordError,
                enabled = !uiState.isLoading,
                focusRequester = confirmPasswordFocusRequester,
                modifier = Modifier.fillMaxWidth()
            )
            
            // Terms & Conditions Checkbox
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.acceptTerms,
                    onCheckedChange = { onEvent(SignUpContract.UiEvent.OnAcceptTermsChanged(it)) },
                    enabled = !uiState.isLoading,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
                
                Column(
                    modifier = Modifier.padding(start = Spacing.small, top = 12.dp)
                ) {
                    Row {
                        Text(
                            text = "I agree to the ",
                            style = AynaTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        androidx.compose.material3.TextButton(
                            onClick = { onEvent(SignUpContract.UiEvent.OnTermsClicked) },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(
                                text = "Terms of Service",
                                style = AynaTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Row {
                        Text(
                            text = "and ",
                            style = AynaTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        androidx.compose.material3.TextButton(
                            onClick = { onEvent(SignUpContract.UiEvent.OnPrivacyPolicyClicked) },
                            enabled = !uiState.isLoading,
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Text(
                                text = "Privacy Policy",
                                style = AynaTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    if (uiState.termsError != null) {
                        Text(
                            text = uiState.termsError,
                            style = AynaTypography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = Spacing.extraSmall)
                        )
                    }
                }
            }
            
            // Newsletter Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.subscribeToNewsletter,
                    onCheckedChange = { onEvent(SignUpContract.UiEvent.OnSubscribeNewsletterChanged(it)) },
                    enabled = !uiState.isLoading,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.width(Spacing.small))
                Text(
                    text = "Subscribe to newsletter for beauty tips and offers",
                    style = AynaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.small))
            
            // SignUp Button
            PrimaryButton(
                text = "Create Account",
                onClick = { 
                    focusManager.clearFocus()
                    onEvent(SignUpContract.UiEvent.OnSignUpClicked)
                },
                enabled = uiState.isSignUpButtonEnabled && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PasswordStrengthIndicator(
    strength: PasswordStrength?,
    modifier: Modifier = Modifier
) {
    strength?.let {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password strength:",
                    style = AynaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = it.label,
                    style = AynaTypography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = it.color
                )
            }
            
            Spacer(modifier = Modifier.height(Spacing.extraSmall))
            
            LinearProgressIndicator(
                progress = { 
                    when (it) {
                        PasswordStrength.WEAK -> 0.25f
                        PasswordStrength.MEDIUM -> 0.5f
                        PasswordStrength.STRONG -> 0.75f
                        PasswordStrength.VERY_STRONG -> 1.0f
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(AynaShapes.extraSmall),
                color = it.color,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun SocialSignUpSection(
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
                text = "Or sign up with",
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
            SocialSignUpButton(
                icon = Icons.Outlined.Google,
                text = "Google",
                onClick = onGoogleClick,
                enabled = !isLoading,
                modifier = Modifier.weight(1f)
            )
            
            SocialSignUpButton(
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
private fun SocialSignUpButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
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
private fun SignUpFooter(
    onLoginClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Already have an account? ",
            style = AynaTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(
            text = "Sign In",
            onClick = onLoginClick,
            contentColor = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// SignUpViewModel is now imported from the separate file

@Preview
@Composable
private fun SignUpScreenPreview() {
    MaterialTheme {
        SignUpScreen()
    }
}

@Preview
@Composable
private fun PasswordStrengthIndicatorPreview() {
    MaterialTheme {
        Column {
            PasswordStrengthIndicator(strength = PasswordStrength.WEAK)
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthIndicator(strength = PasswordStrength.MEDIUM)
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthIndicator(strength = PasswordStrength.STRONG)
            Spacer(modifier = Modifier.height(8.dp))
            PasswordStrengthIndicator(strength = PasswordStrength.VERY_STRONG)
        }
    }
}
