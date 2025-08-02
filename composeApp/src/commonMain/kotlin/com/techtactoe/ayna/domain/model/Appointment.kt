package com.techtactoe.ayna.domain.model

/**
 * Represents a user's appointment at a salon
 */
data class Appointment(
    val id: String,
    val salonId: String,
    val salonName: String,
    val serviceName: String,
    val employeeId: String,
    val employeeName: String,
    val appointmentDateTime: Long,
    val status: AppointmentStatus,
    val price: Double,
    val durationInMinutes: Int,
    val notes: String = ""
)
