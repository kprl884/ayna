package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import kotlinx.coroutines.delay
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * Mock implementation of AppointmentRepository with realistic hardcoded data
 */
class MockAppointmentRepositoryImpl : AppointmentRepository {

    private val appointments = mutableListOf(
        Appointment(
            id = "apt1",
            salonId = "1",
            salonName = "Emre's Barbershop Dolapdere",
            serviceName = "Haircut / Saç Kesimi",
            employeeId = "emp1",
            employeeName = "Emre Demir",
            appointmentDateTime = 1754056654975, // 2 days from now
            status = AppointmentStatus.UPCOMING,
            price = 700.0,
            durationInMinutes = 60,
            notes = "Regular trim, not too short"
        ),
        Appointment(
            id = "apt2",
            salonId = "2",
            salonName = "Ayna Beauty Studio",
            serviceName = "Spa Pedikür (Spa Pedicure)",
            employeeId = "emp3",
            employeeName = "Ayla Kaya",
            appointmentDateTime = 1754056654975, // 5 days from now
            status = AppointmentStatus.UPCOMING,
            price = 950.0,
            durationInMinutes = 55,
            notes = ""
        ),
        Appointment(
            id = "apt3",
            salonId = "3",
            salonName = "Serenity Spa & Wellness",
            serviceName = "60 Min Thai with Oil Massage",
            employeeId = "emp5",
            employeeName = "Mehmet Güler",
            appointmentDateTime = 1754056654975, // 7 days ago
            status = AppointmentStatus.COMPLETED,
            price = 1600.0,
            durationInMinutes = 75,
            notes = "Excellent service"
        ),
        Appointment(
            id = "apt4",
            salonId = "4",
            salonName = "Elite Hair Studio",
            serviceName = "Women's Cut & Style",
            employeeId = "emp7",
            employeeName = "Canan Arslan",
            appointmentDateTime = 1754056654975, // 3 days ago
            status = AppointmentStatus.CANCELLED,
            price = 2000.0,
            durationInMinutes = 90,
            notes = "Cancelled due to emergency"
        )
    )

    override suspend fun getUserAppointments(userId: String): List<Appointment> {
        delay(1000) // Simulate network latency
        return appointments.toList()
    }

    override suspend fun getUpcomingAppointments(userId: String): List<Appointment> {
        delay(800) // Simulate network latency
        return appointments.filter { it.status == AppointmentStatus.UPCOMING }
            .sortedBy { it.appointmentDateTime }
    }

    override suspend fun getPastAppointments(userId: String): List<Appointment> {
        delay(800) // Simulate network latency
        return appointments.filter {
            it.status == AppointmentStatus.COMPLETED || it.status == AppointmentStatus.CANCELLED
        }.sortedByDescending { it.appointmentDateTime }
    }

    override suspend fun createAppointment(appointment: Appointment): Boolean {
        delay(1200) // Simulate network latency
        return try {
            appointments.add(appointment)
            true
        } catch (e: Exception) {
            print(e.message)
            false
        }
    }

    override suspend fun cancelAppointment(appointmentId: String): Boolean {
        delay(800) // Simulate network latency
        return try {
            val appointmentIndex = appointments.indexOfFirst { it.id == appointmentId }
            if (appointmentIndex != -1) {
                val appointment = appointments[appointmentIndex]
                appointments[appointmentIndex] =
                    appointment.copy(status = AppointmentStatus.CANCELLED)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            print(e.message)
            false
        }
    }

    override suspend fun rescheduleAppointment(appointmentId: String, newDateTime: Long): Boolean {
        delay(1000) // Simulate network latency
        return try {
            val appointmentIndex = appointments.indexOfFirst { it.id == appointmentId }
            if (appointmentIndex != -1) {
                val appointment = appointments[appointmentIndex]
                appointments[appointmentIndex] = appointment.copy(appointmentDateTime = newDateTime)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            print(e.message)
            false
        }
    }

    override suspend fun getAvailableTimeSlots(
        salonId: String,
        serviceId: String,
        date: Long
    ): List<TimeSlot> {
        delay(800) // Simulate network latency

        val requestedDate = Instant.fromEpochMilliseconds(date)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date

        // Closed/holiday example: Sundays
        if (requestedDate.dayOfWeek == kotlinx.datetime.DayOfWeek.SUNDAY) return emptyList()

        // Create mock existing bookings to test overlap removal
        val tz = TimeZone.currentSystemDefault()
        val existing: List<Pair<LocalDateTime, LocalDateTime>> = listOf(
            // One booking at 10:00-10:45 and another at 16:30-17:15
            Pair(LocalDateTime(requestedDate.year, requestedDate.monthNumber, requestedDate.dayOfMonth, 10, 0),
                 LocalDateTime(requestedDate.year, requestedDate.monthNumber, requestedDate.dayOfMonth, 10, 45)),
            Pair(LocalDateTime(requestedDate.year, requestedDate.monthNumber, requestedDate.dayOfMonth, 16, 30),
                 LocalDateTime(requestedDate.year, requestedDate.monthNumber, requestedDate.dayOfMonth, 17, 15))
        )

        // Generate slots with breaks and buffer around bookings
        val slots = com.techtactoe.ayna.data.util.AvailabilityGenerator.generate(
            date = requestedDate,
            openHour = 9,
            closeHour = 19,
            stepMinutes = 15,
            breaks = listOf(LocalTime(13, 0) to LocalTime(14, 0)),
            existingBookings = existing,
            buffersMinutes = 0,
            timeZone = tz
        )
        return slots
    }

    override suspend fun joinWaitlist(request: WaitlistRequest): Boolean {
        delay(1000) // Simulate network latency
        return try {
            // In a real implementation, this would save to database
            // For now, just simulate success
            true
        } catch (e: Exception) {
            print(e.message)
            false
        }
    }
}