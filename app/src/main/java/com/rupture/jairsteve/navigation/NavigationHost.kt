package com.rupture.jairsteve.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rupture.jairsteve.scan.destination.ScanDestination
import com.rupture.jairsteve.scan.destination.scanScreen

@Composable
fun WifiScannerNavHost() {
    NavHost(
        navController = rememberNavController(),
        startDestination = ScanDestination.route
    ) {
        scanScreen()
    }
}