package com.techtactoe.ayna.data.supabase.mapper

import com.techtactoe.ayna.data.supabase.dto.*
import com.techtactoe.ayna.domain.model.*
import kotlinx.datetime.Instant

/**
 * Mappers to convert between DTOs and domain models
 * Following the Clean Architecture principle of data transformation at boundaries
 */

fun SalonDto.toDomain(
    services: List<Service> = emptyList(),
    employees: List<Employee> = emptyList(),
    images: List<String> = emptyList(),
    operatingHours: Map<DayOfWeek, String> = emptyMap()
): Salon {
    return Salon(
        id = id,
        name = name,
        location = Location(
            address = address,
            city = city,
            latitude = latitude ?: 0.0,
            longitude = longitude ?: 0.0
        ),
        imageUrls = images,
        rating = rating,
        reviewCount = reviewCount,
        operatingHours = operatingHours,
        employees = employees,
        services = services,
        description = description ?: "",
        phoneNumber = phoneNumber ?: "",
        isOpen = isOpen,
        address = address,
        imageUrl = images.firstOrNull() ?: "",
        tags = tags
    )
}

fun ServiceDto.toDomain(): Service {
    return Service(
        id = id,
        name = name,
        description = description ?: "",
        price = priceCents.toDouble() / 100.0, // Convert cents to currency
        durationInMinutes = durationMinutes,
        category = "General" // TODO: Map from category_id
    )
}

fun EmployeeDto.toDomain(): Employee {
    return Employee(
        id = id,
        name = name,
        specialty = specialty ?: role,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount
    )
}

fun AppointmentDto.toDomain(
    salonName: String = "",
    serviceName: String = "",
    employeeName: String = ""
): Appointment {
    return Appointment(
        id = id,
        salonId = salonId,
        salonName = salonName,
        serviceName = serviceName,
        employeeId = employeeId ?: "",
        employeeName = employeeName,
        appointmentDateTime = Instant.parse(appointmentDate).toEpochMilliseconds(),
        status = AppointmentStatus.valueOf(status),
        price = priceCents.toDouble() / 100.0,
        durationInMinutes = durationMinutes,
        notes = notes ?: ""
    )
}

fun ReviewDto.toDomain(
    userName: String = "",
    userInitials: String = ""
): Review {
    return Review(
        id = id,
        userName = userName,
        userInitials = userInitials,
        date = formatDate(createdAt),
        rating = rating,
        comment = comment ?: ""
    )
}

fun TimeSlotDto.toDomain(): TimeSlot {
    return TimeSlot(
        dateTime = Instant.parse(startTime).toEpochMilliseconds(),
        isAvailable = isAvailable,
        formattedTime = formatTime(startTime)
    )
}

fun ProfileDto.toDomain(
    paymentMethods: List<PaymentMethod> = emptyList(),
    favoriteServices: List<String> = emptyList()
): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        email = email,
        profilePictureUrl = profilePictureUrl,
        phoneNumber = phoneNumber ?: "",
        paymentMethods = paymentMethods,
        favoriteServices = favoriteServices,
        loyaltyPoints = loyaltyPoints,
        memberSince = Instant.parse(memberSince).toEpochMilliseconds()
    )
}

fun WaitlistRequestDto.toDomain(): WaitlistRequest {
    return WaitlistRequest(
        id = id,
        userId = userId,
        salonId = salonId,
        serviceId = serviceId,
        preferredDate = Instant.parse(preferredDate).toEpochMilliseconds(),
        preferredTimeRange = preferredTimeRange,
        status = WaitlistStatus.valueOf(status),
        createdAt = Instant.parse(createdAt).toEpochMilliseconds()
    )
}

fun NotificationDto.toDomain(): NotificationItem {
    return NotificationItem(
        id = id,
        message = message,
        timestamp = formatDate(createdAt),
        isRead = isRead,
        type = NotificationType.valueOf(type),
        actionText = actionText,
        actionRoute = actionRoute
    )
}

// Domain to DTO mappers for creating/updating data

fun Appointment.toCreateDto(): Map<String, Any> {
    return mapOf(
        "user_id" to "user123", // TODO: Get from auth
        "salon_id" to salonId,
        "service_id" to "service123", // TODO: Map from service selection
        "employee_id" to employeeId.takeIf { it.isNotBlank() },
        "appointment_date" to Instant.fromEpochMilliseconds(appointmentDateTime).toString(),
        "duration_minutes" to durationInMinutes,
        "price_cents" to (price * 100).toInt(),
        "status" to status.name,
        "notes" to notes.takeIf { it.isNotBlank() }
    ).filterValues { it != null }
}

fun WaitlistRequest.toCreateDto(): Map<String, Any> {
    return mapOf(
        "user_id" to userId,
        "salon_id" to salonId,
        "service_id" to serviceId,
        "preferred_date" to Instant.fromEpochMilliseconds(preferredDate).toString().split("T")[0],
        "preferred_time_range" to preferredTimeRange,
        "status" to status.name
    )
}

fun UserProfile.toUpdateDto(): Map<String, Any> {
    return mapOf(
        "name" to name,
        "phone_number" to phoneNumber.takeIf { it.isNotBlank() },
        "profile_picture_url" to profilePictureUrl
    ).filterValues { it != null }
}

// Helper functions for date/time formatting
private fun formatDate(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        // TODO: Implement proper date formatting based on locale
        "Recently" // Placeholder
    } catch (e: Exception) {
        "Unknown"
    }
}

private fun formatTime(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val dateTime = instant.toString()
        val time = dateTime.split("T")[1].split(":").take(2).joinToString(":")
        time
    } catch (e: Exception) {
        "00:00"
    }
}