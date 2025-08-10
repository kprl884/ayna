package com.techtactoe.ayna.util

actual object Platform {
    actual val isAndroid: Boolean = true
    actual val isIos: Boolean = false
}

actual fun launchGoogleSignIn() {
    GoogleSignInLauncher.launch?.invoke()
}
