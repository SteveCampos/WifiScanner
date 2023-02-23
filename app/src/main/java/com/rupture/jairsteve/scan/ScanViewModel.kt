package com.rupture.jairsteve.scan

import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.infrastructure.WifiScannerAndroid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val wifiScannerAndroid: WifiScannerAndroid) :
    ViewModel() {
    val scanState: Flow<ScanState<MyScanResult>> =
        wifiScannerAndroid.scanState
            .map { scanState ->
                val state: ScanState<MyScanResult> = when (scanState) {
                    is ScanState.PerformingScan<ScanResult> -> ScanState.PerformingScan<MyScanResult>()
                    is ScanState.StartScanFailed -> ScanState.StartScanFailed<MyScanResult>(
                        scanState.tryAgain
                    )
                    is ScanState.SecurityExceptionOnScan -> ScanState.SecurityExceptionOnScan<MyScanResult>(
                        scanState.tryAgain
                    )
                    is ScanState.SuccessScan<ScanResult> -> {
                        val items = scanState.items.map {
                            MyScanResult(it.BSSID, it.capabilities)
                        }
                        ScanState.SuccessScan<MyScanResult>(scanState.resultsUpdated, items)
                    }
                }
                return@map state
            }
}