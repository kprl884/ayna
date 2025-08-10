package com.techtactoe.ayna.presentation.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.navigation.NavHostController
import com.techtactoe.ayna.common.designsystem.component.button.PrimaryButton
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.designsystem.typography.AynaTypography
import com.techtactoe.ayna.domain.repository.EmailValidator
import com.techtactoe.ayna.domain.repository.PasswordValidation
import com.techtactoe.ayna.presentation.navigation.Screen
import com.techtactoe.ayna.presentation.ui.screens.auth.component.termsAnnotated

@Composable
fun CreateAccountScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val uiState by authViewModel.uiState.collectAsState()
    
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+90") }
    var agreePolicy by remember { mutableStateOf(false) }
    var agreeMarketing by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }

    // Get email from ViewModel state
    val email = uiState.email
    
    val nameValid = firstName.isNotBlank() && lastName.isNotBlank()
    val passwordValidation = PasswordValidation.validate(password)
    val passwordValid = passwordValidation.isValid
    val phoneValid = phone.filter { it.isDigit() }.length >= 7
    val emailValid = EmailValidator.isValid(email)
    val formValid = nameValid && passwordValid && phoneValid && agreePolicy

    // Navigate when authenticated
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState(initial = false)
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            navController.navigate(Screen.Profile) {
                popUpTo<Screen.AuthRegister> { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Update ViewModel with form data
    LaunchedEffect(firstName, lastName, password) {
        authViewModel.onFirstNameChange(firstName)
        authViewModel.onLastNameChange(lastName)
        authViewModel.onPasswordChange(password)
    }

    val outlinedColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
        unfocusedBorderColor = MaterialTheme.colorScheme.tertiary
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.large),
        verticalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Text(
            text = "Create account",
            style = AynaTypography.headlineLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Create your new account by completing these details.",
            style = AynaTypography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        // Email field (read-only, from previous screen)
        OutlinedTextField(
            value = email,
            onValueChange = { },
            label = { Text("Email address") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            singleLine = true,
            colors = outlinedColors
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First name") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrors && firstName.isBlank(),
            singleLine = true,
            colors = outlinedColors,
            enabled = !uiState.isLoading
        )
        AnimatedVisibility(
            visible = showErrors && firstName.isBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "This field is required",
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last name") },
            modifier = Modifier.fillMaxWidth(),
            isError = showErrors && lastName.isBlank(),
            singleLine = true,
            colors = outlinedColors,
            enabled = !uiState.isLoading
        )
        AnimatedVisibility(
            visible = showErrors && lastName.isBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "This field is required",
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (uiState.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (uiState.showPassword) "Hide" else "Show",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { authViewModel.togglePasswordVisibility() }
                )
            },
            isError = showErrors && !passwordValid,
            singleLine = true,
            colors = outlinedColors,
            enabled = !uiState.isLoading
        )
        AnimatedVisibility(
            visible = showErrors && !passwordValid && password.isNotBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                passwordValidation.errors.forEach { error ->
                    Text(
                        text = "• $error",
                        style = AynaTypography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        
        AnimatedVisibility(
            visible = showErrors && password.isBlank(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Password is required",
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = countryCode,
                onValueChange = { countryCode = it.take(4) },
                label = { Text("Code") },
                modifier = Modifier.weight(0.35f),
                singleLine = true,
                colors = outlinedColors,
                enabled = !uiState.isLoading
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Mobile number") },
                modifier = Modifier.weight(0.65f),
                isError = showErrors && phone.isNotEmpty() && !phoneValid,
                singleLine = true,
                colors = outlinedColors,
                enabled = !uiState.isLoading
            )
        }
        
        AnimatedVisibility(
            visible = showErrors && phone.isNotEmpty() && !phoneValid,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Please enter a valid phone number",
                style = AynaTypography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = agreePolicy, 
                onCheckedChange = { agreePolicy = it },
                enabled = !uiState.isLoading
            )
            Text(text = termsAnnotated(), style = AynaTypography.bodySmall)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = agreeMarketing, 
                onCheckedChange = { agreeMarketing = it },
                enabled = !uiState.isLoading
            )
            Text(
                text = "I agree to receive marketing notifications with offers and news",
                style = AynaTypography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(Spacing.small))

        PrimaryButton(
            text = if (uiState.isLoading) "Creating Account..." else "Create Account",
            onClick = {
                showErrors = true
                if (formValid) {
                    authViewModel.signUp {
                        // Success callback - navigation handled by LaunchedEffect above
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        )
        
        // Show validation errors
        AnimatedVisibility(
            visible = uiState.errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    style = AynaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = Spacing.small)
                )
            }
        }
    }
}