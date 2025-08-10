package com.techtactoe.ayna.util

// Platform flags available in common code
expect object Platform {
    val isAndroid: Boolean
    val isIos: Boolean
}

// Android-only: launch Google Sign-In UI. No-op on iOS.
expect fun launchGoogleSignIn()
