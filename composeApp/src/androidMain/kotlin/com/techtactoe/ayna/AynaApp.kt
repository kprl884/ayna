package com.techtactoe.ayna

import android.app.Application
import com.techtactoe.ayna.di.initKoin

class AynaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
