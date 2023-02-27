package com.rupture.jairsteve.scan.infrastructure.repository.local

import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.repository.MyScanResultRepository

class MyScanRepositoryLocal(private val myScanResultDao: MyScanResultDao) : MyScanResultRepository {
    override suspend fun getMyScanResultList(): List<MyScanResult> =
        myScanResultDao.getScannedItems()

    override suspend fun saveOrUpdate(scannedItems: List<MyScanResult>) =
        myScanResultDao.addOrReplace(
            scannedItems
        )
}