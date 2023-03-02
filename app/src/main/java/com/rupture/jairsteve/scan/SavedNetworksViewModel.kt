package com.rupture.jairsteve.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.repository.MyScanResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SavedNetworksViewModel @Inject constructor(
    private val myScanResultRepository: MyScanResultRepository
) : ViewModel() {


    init {
        collectSavedNetworks()
    }

    private val _savedNetworkState: MutableStateFlow<SavedNetworksState> =
        MutableStateFlow(SavedNetworksState.Loading)
    val savedNetworksState get() = _savedNetworkState

    private fun collectSavedNetworks() {
        viewModelScope.launch(Dispatchers.IO) {
            myScanResultRepository
                .getMyScanResultList()
                .flowOn(Dispatchers.IO).catch {
                    onGetSavedNetworksFailed(it)
                }.collect(::onGetSavedNetworks)

        }
    }

    private fun onGetSavedNetworksFailed(throwable: Throwable) {
        _savedNetworkState.value = SavedNetworksState.Error(::collectSavedNetworks)
    }

    private fun onGetSavedNetworks(items: List<MyScanResult>) {
        _savedNetworkState.value = SavedNetworksState.Success(items)
    }

}