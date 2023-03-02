package com.rupture.jairsteve.scan

import com.rupture.jairsteve.scan.entity.MyScanResult

sealed class SavedNetworksState {
    object Loading : SavedNetworksState()
    data class Error(val tryAgain: ()-> Unit ) : SavedNetworksState()
    data class Success(val items: List<MyScanResult>) : SavedNetworksState()
}
