package com.techtactoe.ayna.util

import android.util.Log

actual fun log(level: LogLevel, tag: String, message: String) {
    when (level) {
        LogLevel.DEBUG -> Log.d(tag, message)
        LogLevel.WARN -> Log.w(tag, message)
        LogLevel.ERROR -> Log.e(tag, message)
    }
} 