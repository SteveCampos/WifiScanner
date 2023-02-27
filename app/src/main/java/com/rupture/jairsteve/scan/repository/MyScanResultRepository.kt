package com.rupture.jairsteve.scan.repository

import com.rupture.jairsteve.scan.entity.MyScanResult

interface MyScanResultRepository {
    suspend fun getMyScanResultList(): List<MyScanResult>
    suspend fun saveOrUpdate(scannedItems: List<MyScanResult>)
}
