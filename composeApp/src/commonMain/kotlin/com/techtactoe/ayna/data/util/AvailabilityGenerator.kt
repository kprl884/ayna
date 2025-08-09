package com.techtactoe.ayna.data.util

import com.techtactoe.ayna.domain.model.TimeSlot
import kotlinx.datetime.*
import kotlinx.datetime.LocalDate
import kotlin.time.ExperimentalTime

/**
 * Deterministic availability generator for mock data.
 * - Working hours: [openHour, closeHour)
 * - Step: stepMinutes
 * - Breaks: list of closed intervals within the day (local times)
 * - Buffers: minutes applied around existing bookings when filtering out overlaps
 */
object AvailabilityGenerator {
    /**
     * Generate all slots for a given date with 15-min granularity and filter out overlaps
     * with existing bookings and breaks/closed times.
     */
    @OptIn(ExperimentalTime::class)
    fun generate(
        date: LocalDate,
        openHour: Int = 9,
        closeHour: Int = 19,
        stepMinutes: Int = 15,
        breaks: List<Pair<LocalTime, LocalTime>> = listOf(LocalTime(13, 0) to LocalTime(14, 0)),
        existingBookings: List<Pair<LocalDateTime, LocalDateTime>> = emptyList(),
        buffersMinutes: Int = 0,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): List<TimeSlot> {
        val slots = mutableListOf<TimeSlot>()
        val t = LocalDateTime(date.year, date.month.number, date.day, openHour, 0)
        val endOfDay = LocalDateTime(date.year, date.month.number, date.day, closeHour, 0)
        var tMs = t.toInstant(timeZone).toEpochMilliseconds()
        val endMs = endOfDay.toInstant(timeZone).toEpochMilliseconds()

        fun overlapsBreak(start: LocalDateTime, end: LocalDateTime): Boolean {
            return breaks.any { (bs, be) ->
                val bStart = LocalDateTime(date.year, date.month.number, date.day, bs.hour, bs.minute)
                val bEnd = LocalDateTime(date.year, date.month.number, date.day, be.hour, be.minute)
                start < bEnd && end > bStart
            }
        }

        fun overlapsExisting(start: LocalDateTime, end: LocalDateTime): Boolean {
            val sMillis = start.toInstant(timeZone).toEpochMilliseconds()
            val eMillis = end.toInstant(timeZone).toEpochMilliseconds()
            val bufferMs = buffersMinutes * 60_000L
            return existingBookings.any { (bs, be) ->
                val bsMs = bs.toInstant(timeZone).toEpochMilliseconds() - bufferMs
                val beMs = be.toInstant(timeZone).toEpochMilliseconds() + bufferMs
                sMillis < beMs && eMillis > bsMs
            }
        }

        while (tMs < endMs) {
            val nextMs = tMs + stepMinutes * 60_000L
            val tLdt = Instant.fromEpochMilliseconds(tMs).toLocalDateTime(timeZone)
            val eLdt = Instant.fromEpochMilliseconds(nextMs).toLocalDateTime(timeZone)
            val blocked = overlapsBreak(tLdt, eLdt) || overlapsExisting(tLdt, eLdt)
            if (!blocked) {
                val label = tLdt.hour.toString().padStart(2, '0') + ":" + tLdt.minute.toString().padStart(2, '0')
                slots += TimeSlot(dateTime = tMs, isAvailable = true, formattedTime = label)
            }
            tMs = nextMs
        }
        return slots
    }
}
