package com.rupture.jairsteve.scan.infrastructure.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rupture.jairsteve.scan.entity.MyScanResult

@Dao
interface MyScanResultDao {
    @Query("SELECT * FROM wlan")
    suspend fun getScannedItems(): List<MyScanResult>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrReplace(items: List<MyScanResult>)
}