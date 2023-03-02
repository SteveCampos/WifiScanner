package com.rupture.jairsteve.scan

import android.net.wifi.ScanResult
import android.util.Log
import androidx.lifecycle.ViewModel
import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.infrastructure.WifiScannerAndroid
import com.rupture.jairsteve.scan.repository.MyScanResultRepository
import com.rupture.jairsteve.scan.repository.VendorRepository
import com.rupture.jairsteve.scan.repository.entity.Vendor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    wifiScannerAndroid: WifiScannerAndroid,
    private val vendorRepository: VendorRepository,
    private val myScanResultRepository: MyScanResultRepository
) : ViewModel() {
    companion object {
        const val TAG = "ScanViewModel"
    }

    val scanState: Flow<ScanState<MyScanResult>> =
        wifiScannerAndroid.scanState.flowOn(Dispatchers.IO).map {
            //withContext(Dispatchers.Main) {
            mapScanResultToMyScanResult(it)
            //}
        }.onEach(::saveScannedItems)

    private suspend fun mapScanResultToMyScanResult(scanState: ScanState<ScanResult>): ScanState<MyScanResult> {
        Log.d(TAG, "mapScanResultToMyScanResult")

        val state: ScanState<MyScanResult> = when (scanState) {
            is ScanState.PerformingScan<ScanResult> -> ScanState.PerformingScan()
            is ScanState.StartScanFailed -> ScanState.StartScanFailed(
                scanState.tryAgain
            )
            is ScanState.SecurityExceptionOnScan -> ScanState.SecurityExceptionOnScan(
                scanState.tryAgain
            )
            is ScanState.SuccessScan<ScanResult> -> {
                Log.d(TAG, "SuccessScan ${scanState.items.size}")
                val items = scanState.items.map {

                    val bssid = it.BSSID
                    var oui = if (bssid.length > 8) bssid.substring(0, 8) else ""

                    var vendor: Vendor? = null

                    if (oui.isNotEmpty()) {
                        vendor = vendorRepository.getVendor(oui)
                        if (vendor == null) {
                            //Look for vendor on other table
                            val realOui = getRealOuiFromFalseOui(oui)
                            if (realOui != oui) {
                                oui = realOui
                                vendor = vendorRepository.getVendor(realOui)
                            }
                        }
                    }


                    MyScanResult(
                        bssid = it.BSSID,
                        ssid = it.wifiSsid.toString().replace("\"", ""),
                        capabilities = it.capabilities,
                        vendorMacPrefix = oui,
                        vendorName = vendor?.name
                    )
                }
                ScanState.SuccessScan(scanState.resultsUpdated, items)
            }
        }
        return state
    }

    private suspend fun saveScannedItems(state: ScanState<MyScanResult>) {
        Log.d(TAG, "saveScannedItems: $state")
        if (state is ScanState.SuccessScan) {
            val scannedItems = state.items
            myScanResultRepository.saveOrUpdate(scannedItems)
        }
    }


    private fun getRealOuiFromFalseOui(oui: String): String {
        return when (oui) {
            "F8:27:C5" -> "2C:26:C5"
            "F8:E1:CF" -> "34:E0:CF"
            "F8:03:8E" -> "DC:02:8E"
            "F8:3F:61" -> "F4:3E:61"
            "F8:61:80" -> "14:60:80"
            "F8:7F:35" -> "CC:7B:35"
            "F8:C3:46" -> "E4:C1:46"
            "F8:63:94" -> "D8:61:94"
            "F8:ED:80" -> "A0:EC:80"
            "7E:38:9F" -> "2C:AB:25"
            "3E:9E:03" -> "2C:AB:25"
            "48:B3:6C" -> "FC:8B:97"
            "BC:9E:38" -> "FC:8B:97"
            "68:79:A8" -> "FC:8B:97"
            else -> oui
        }
    }
}