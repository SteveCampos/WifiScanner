package com.rupture.jairsteve.scan.infrastructure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import com.rupture.jairsteve.scan.ScanState
import kotlinx.coroutines.flow.MutableStateFlow

class WifiScannerAndroid(private val wifiManager: WifiManager, private val context: Context) {

    companion object {
        const val TAG = "ScanResultImpl"
    }


    private val _scanState: MutableStateFlow<ScanState<ScanResult>> =
        MutableStateFlow(ScanState.PerformingScan())
    val scanState /*: StateFlow<ScanState>*/ get() = _scanState

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            Log.d(TAG, "wifiScanReceiver onReceive: $success")
            getScannedResults(success)
        }
    }

    init {
        startScan()
    }

    fun startScan() {
        Log.d(TAG, "startScan")

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)

        context.registerReceiver(
            wifiScanReceiver, intentFilter
        )


        try {
            val startScanSuccess = wifiManager.startScan()
            if (!startScanSuccess) onStartScanFailure()
        } catch (throwable: SecurityException) {
            Log.e(TAG, throwable.stackTraceToString())
            onSecurityException()
        }
        // scan failure handling
    }

    private fun onStartScanFailure() {
        Log.d(TAG, "onStartScanFailure")
        _scanState.value = ScanState.StartScanFailed(::startScan)
    }

    private fun getScannedResults(resultsUpdated: Boolean) {
        Log.d(TAG, "getScannedResults")
        try {
            val results = wifiManager.scanResults
            _scanState.value = ScanState.SuccessScan(resultsUpdated, results)
        } catch (throwable: SecurityException) {
            Log.e(TAG, throwable.stackTraceToString())
            onSecurityException()
        }
        //... use new scan results ...
    }

    private fun onSecurityException() {
        Log.d(TAG, "onScanFailure")
        _scanState.value = ScanState.SecurityExceptionOnScan(::startScan)
    }
}