package com.rupture.jairsteve.scan.destination

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rupture.jairsteve.scan.ScanDetailScreen
import com.rupture.jairsteve.scan.ScanScreen
import com.rupture.jairsteve.scan.entity.MyScanResult

class ScanDestination {
    companion object {
        const val route = "/scan"
    }
}

fun NavGraphBuilder.scanScreen(onScanItemClicked: (MyScanResult) -> Unit) {
    composable(ScanDestination.route) {
        ScanScreen(onScanItemClicked)
    }
}

fun NavGraphBuilder.scanDetailScreen() {
    composable("/scan-detail") {
        ScanDetailScreen()
    }
}