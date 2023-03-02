package com.rupture.jairsteve.scan

import com.rupture.jairsteve.scan.entity.MyScanResult

data class ScanScreenState(val scanState: ScanState<MyScanResult>, val myScanResultState: SavedNetworksState)