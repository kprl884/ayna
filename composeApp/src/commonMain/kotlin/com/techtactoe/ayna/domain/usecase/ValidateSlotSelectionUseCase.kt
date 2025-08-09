package com.techtactoe.ayna.domain.usecase

import com.techtactoe.ayna.domain.error.BookingError
import com.techtactoe.ayna.domain.model.TimeSlot
import kotlinx.datetime.Clock

/**
 * Validates whether a selected time slot can be booked.
 */
class ValidateSlotSelectionUseCase {
    sealed class Result {
        data object Ok : Result()
        data class Error(val error: BookingError) : Result()
    }

    operator fun invoke(slot: TimeSlot?): Result {
        val s = slot ?: return Result.Error(BookingError.ValidationError)
        val now = Clock.System.now().toEpochMilliseconds()
        if (!s.isAvailable) return Result.Error(BookingError.SlotUnavailable)
        if (s.dateTime < now) return Result.Error(BookingError.ValidationError)
        return Result.Ok
    }
}
