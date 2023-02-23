package com.rupture.jairsteve.navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rupture.jairsteve.navigation.ui.theme.WifiScannerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WifiScannerTheme {
                WifiScannerNavHost()
            }
        }
    }
}