package com.rupture.jairsteve.scan.infrastructure.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rupture.jairsteve.scan.entity.MyScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface MyScanResultDao {
    @Query("SELECT * FROM wlan")
    fun getScannedItems(): Flow<List<MyScanResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrReplace(items: List<MyScanResult>)
}