package com.rupture.jairsteve.scan

import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.infrastructure.WifiScannerAndroid
import com.rupture.jairsteve.scan.repository.MyScanResultRepository
import com.rupture.jairsteve.scan.repository.VendorRepository
import com.rupture.jairsteve.scan.repository.entity.Vendor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    wifiScannerAndroid: WifiScannerAndroid,
    private val vendorRepository: VendorRepository,
    private val myScanResultRepository: MyScanResultRepository
) : ViewModel() {

    val scanState: Flow<ScanState<MyScanResult>> =
        wifiScannerAndroid
            .scanState
            .flowOn(Dispatchers.IO)
            .map(::mapScanResultToMyScanResult)
            .onEach(::saveScannedItems)

    private suspend fun mapScanResultToMyScanResult(scanState: ScanState<ScanResult>): ScanState<MyScanResult> {
        val state: ScanState<MyScanResult> = when (scanState) {
            is ScanState.PerformingScan<ScanResult> -> ScanState.PerformingScan()
            is ScanState.StartScanFailed -> ScanState.StartScanFailed(
                scanState.tryAgain
            )
            is ScanState.SecurityExceptionOnScan -> ScanState.SecurityExceptionOnScan(
                scanState.tryAgain
            )
            is ScanState.SuccessScan<ScanResult> -> {
                val items = scanState.items.map {

                    val bssid = it.BSSID
                    val oui = if (bssid.length > 8) bssid.substring(0, 8) else ""

                    var vendor: Vendor? = null

                    if (oui.isNotEmpty()) {
                        vendor = vendorRepository.getVendor(oui)
                        if (vendor == null) {
                            //Look for vendor on other table
                            val realOui = getRealOuiFromFalseOui(oui)
                            if (realOui != oui) {
                                vendor = vendorRepository.getVendor(realOui)
                            }
                        }
                    }


                    MyScanResult(
                        bssid = it.BSSID,
                        ssid = it.wifiSsid.toString().replace("\"", ""),
                        capabilities = it.capabilities,
                        vendorName = vendor?.name
                    )
                }
                ScanState.SuccessScan(scanState.resultsUpdated, items)
            }
        }
        return state
    }

    private suspend fun saveScannedItems(state: ScanState<MyScanResult>) {
        if (state is ScanState.SuccessScan) {
            val scannedItems = state.items
            myScanResultRepository.saveOrUpdate(scannedItems)
        }
    }


    private fun getRealOuiFromFalseOui(oui: String): String = oui
}