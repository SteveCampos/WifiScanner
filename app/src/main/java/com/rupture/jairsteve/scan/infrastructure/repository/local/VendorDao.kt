package com.rupture.jairsteve.scan.infrastructure.repository.local

import androidx.room.Dao
import androidx.room.Query
import com.rupture.jairsteve.scan.repository.entity.Vendor

@Dao
interface VendorDao {
    @Query("SELECT * FROM mac_vendors WHERE mac_prefix LIKE '%' || :oui || '%' LIMIT 1")
    suspend fun findByOui(oui: String): Vendor?
}