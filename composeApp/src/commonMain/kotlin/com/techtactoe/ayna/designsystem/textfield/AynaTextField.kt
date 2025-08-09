package com.techtactoe.ayna.designsystem.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.techtactoe.ayna.designsystem.theme.AynaShapes
import com.techtactoe.ayna.designsystem.theme.Spacing
import com.techtactoe.ayna.designsystem.typography.AynaTypography
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Ayna Design System TextField Component
 * Modern Material 3 text field with validation, error states, and accessibility
 */
@Composable
fun AynaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    focusRequester: FocusRequester? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    var passwordVisible by remember { mutableStateOf(!isPassword) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    style = AynaTypography.bodyMedium
                )
            },
            placeholder = placeholder?.let { 
                {
                    Text(
                        text = it,
                        style = AynaTypography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            leadingIcon = leadingIcon?.let {
                {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = if (isError) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            },
            trailingIcon = when {
                isError && errorMessage != null -> {
                    {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                isPassword -> {
                    {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                else -> null
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = keyboardCapitalization
            ),
            keyboardActions = keyboardActions,
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = AynaShapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                errorLabelColor = MaterialTheme.colorScheme.error,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorCursorColor = MaterialTheme.colorScheme.error,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                errorTextColor = MaterialTheme.colorScheme.onSurface
            ),
            textStyle = AynaTypography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (focusRequester != null) {
                        Modifier.focusRequester(focusRequester)
                    } else {
                        Modifier
                    }
                )
        )

        // Error message
        AnimatedVisibility(
            visible = isError && !errorMessage.isNullOrBlank(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(Spacing.extraSmall))
                Text(
                    text = message,
                    style = AynaTypography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = Spacing.medium)
                )
            }
        }
    }
}

/**
 * Email TextField with built-in validation styling
 */
@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null
) {
    AynaTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Email",
        placeholder = "Enter your email",
        keyboardType = KeyboardType.Email,
        keyboardCapitalization = KeyboardCapitalization.None,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        focusRequester = focusRequester,
        modifier = modifier
    )
}

/**
 * Password TextField with built-in security features
 */
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Password",
    modifier: Modifier = Modifier,
    placeholder: String = "Enter your password",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null
) {
    AynaTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isPassword = true,
        keyboardType = KeyboardType.Password,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        focusRequester = focusRequester,
        modifier = modifier
    )
}

/**
 * Name TextField for first/last names
 */
@Composable
fun NameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    focusRequester: FocusRequester? = null
) {
    AynaTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        keyboardType = KeyboardType.Text,
        keyboardCapitalization = KeyboardCapitalization.Words,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        focusRequester = focusRequester,
        modifier = modifier
    )
}

@Preview
@Composable
private fun AynaTextFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(Spacing.medium)
        ) {
            EmailTextField(
                value = "",
                onValueChange = {}
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            PasswordTextField(
                value = "",
                onValueChange = {}
            )
            
            Spacer(modifier = Modifier.height(Spacing.medium))
            
            AynaTextField(
                value = "",
                onValueChange = {},
                label = "Error Example",
                isError = true,
                errorMessage = "This field is required"
            )
        }
    }
}
