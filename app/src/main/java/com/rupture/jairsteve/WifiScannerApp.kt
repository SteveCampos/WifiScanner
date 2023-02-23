package com.rupture.jairsteve

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WifiScannerApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}