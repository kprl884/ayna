package com.techtactoe.ayna.domain.model

/**
 * Represents the status of an appointment
 */
enum class AppointmentStatus {
    UPCOMING,
    COMPLETED,
    CANCELLED
}

/**
 * Represents a user's appointment at a salon
 */
data class Appointment(
    val id: String,
    val salonId: String,
    val salonName: String, // Denormalized for easy display
    val serviceName: String, // Denormalized for easy display
    val employeeId: String,
    val employeeName: String, // Denormalized for easy display
    val appointmentDateTime: Long, // UTC Timestamp
    val status: AppointmentStatus,
    val price: Double,
    val durationInMinutes: Int,
    val notes: String = ""
)
