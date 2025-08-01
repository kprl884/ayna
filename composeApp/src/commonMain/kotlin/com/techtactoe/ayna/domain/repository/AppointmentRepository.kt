package com.techtactoe.ayna.domain.repository

import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.model.WaitlistRequest

/**
 * Repository interface for appointment data operations
 */
interface AppointmentRepository {
    /**
     * Get all appointments for a specific user
     */
    suspend fun getUserAppointments(userId: String): List<Appointment>
    
    /**
     * Get upcoming appointments for a user
     */
    suspend fun getUpcomingAppointments(userId: String): List<Appointment>
    
    /**
     * Get past appointments for a user
     */
    suspend fun getPastAppointments(userId: String): List<Appointment>
    
    /**
     * Create a new appointment
     */
    suspend fun createAppointment(appointment: Appointment): Boolean
    
    /**
     * Cancel an existing appointment
     */
    suspend fun cancelAppointment(appointmentId: String): Boolean
    
    /**
     * Reschedule an appointment
     */
    suspend fun rescheduleAppointment(appointmentId: String, newDateTime: Long): Boolean

    /**
     * Get available time slots for a specific salon, service and date
     */
    suspend fun getAvailableTimeSlots(salonId: String, serviceId: String, date: Long): List<TimeSlot>

    /**
     * Join waitlist when no slots are available
     */
    suspend fun joinWaitlist(request: WaitlistRequest): Boolean
}
