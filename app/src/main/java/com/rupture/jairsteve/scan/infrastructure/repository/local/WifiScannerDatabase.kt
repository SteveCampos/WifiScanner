package com.rupture.jairsteve.scan.infrastructure.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rupture.jairsteve.scan.entity.MyScanResult
import com.rupture.jairsteve.scan.repository.entity.Vendor

@Database(entities = [Vendor::class, MyScanResult::class], exportSchema = false, version = 8)
abstract class WifiScannerDatabase : RoomDatabase() {
    abstract fun vendorDao(): VendorDao
    abstract fun myScanResultDao(): MyScanResultDao
}