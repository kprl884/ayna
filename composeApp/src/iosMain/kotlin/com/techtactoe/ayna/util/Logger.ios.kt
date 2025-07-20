package com.techtactoe.ayna.util

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSLog

@OptIn(ExperimentalForeignApi::class)
actual fun log(level: LogLevel, tag: String, message: String) {
    NSLog("[${level.name}] $tag: $message")
} 