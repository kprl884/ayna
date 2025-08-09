package com.techtactoe.ayna.data.supabase.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Objects for Supabase API responses
 * These DTOs match the database schema exactly
 */

@Serializable
data class SalonDto(
    val id: String,
    val name: String,
    val description: String? = null,
    val address: String,
    val city: String,
    val district: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    val rating: Double = 0.0,
    @SerialName("review_count")
    val reviewCount: Int = 0,
    @SerialName("venue_type")
    val venueType: String = "EVERYONE",
    @SerialName("is_open")
    val isOpen: Boolean = true,
    @SerialName("is_featured")
    val isFeatured: Boolean = false,
    val tags: List<String> = emptyList(),
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class ServiceDto(
    val id: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("category_id")
    val categoryId: String? = null,
    val name: String,
    val description: String? = null,
    @SerialName("price_cents")
    val priceCents: Int,
    @SerialName("duration_minutes")
    val durationMinutes: Int,
    @SerialName("gender_restriction")
    val genderRestriction: String? = null,
    @SerialName("is_active")
    val isActive: Boolean = true,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class EmployeeDto(
    val id: String,
    @SerialName("salon_id")
    val salonId: String,
    val name: String,
    val role: String,
    val specialty: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    val rating: Double = 0.0,
    @SerialName("review_count")
    val reviewCount: Int = 0,
    @SerialName("is_active")
    val isActive: Boolean = true,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class AppointmentDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("service_id")
    val serviceId: String,
    @SerialName("employee_id")
    val employeeId: String? = null,
    @SerialName("time_slot_id")
    val timeSlotId: String? = null,
    @SerialName("appointment_date")
    val appointmentDate: String,
    @SerialName("duration_minutes")
    val durationMinutes: Int,
    @SerialName("price_cents")
    val priceCents: Int,
    val status: String = "UPCOMING",
    val notes: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class ReviewDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("appointment_id")
    val appointmentId: String? = null,
    val rating: Int,
    val comment: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class TimeSlotDto(
    val id: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("employee_id")
    val employeeId: String? = null,
    @SerialName("service_id")
    val serviceId: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("is_available")
    val isAvailable: Boolean = true,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class ProfileDto(
    val id: String,
    val name: String,
    val email: String,
    @SerialName("phone_number")
    val phoneNumber: String? = null,
    @SerialName("profile_picture_url")
    val profilePictureUrl: String? = null,
    @SerialName("loyalty_points")
    val loyaltyPoints: Int = 0,
    @SerialName("member_since")
    val memberSince: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class WaitlistRequestDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("service_id")
    val serviceId: String,
    @SerialName("preferred_date")
    val preferredDate: String,
    @SerialName("preferred_time_range")
    val preferredTimeRange: String,
    val status: String = "PENDING",
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

@Serializable
data class NotificationDto(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    val title: String,
    val message: String,
    val type: String,
    @SerialName("is_read")
    val isRead: Boolean = false,
    @SerialName("action_text")
    val actionText: String? = null,
    @SerialName("action_route")
    val actionRoute: String? = null,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class SalonImageDto(
    val id: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("image_url")
    val imageUrl: String,
    @SerialName("alt_text")
    val altText: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int = 0,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class OpeningHourDto(
    val id: String,
    @SerialName("salon_id")
    val salonId: String,
    @SerialName("day_of_week")
    val dayOfWeek: Int,
    @SerialName("is_open")
    val isOpen: Boolean,
    @SerialName("open_time")
    val openTime: String? = null,
    @SerialName("close_time")
    val closeTime: String? = null,
    @SerialName("created_at")
    val createdAt: String
)

@Serializable
data class ServiceCategoryDto(
    val id: String,
    val name: String,
    @SerialName("display_name")
    val displayName: String,
    val emoji: String? = null,
    @SerialName("sort_order")
    val sortOrder: Int = 0,
    @SerialName("created_at")
    val createdAt: String
)