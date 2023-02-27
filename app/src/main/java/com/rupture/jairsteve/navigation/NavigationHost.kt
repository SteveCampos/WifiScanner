package com.rupture.jairsteve.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.rupture.jairsteve.scan.destination.ScanDestination
import com.rupture.jairsteve.scan.destination.scanDetailScreen
import com.rupture.jairsteve.scan.destination.scanScreen

@Composable
fun WifiScannerNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScanDestination.route
    ) {
        scanScreen {
            navController.navigate("/scan-detail")
        }
        scanDetailScreen()
    }
}