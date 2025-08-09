package com.techtactoe.ayna.data.realtime

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.filter
import kotlinx.serialization.json.JsonObject

/**
 * Real-time data manager using Supabase Realtime
 * Handles live updates for appointments, notifications, and availability
 */
class SupabaseRealtimeManager {

    private val realtime = AynaSupabaseClient.realtime

    /**
     * Listen to appointment changes for a specific user
     */
    fun listenToUserAppointments(userId: String): Flow<AppointmentRealtimeEvent> {
        val channel = realtime.channel("user_appointments_$userId")

        return channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "appointments"
        }
        .filter { action ->
            when (action) {
                is PostgresAction.Insert -> action.record["user_id"]?.toString()?.trim('"') == userId
                is PostgresAction.Update -> action.record["user_id"]?.toString()?.trim('"') == userId
                is PostgresAction.Delete -> action.oldRecord?.get("user_id")?.toString()?.trim('"') == userId
                else -> false
            }
        }
        .map { action: PostgresAction ->
            when (action) {
                is PostgresAction.Insert -> AppointmentRealtimeEvent.Created(action.record)
                is PostgresAction.Update -> AppointmentRealtimeEvent.Updated(action.record)
                is PostgresAction.Delete -> AppointmentRealtimeEvent.Deleted(action.oldRecord)
                else -> AppointmentRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Listen to time slot availability changes for a salon
     */
    fun listenToTimeSlotChanges(salonId: String): Flow<TimeSlotRealtimeEvent> {
        val channel = realtime.channel("salon_slots_$salonId")

        return channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "time_slots"
        }
        .filter { action ->
            when (action) {
                is PostgresAction.Insert -> action.record["salon_id"]?.toString()?.trim('"') == salonId
                is PostgresAction.Update -> action.record["salon_id"]?.toString()?.trim('"') == salonId
                is PostgresAction.Delete -> action.oldRecord?.get("salon_id")?.toString()?.trim('"') == salonId
                else -> false
            }
        }
        .map { action: PostgresAction ->
            when (action) {
                is PostgresAction.Insert -> TimeSlotRealtimeEvent.SlotAdded(action.record)
                is PostgresAction.Update -> TimeSlotRealtimeEvent.SlotUpdated(action.record)
                is PostgresAction.Delete -> TimeSlotRealtimeEvent.SlotRemoved(action.oldRecord)
                else -> TimeSlotRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Listen to new notifications for a user
     */
    fun listenToUserNotifications(userId: String): Flow<NotificationRealtimeEvent> {
        val channel = realtime.channel("user_notifications_$userId")

        return channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "notifications"
        }
        .filter { action ->
            when (action) {
                is PostgresAction.Insert -> action.record["user_id"]?.toString()?.trim('"') == userId
                is PostgresAction.Update -> action.record["user_id"]?.toString()?.trim('"') == userId
                is PostgresAction.Delete -> action.oldRecord?.get("user_id")?.toString()?.trim('"') == userId
                else -> false
            }
        }
        .map { action: PostgresAction ->
            when (action) {
                is PostgresAction.Insert -> NotificationRealtimeEvent.NewNotification(action.record)
                is PostgresAction.Update -> NotificationRealtimeEvent.NotificationUpdated(action.record)
                else -> NotificationRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Listen to waitlist status changes
     */
    fun listenToWaitlistUpdates(userId: String): Flow<WaitlistRealtimeEvent> {
        val channel = realtime.channel("user_waitlist_$userId")

        return channel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "waitlist_requests"
        }
        .filter { action ->
            when (action) {
                is PostgresAction.Update -> action.record["user_id"]?.toString()?.trim('"') == userId
                is PostgresAction.Insert -> action.record["user_id"]?.toString()?.trim('"') == userId
                is PostgresAction.Delete -> action.oldRecord?.get("user_id")?.toString()?.trim('"') == userId
                else -> false
            }
        }
        .map { action: PostgresAction ->
            when (action) {
                is PostgresAction.Update -> WaitlistRealtimeEvent.StatusChanged(action.record)
                else -> WaitlistRealtimeEvent.Unknown
            }
        }
    }
}

/**
 * Real-time event types for different data changes
 */
sealed interface AppointmentRealtimeEvent {
    data class Created(val data: JsonObject) : AppointmentRealtimeEvent
    data class Updated(val data: JsonObject) : AppointmentRealtimeEvent
    data class Deleted(val data: JsonObject?) : AppointmentRealtimeEvent
    data object Unknown : AppointmentRealtimeEvent
}

sealed interface TimeSlotRealtimeEvent {
    data class SlotAdded(val data: JsonObject) : TimeSlotRealtimeEvent
    data class SlotUpdated(val data: JsonObject) : TimeSlotRealtimeEvent
    data class SlotRemoved(val data: JsonObject?) : TimeSlotRealtimeEvent
    data object Unknown : TimeSlotRealtimeEvent
}

sealed interface NotificationRealtimeEvent {
    data class NewNotification(val data: JsonObject) : NotificationRealtimeEvent
    data class NotificationUpdated(val data: JsonObject) : NotificationRealtimeEvent
    data object Unknown : NotificationRealtimeEvent
}

sealed interface WaitlistRealtimeEvent {
    data class StatusChanged(val data: JsonObject) : WaitlistRealtimeEvent
    data object Unknown : WaitlistRealtimeEvent
}