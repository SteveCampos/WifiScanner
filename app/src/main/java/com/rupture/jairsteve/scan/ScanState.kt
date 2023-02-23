package com.rupture.jairsteve.scan

import android.net.wifi.ScanResult


sealed class ScanState {
    data class SuccessScan(val resultsUpdated: Boolean, val items: List<ScanResult>) : ScanState()
    data class SecurityExceptionOnScan(val tryAgain: () -> Unit) : ScanState()
    class StartScanFailed(val tryAgain: () -> Unit) : ScanState()

    object PerformingScan : ScanState()
}
