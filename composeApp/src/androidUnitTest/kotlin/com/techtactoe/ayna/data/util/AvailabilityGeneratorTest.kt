package com.techtactoe.ayna.data.util

import kotlinx.datetime.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AvailabilityGeneratorTest {

    @Test
    fun generates_no_slots_on_sunday() {
        // Find next Sunday
        var date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        while (date.dayOfWeek != DayOfWeek.SUNDAY) {
            date = date.plus(1, DateTimeUnit.DAY)
        }
        val slots = AvailabilityGenerator.generate(date)
        assertTrue(slots.isEmpty())
    }

    @Test
    fun removes_slots_during_lunch_break() {
        // Use a known weekday
        var date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (date.dayOfWeek == DayOfWeek.SUNDAY) date = date.plus(1, DateTimeUnit.DAY)
        val slots = AvailabilityGenerator.generate(date, breaks = listOf(LocalTime(13,0) to LocalTime(14,0)))
        // Ensure no slot starts at 13:00 or 13:15 etc.
        val hasBreakSlot = slots.any { slot ->
            val t = Instant.fromEpochMilliseconds(slot.dateTime).toLocalDateTime(TimeZone.currentSystemDefault()).time
            t.hour == 13
        }
        assertFalse(hasBreakSlot)
    }
}
