package com.techtactoe.ayna.presentation.ui.screens.auth.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

@Composable
fun termsAnnotated(): AnnotatedString = buildAnnotatedString {
    append("I agree to the ")
    pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
    append("Privacy Policy")
    pop()
    append(", ")
    pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
    append("Terms of Use")
    pop()
    append(" and ")
    pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
    append("Terms of Service")
    pop()
}