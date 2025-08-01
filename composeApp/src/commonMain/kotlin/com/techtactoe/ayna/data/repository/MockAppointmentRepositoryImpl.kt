package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.repository.AppointmentRepository
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
}
