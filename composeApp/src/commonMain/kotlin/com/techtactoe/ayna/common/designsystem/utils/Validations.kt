package com.techtactoe.ayna.common.designsystem.utils

object Validations {
    val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
    val PASSWORD_REGEX = Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}")
}