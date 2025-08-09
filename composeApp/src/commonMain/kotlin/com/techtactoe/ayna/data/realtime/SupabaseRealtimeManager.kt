package com.techtactoe.ayna.data.realtime

import com.techtactoe.ayna.data.supabase.AynaSupabaseClient
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        
        return channel.postgresChangeFlow<JsonObject>(schema = "public") {
            table = "appointments"
            filter = "user_id=eq.$userId"
        }.map { change ->
            when (change.eventType) {
                PostgresAction.INSERT -> AppointmentRealtimeEvent.Created(change.record)
                PostgresAction.UPDATE -> AppointmentRealtimeEvent.Updated(change.record)
                PostgresAction.DELETE -> AppointmentRealtimeEvent.Deleted(change.oldRecord)
                else -> AppointmentRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Listen to time slot availability changes for a salon
     */
    fun listenToTimeSlotChanges(salonId: String): Flow<TimeSlotRealtimeEvent> {
        val channel = realtime.channel("salon_slots_$salonId")
        
        return channel.postgresChangeFlow<JsonObject>(schema = "public") {
            table = "time_slots"
            filter = "salon_id=eq.$salonId"
        }.map { change ->
            when (change.eventType) {
                PostgresAction.INSERT -> TimeSlotRealtimeEvent.SlotAdded(change.record)
                PostgresAction.UPDATE -> TimeSlotRealtimeEvent.SlotUpdated(change.record)
                PostgresAction.DELETE -> TimeSlotRealtimeEvent.SlotRemoved(change.oldRecord)
                else -> TimeSlotRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Listen to new notifications for a user
     */
    fun listenToUserNotifications(userId: String): Flow<NotificationRealtimeEvent> {
        val channel = realtime.channel("user_notifications_$userId")
        
        return channel.postgresChangeFlow<JsonObject>(schema = "public") {
            table = "notifications"
            filter = "user_id=eq.$userId"
        }.map { change ->
            when (change.eventType) {
                PostgresAction.INSERT -> NotificationRealtimeEvent.NewNotification(change.record)
                PostgresAction.UPDATE -> NotificationRealtimeEvent.NotificationUpdated(change.record)
                else -> NotificationRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Listen to waitlist status changes
     */
    fun listenToWaitlistUpdates(userId: String): Flow<WaitlistRealtimeEvent> {
        val channel = realtime.channel("user_waitlist_$userId")
        
        return channel.postgresChangeFlow<JsonObject>(schema = "public") {
            table = "waitlist_requests"
            filter = "user_id=eq.$userId"
        }.map { change ->
            when (change.eventType) {
                PostgresAction.UPDATE -> WaitlistRealtimeEvent.StatusChanged(change.record)
                else -> WaitlistRealtimeEvent.Unknown
            }
        }
    }

    /**
     * Subscribe to a channel (call this to start listening)
     */
    suspend fun subscribeToChannel(channelName: String) {
        try {
            val channel = realtime.channel(channelName)
            channel.subscribe()
        } catch (e: Exception) {
            println("Error subscribing to channel $channelName: ${e.message}")
        }
    }

    /**
     * Unsubscribe from a channel
     */
    suspend fun unsubscribeFromChannel(channelName: String) {
        try {
            val channel = realtime.channel(channelName)
            channel.unsubscribe()
        } catch (e: Exception) {
            println("Error unsubscribing from channel $channelName: ${e.message}")
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