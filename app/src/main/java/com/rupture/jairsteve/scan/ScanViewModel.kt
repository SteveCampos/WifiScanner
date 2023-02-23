package com.rupture.jairsteve.scan

import androidx.lifecycle.ViewModel
import com.rupture.jairsteve.scan.repository.WifiScannerAndroid
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(private val wifiScannerAndroid: WifiScannerAndroid) :
    ViewModel() {

    val scanState = wifiScannerAndroid.scanState

    init {
        wifiScannerAndroid.startScan()
    }


}