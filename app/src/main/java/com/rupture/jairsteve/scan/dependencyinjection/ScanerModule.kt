package com.rupture.jairsteve.scan.dependencyinjection

import android.content.Context
import android.net.wifi.WifiManager
import com.rupture.jairsteve.scan.infrastructure.WifiScannerAndroid
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScanerModule {

    @Singleton
    @Provides
    fun provideWifiScanner(@ApplicationContext context: Context): WifiScannerAndroid =
        WifiScannerAndroid(
            wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager,
            context = context
        )


}