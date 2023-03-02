package com.rupture.jairsteve.scan


sealed class ScanState<T> {
    data class SuccessScan<T>(val resultsUpdated: Boolean, val items: List<T>) : ScanState<T>()
    data class SecurityExceptionOnScan<T>(val tryAgain: () -> Unit) : ScanState<T>()
    class StartScanFailed<T>(val tryAgain: () -> Unit) : ScanState<T>()
    class PerformingScan<T> : ScanState<T>()
}
