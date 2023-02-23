package com.rupture.jairsteve.scan.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import com.rupture.jairsteve.scan.ScanState
import com.rupture.jairsteve.scan.ScanViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class WifiScannerAndroid(private val wifiManager: WifiManager, private val context: Context) {

    companion object {
        const val TAG = "ScanResultImpl"
    }


    private val _scanState: MutableStateFlow<ScanState> = MutableStateFlow(ScanState.PerformingScan)
    val scanState get() = _scanState

    private val wifiScanReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            Log.d(TAG, "wifiScanReceiver onReceive: $success")
            getScannedResults(success)
        }
    }

    fun startScan() {
        Log.d(TAG, "startScan")

        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)

        context.registerReceiver(
            wifiScanReceiver, intentFilter
        )

        val startScanSuccess = wifiManager.startScan()

        // scan failure handling
        if (!startScanSuccess) onStartScanFailure()

    }

    private fun onStartScanFailure() {
        _scanState.value = ScanState.StartScanFailed(::startScan)
    }
    
    private fun getScannedResults(resultsUpdated: Boolean) {
        Log.d(TAG, "scanSuccess")
        try {
            val results = wifiManager.scanResults
            _scanState.value = ScanState.SuccessScan(resultsUpdated, results)
        } catch (securityException: SecurityException) {
            onScanFailure()
        }
        //... use new scan results ...
    }

    private fun onScanFailure() {
        _scanState.value = ScanState.SecurityExceptionOnScan(::startScan)
    }
}