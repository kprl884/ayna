package com.techtactoe.ayna.util

actual object Platform {
    actual val isAndroid: Boolean = false
    actual val isIos: Boolean = true
}

actual fun launchGoogleSignIn() {
    // No-op on iOS
}
