package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay

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
            appointmentDateTime = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000), // 2 days from now
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
            appointmentDateTime = System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000), // 5 days from now
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
            appointmentDateTime = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000), // 7 days ago
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
            appointmentDateTime = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000), // 3 days ago
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
            false
        }
    }
    
    override suspend fun cancelAppointment(appointmentId: String): Boolean {
        delay(800) // Simulate network latency
        return try {
            val appointmentIndex = appointments.indexOfFirst { it.id == appointmentId }
            if (appointmentIndex != -1) {
                val appointment = appointments[appointmentIndex]
                appointments[appointmentIndex] = appointment.copy(status = AppointmentStatus.CANCELLED)
                true
            } else {
                false
            }
        } catch (e: Exception) {
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
            false
        }
    }

    override suspend fun getAvailableTimeSlots(salonId: String, serviceId: String, date: Long): List<TimeSlot> {
        delay(1200) // Simulate network latency

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        // Return empty list for Sundays to simulate "fully booked" scenario
        if (dayOfWeek == Calendar.SUNDAY) {
            return emptyList()
        }

        // Generate realistic time slots for the day
        val timeSlots = mutableListOf<TimeSlot>()
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        // Morning slots (10:00 AM - 12:00 PM)
        val morningSlots = listOf("10:30", "11:00", "11:30")
        morningSlots.forEach { time ->
            val (hour, minute) = time.split(":").map { it.toInt() }
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            timeSlots.add(
                TimeSlot(
                    dateTime = calendar.timeInMillis,
                    isAvailable = true,
                    formattedTime = timeFormat.format(calendar.time)
                )
            )
        }

        // Afternoon slots (4:00 PM - 6:00 PM)
        val afternoonSlots = listOf("16:00", "16:15", "16:30", "16:45", "17:00", "17:15")
        afternoonSlots.forEach { time ->
            val (hour, minute) = time.split(":").map { it.toInt() }
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            timeSlots.add(
                TimeSlot(
                    dateTime = calendar.timeInMillis,
                    isAvailable = true,
                    formattedTime = timeFormat.format(calendar.time)
                )
            )
        }

        return timeSlots
    }

    override suspend fun joinWaitlist(request: WaitlistRequest): Boolean {
        delay(1000) // Simulate network latency
        return try {
            // In a real implementation, this would save to database
            // For now, just simulate success
            true
        } catch (e: Exception) {
            false
        }
    }
}
