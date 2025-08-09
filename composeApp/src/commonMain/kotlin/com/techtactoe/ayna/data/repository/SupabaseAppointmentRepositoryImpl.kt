package com.techtactoe.ayna.data.repository

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import com.techtactoe.ayna.data.supabase.dto.AppointmentDto
import com.techtactoe.ayna.data.supabase.dto.TimeSlotDto
import com.techtactoe.ayna.data.supabase.mapper.appointmentDtoToAppointmentDomainModel
import com.techtactoe.ayna.data.supabase.mapper.timeSlotDtoToTimeSlotDomainModel
import com.techtactoe.ayna.data.supabase.mapper.toCreateDto
import com.techtactoe.ayna.domain.model.Appointment
import com.techtactoe.ayna.domain.model.TimeSlot
import com.techtactoe.ayna.domain.model.WaitlistRequest
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Supabase implementation of AppointmentRepository
 * Handles all appointment-related data operations
 */
class SupabaseAppointmentRepositoryImpl : AppointmentRepository {

    private val client = AynaSupabaseClient.database

    override suspend fun getUserAppointments(userId: String): List<Appointment> {
        return try {
            val appointmentsResponse = client
                .from("appointments")
                .select(
                    columns = Columns.raw(
                        """
                        *,
                        salons!inner(name),
                        services!inner(name),
                        employees(name)
                        """.trimIndent()
                    )
                ) {
                    filter { eq("user_id", userId) }
                    order("appointment_date", order = Order.DESCENDING)
                }
                .decodeList<Map<String, Any>>()

            appointmentsResponse.map { appointmentMap: Map<String, Any> ->
                val appointmentDto = parseAppointmentFromMap(appointmentMap)
                val salonName =
                    (appointmentMap["salons"] as? Map<String, Any>)?.get("name") as? String ?: ""
                val serviceName =
                    (appointmentMap["services"] as? Map<String, Any>)?.get("name") as? String ?: ""
                val employeeName =
                    (appointmentMap["employees"] as? Map<String, Any>)?.get("name") as? String ?: ""

                appointmentDto.appointmentDtoToAppointmentDomainModel(
                    salonName = salonName,
                    serviceName = serviceName,
                    employeeName = employeeName
                )
            }
        } catch (e: Exception) {
            println("Error fetching user appointments: ${e.message}")
            throw e
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getUpcomingAppointments(userId: String): List<Appointment> {
        return try {
            val appointmentsResponse = client
                .from("appointments")
                .select(
                    columns = Columns.raw(
                        """
                        *,
                        salons!inner(name),
                        services!inner(name),
                        employees(name)
                        """.trimIndent()
                    )
                ) {
                    filter {
                        eq("user_id", userId)
                        eq("status", "UPCOMING")
                        gte("appointment_date", Clock.System.now().toEpochMilliseconds().toString())
                    }
                    order("appointment_date", order = Order.ASCENDING)
                }
                .decodeList<Map<String, Any>>()

            appointmentsResponse.map { appointmentMap: Map<String, Any> ->
                val appointmentDto = parseAppointmentFromMap(appointmentMap)
                val salonName =
                    (appointmentMap["salons"] as? Map<String, Any>)?.get("name") as? String ?: ""
                val serviceName =
                    (appointmentMap["services"] as? Map<String, Any>)?.get("name") as? String ?: ""
                val employeeName =
                    (appointmentMap["employees"] as? Map<String, Any>)?.get("name") as? String ?: ""

                appointmentDto.appointmentDtoToAppointmentDomainModel(
                    salonName = salonName,
                    serviceName = serviceName,
                    employeeName = employeeName
                )
            }
        } catch (e: Exception) {
            println("Error fetching upcoming appointments: ${e.message}")
            throw e
        }
    }

    override suspend fun getPastAppointments(userId: String): List<Appointment> {
        return try {
            val appointmentsResponse = client
                .from("appointments")
                .select(
                    columns = Columns.raw(
                        """
                        *,
                        salons!inner(name),
                        services!inner(name),
                        employees(name)
                        """.trimIndent()
                    )
                ) {
                    filter {
                        eq("user_id", userId)
                    }
                    order("appointment_date", order = Order.DESCENDING)
                }
                .decodeList<Map<String, Any>>()

            appointmentsResponse.map { appointmentMap: Map<String, Any> ->
                val appointmentDto = parseAppointmentFromMap(appointmentMap)
                val salonName =
                    (appointmentMap["salons"] as? Map<String, Any>)?.get("name") as? String ?: ""
                val serviceName =
                    (appointmentMap["services"] as? Map<String, Any>)?.get("name") as? String ?: ""
                val employeeName =
                    (appointmentMap["employees"] as? Map<String, Any>)?.get("name") as? String ?: ""

                appointmentDto.appointmentDtoToAppointmentDomainModel(
                    salonName = salonName,
                    serviceName = serviceName,
                    employeeName = employeeName
                )
            }
        } catch (e: Exception) {
            println("Error fetching past appointments: ${e.message}")
            throw e
        }
    }

    override suspend fun createAppointment(appointment: Appointment): Boolean {
        return try {
            client
                .from("appointments")
                .insert(appointment.toCreateDto())

            true
        } catch (e: Exception) {
            println("Error creating appointment: ${e.message}")
            false
        }
    }

    override suspend fun cancelAppointment(appointmentId: String): Boolean {
        return try {
            client
                .from("appointments")
                .update(mapOf("status" to "CANCELLED")) {
                    filter { eq("id", appointmentId) }
                }

            true
        } catch (e: Exception) {
            println("Error cancelling appointment: ${e.message}")
            false
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun rescheduleAppointment(appointmentId: String, newDateTime: Long): Boolean {
        return try {
            client
                .from("appointments")
                .update(
                    mapOf(
                        "appointment_date" to kotlinx.datetime.Instant.fromEpochMilliseconds(
                            newDateTime
                        ).toString()
                    )
                ) {
                    filter { eq("id", appointmentId) }
                }

            true
        } catch (e: Exception) {
            println("Error rescheduling appointment: ${e.message}")
            false
        }
    }

    @OptIn(ExperimentalTime::class)
    override suspend fun getAvailableTimeSlots(
        salonId: String,
        serviceId: String,
        date: Long
    ): List<TimeSlot> {
        return try {
            val dateString =
                kotlinx.datetime.Instant.fromEpochMilliseconds(date).toString().split("T")[0]

            val timeSlotsResponse = client
                .from("time_slots")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("salon_id", salonId)
                        eq("service_id", serviceId)
                        gte("start_time", "${dateString}T00:00:00Z")
                        lt("start_time", "${dateString}T23:59:59Z")
                        eq("is_available", true)
                    }
                    order("start_time", order = Order.ASCENDING)
                }
                .decodeList<TimeSlotDto>()

            timeSlotsResponse.map { dto: TimeSlotDto -> dto.timeSlotDtoToTimeSlotDomainModel() }
        } catch (e: Exception) {
            println("Error fetching available time slots: ${e.message}")
            emptyList()
        }
    }

    override suspend fun joinWaitlist(request: WaitlistRequest): Boolean {
        return try {
            client
                .from("waitlist_requests")
                .insert(request.toCreateDto())

            true
        } catch (e: Exception) {
            println("Error joining waitlist: ${e.message}")
            false
        }
    }

    // Helper function to parse appointment from complex query result
    private fun parseAppointmentFromMap(map: Map<String, Any>): AppointmentDto {
        return AppointmentDto(
            id = map["id"] as String,
            userId = map["user_id"] as String,
            salonId = map["salon_id"] as String,
            serviceId = map["service_id"] as String,
            employeeId = map["employee_id"] as? String,
            timeSlotId = map["time_slot_id"] as? String,
            appointmentDate = map["appointment_date"] as String,
            durationMinutes = (map["duration_minutes"] as Number).toInt(),
            priceCents = (map["price_cents"] as Number).toInt(),
            status = map["status"] as String,
            notes = map["notes"] as? String,
            createdAt = map["created_at"] as String,
            updatedAt = map["updated_at"] as String
        )
    }
}