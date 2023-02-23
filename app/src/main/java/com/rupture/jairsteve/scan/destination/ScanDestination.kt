package com.rupture.jairsteve.scan.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rupture.jairsteve.scan.ScanScreen

class ScanDestination {
    companion object {
        const val route = "/scan"
    }
}

fun NavGraphBuilder.scanScreen() {
    composable(ScanDestination.route) {
        ScanScreen()
    }
}