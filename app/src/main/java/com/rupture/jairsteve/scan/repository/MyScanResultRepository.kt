package com.rupture.jairsteve.scan.repository

import com.rupture.jairsteve.scan.entity.MyScanResult
import kotlinx.coroutines.flow.Flow

interface MyScanResultRepository {
    fun getMyScanResultList(): Flow<List<MyScanResult>>
    suspend fun saveOrUpdate(scannedItems: List<MyScanResult>)
}
