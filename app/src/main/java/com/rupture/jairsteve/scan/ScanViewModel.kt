package com.rupture.jairsteve.scan

import android.net.wifi.ScanResult
import androidx.lifecycle.ViewModel
import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.infrastructure.WifiScannerAndroid
import com.rupture.jairsteve.scan.repository.VendorRepository
import com.rupture.jairsteve.scan.repository.entity.Vendor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    wifiScannerAndroid: WifiScannerAndroid,
    private val vendorRepository: VendorRepository
) :
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


                            val bssid = it.BSSID
                            val oui: String =
                                if (bssid.length > 8) bssid.substring(0, 8) else ""

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


                            MyScanResult(it.SSID, it.capabilities, vendor?.name)
                        }
                        ScanState.SuccessScan<MyScanResult>(scanState.resultsUpdated, items)
                    }
                }
                return@map state
            }

    private fun getRealOuiFromFalseOui(oui: String): String = oui
}