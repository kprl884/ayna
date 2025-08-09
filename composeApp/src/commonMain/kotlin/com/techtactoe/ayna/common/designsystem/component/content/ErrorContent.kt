package com.techtactoe.ayna.common.designsystem.component.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.techtactoe.ayna.common.designsystem.theme.Spacing
import com.techtactoe.ayna.common.model.ErrorInfo
import com.techtactoe.ayna.domain.model.ExploreError

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    onClearError: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.xlarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = Spacing.medium)
        )
        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Try again",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun ErrorContent(
    error: ExploreError,
    onRetry: () -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (_, message, primaryAction, secondaryAction) = when (error) {
        is ExploreError.NetworkError -> ErrorInfo(
            title = "Connection Problem",
            message = "Please check your internet connection and try again.",
            primaryAction = "Retry" to onRetry,
            secondaryAction = null
        )

        is ExploreError.LocationPermissionDenied -> ErrorInfo(
            title = "Location Permission Needed",
            message = "Please enable location permission to find venues near you.",
            primaryAction = "Clear Filters" to onClearFilters,
            secondaryAction = null
        )

        is ExploreError.NoDataError -> ErrorInfo(
            title = "No Data Available",
            message = "Unable to load venue data at the moment.",
            primaryAction = "Retry" to onRetry,
            secondaryAction = "Clear Filters" to onClearFilters
        )

        is ExploreError.UnknownError -> ErrorInfo(
            title = "Something went wrong",
            message = error.message,
            primaryAction = "Retry" to onRetry,
            secondaryAction = "Clear Filters" to onClearFilters
        )
    }

    ErrorContent(
        message = message,
        onRetry = primaryAction.second,
        onClearError = secondaryAction?.second ?: onClearFilters
    )
}