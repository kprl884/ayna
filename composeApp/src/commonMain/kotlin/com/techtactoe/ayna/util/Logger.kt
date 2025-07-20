package com.techtactoe.ayna.util

enum class LogLevel {
    DEBUG, WARN, ERROR
}

expect fun log(level: LogLevel, tag: String, message: String) 