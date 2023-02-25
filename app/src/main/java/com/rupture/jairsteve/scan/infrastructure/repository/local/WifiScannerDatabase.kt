package com.rupture.jairsteve.scan.infrastructure.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rupture.jairsteve.scan.repository.entity.Vendor

@Database(entities = [Vendor::class], exportSchema = false, version = 7)
abstract class WifiScannerDatabase : RoomDatabase() {
    abstract fun vendorDao(): VendorDao
}