package com.techtactoe.ayna.presentation.ui.screens.appointments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.presentation.theme.Spacing
import com.techtactoe.ayna.presentation.ui.screens.appointments.component.AppointmentCard
import com.techtactoe.ayna.presentation.ui.screens.appointments.model.AppointmentTab

@Composable
fun AppointmentsContent(
    selectedTab: AppointmentTab,
    upcomingAppointments: List<Appointment>,
    pastAppointments: List<Appointment>,
    onAppointmentClick: (Appointment) -> Unit
) {
    val appointments = when (selectedTab) {
        AppointmentTab.UPCOMING -> upcomingAppointments
        AppointmentTab.PAST -> pastAppointments
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.md),
        verticalArrangement = Arrangement.spacedBy(Spacing.md)
    ) {
        items(appointments) { appointment ->
            AppointmentCard(
                appointment = appointment,
                onClick = { onAppointmentClick(appointment) }
            )
        }
    }
}