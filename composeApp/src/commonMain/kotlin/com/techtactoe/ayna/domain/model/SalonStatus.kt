package com.techtactoe.ayna.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class SalonStatus {
    OPEN, CLOSED, OPENS_LATER
}
