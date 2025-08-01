package com.techtactoe.ayna.presentation.ui.screens.selecttime

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.presentation.theme.AynaAppTheme
import java.text.SimpleDateFormat
import java.util.*

/**
 * Screen for selecting appointment time
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimeScreen(
    viewModel: SelectTimeViewModel,
    salonId: String,
    serviceId: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onTimeSelected: (TimeSlot) -> Unit,
    onJoinWaitlistClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(salonId, serviceId) {
        viewModel.initialize(salonId, serviceId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select time") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onCloseClick) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Month/Year header
            Text(
                text = "Aug 2025",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Date selector
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                items(viewModel.getDateOptions()) { dateOption ->
                    DateChip(
                        dateOption = dateOption,
                        onClick = { 
                            if (!dateOption.isDisabled) {
                                viewModel.loadTimeSlots(dateOption.date)
                            }
                        }
                    )
                }
            }
            
            // Content based on state
            when {
                uiState.isLoading -> {
                    LoadingContent()
                }
                uiState.error != null -> {
                    ErrorContent(
                        message = uiState.error,
                        onRetry = { viewModel.loadTimeSlots(uiState.selectedDate) }
                    )
                }
                uiState.isFullyBooked -> {
                    FullyBookedContent(
                        nextAvailableDate = uiState.nextAvailableDate,
                        onGoToNextDate = { viewModel.goToNextAvailableDate() },
                        onJoinWaitlist = onJoinWaitlistClick
                    )
                }
                else -> {
                    TimeSlotsList(
                        timeSlots = uiState.availableSlots,
                        selectedTimeSlot = uiState.selectedTimeSlot,
                        onTimeSlotClick = { timeSlot ->
                            viewModel.selectTimeSlot(timeSlot)
                            onTimeSelected(timeSlot)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DateChip(
    dateOption: DateOption,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        dateOption.isSelected -> Color(0xFF7B61FF)
        dateOption.isDisabled -> Color.Transparent
        else -> Color.Transparent
    }
    
    val textColor = when {
        dateOption.isSelected -> Color.White
        dateOption.isDisabled -> Color.Gray
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    val borderColor = when {
        dateOption.isSelected -> Color.Transparent
        dateOption.isDisabled -> Color.Transparent
        else -> MaterialTheme.colorScheme.outline
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable(enabled = !dateOption.isDisabled) { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(1.dp, borderColor, CircleShape)
        ) {
            Text(
                text = dateOption.dayOfMonth.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = textColor
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = dateOption.dayOfWeek,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color(0xFF7B61FF))
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Button(onClick = onRetry) {
            Text("Try again")
        }
    }
}

@Composable
private fun FullyBookedContent(
    nextAvailableDate: String,
    onGoToNextDate: () -> Unit,
    onJoinWaitlist: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            tint = Color(0xFF7B61FF),
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 24.dp)
        )
        
        Text(
            text = "Selected professional is fully booked on this date",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Available from $nextAvailableDate",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        OutlinedButton(
            onClick = onGoToNextDate,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Go to next available date")
        }
        
        OutlinedButton(
            onClick = onJoinWaitlist,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Join waitlist")
        }
    }
}

@Composable
private fun TimeSlotsList(
    timeSlots: List<TimeSlot>,
    selectedTimeSlot: TimeSlot?,
    onTimeSlotClick: (TimeSlot) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(timeSlots) { timeSlot ->
            TimeSlotItem(
                timeSlot = timeSlot,
                isSelected = selectedTimeSlot == timeSlot,
                onClick = { onTimeSlotClick(timeSlot) }
            )
        }
    }
}

@Composable
private fun TimeSlotItem(
    timeSlot: TimeSlot,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        Color(0xFF7B61FF).copy(alpha = 0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    val borderColor = if (isSelected) {
        Color(0xFF7B61FF)
    } else {
        MaterialTheme.colorScheme.outline
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = timeSlot.formattedTime,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Preview
@Composable
private fun SelectTimeScreenPreview() {
    AynaAppTheme {
        // Preview implementation would go here
    }
}
