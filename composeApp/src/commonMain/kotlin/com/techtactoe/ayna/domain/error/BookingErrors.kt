package com.techtactoe.ayna.domain.error

/**
 * Domain-layer booking errors for explicit handling in UI.
 */
sealed interface BookingError {
    data object SlotUnavailable : BookingError
    data object ValidationError : BookingError
}
