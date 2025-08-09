package com.techtactoe.ayna.data.supabase.mapper

import com.techtactoe.ayna.data.supabase.dto.AppointmentDto
import com.techtactoe.ayna.data.supabase.dto.EmployeeDto
import com.techtactoe.ayna.data.supabase.dto.NotificationDto
import com.techtactoe.ayna.data.supabase.dto.ProfileDto
import com.techtactoe.ayna.data.supabase.dto.ReviewDto
import com.techtactoe.ayna.data.supabase.dto.SalonDto
import com.techtactoe.ayna.data.supabase.dto.ServiceDto
import com.techtactoe.ayna.data.supabase.dto.TimeSlotDto
import com.techtactoe.ayna.data.supabase.dto.WaitlistRequestDto
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.AppointmentStatus
import com.techtactoe.ayna.domain.model.DayOfWeek
import com.techtactoe.ayna.domain.model.Employee
import com.techtactoe.ayna.domain.model.Location
import com.techtactoe.ayna.domain.model.NotificationItem
import com.techtactoe.ayna.domain.model.NotificationType
import com.techtactoe.ayna.domain.model.PaymentMethod
import com.techtactoe.ayna.domain.model.Review
import com.techtactoe.ayna.domain.model.Salon
import com.techtactoe.ayna.domain.model.Service
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.model.UserProfile
import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.model.WaitlistStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Mappers to convert between DTOs and domain models
 * Following the Clean Architecture principle of data transformation at boundaries
 */

fun SalonDto.salonDtoToSalonDomainModel(
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

fun ServiceDto.serviceDtoToServiceDomainModel(): Service {
    return Service(
        id = id,
        name = name,
        description = description ?: "",
        price = priceCents.toDouble() / 100.0, // Convert cents to currency
        durationInMinutes = durationMinutes,
        category = "General" // TODO: Map from category_id
    )
}

fun EmployeeDto.employeeDtoToEmployeeDomainModel(): Employee {
    return Employee(
        id = id,
        name = name,
        specialty = specialty ?: role,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount
    )
}

@OptIn(ExperimentalTime::class)
fun AppointmentDto.appointmentDtoToAppointmentDomainModel(
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

fun ReviewDto.reviewDtoToReviewDomainModel(
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

@OptIn(ExperimentalTime::class)
fun TimeSlotDto.timeSlotDtoToTimeSlotDomainModel(): TimeSlot {
    return TimeSlot(
        dateTime = Instant.parse(startTime).toEpochMilliseconds(),
        isAvailable = isAvailable,
        formattedTime = formatTime(startTime)
    )
}

@OptIn(ExperimentalTime::class)
fun ProfileDto.profileDtoToUserProfileDomainModel(
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

@OptIn(ExperimentalTime::class)
fun WaitlistRequestDto.waitlistRequestDtoToWaitlistRequestDomainModel(): WaitlistRequest {
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

fun NotificationDto.notificationDtoToNotificationItemDomainModel(): NotificationItem {
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

@OptIn(ExperimentalTime::class)
fun Appointment.toCreateDto(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    // TODO: Replace with actual authenticated user id
    map["user_id"] = "user123"
    map["salon_id"] = salonId
    map["service_id"] = "service123" // TODO: Map from service selection
    if (employeeId.isNotBlank()) map["employee_id"] = employeeId
    map["appointment_date"] = Instant.fromEpochMilliseconds(appointmentDateTime).toString()
    map["duration_minutes"] = durationInMinutes
    map["price_cents"] = (price * 100).toInt()
    map["status"] = status.name
    if (notes.isNotBlank()) map["notes"] = notes
    return map
}

@OptIn(ExperimentalTime::class)
fun WaitlistRequest.toCreateDto(): Map<String, Any> {
    return buildMap {
        put("user_id", userId)
        put("salon_id", salonId)
        put("service_id", serviceId)
        put("preferred_date", Instant.fromEpochMilliseconds(preferredDate).toString().split("T")[0])
        put("preferred_time_range", preferredTimeRange)
        put("status", status.name)
    }
}

fun UserProfile.toUpdateDto(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    map["name"] = name
    if (phoneNumber.isNotBlank()) map["phone_number"] = phoneNumber
    profilePictureUrl?.let { map["profile_picture_url"] = it }
    return map
}

// Helper functions for date/time formatting
@OptIn(ExperimentalTime::class)
private fun formatDate(isoString: String): String {
    return try {
        Instant.parse(isoString)
        // TODO: Implement proper date formatting based on locale
        "Recently" // Placeholder
    } catch (e: Exception) {
        println("Error formatting date: ${e.message}")
        "Unknown"
    }
}

@OptIn(ExperimentalTime::class)
private fun formatTime(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val dateTime = instant.toString()
        val time = dateTime.split("T")[1].split(":").take(2).joinToString(":")
        time
    } catch (e: Exception) {
        println("Error formatting time: ${e.message}")
        "00:00"
    }
}