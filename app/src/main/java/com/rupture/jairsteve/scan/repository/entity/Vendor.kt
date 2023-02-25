package com.rupture.jairsteve.scan.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mac_vendors")
data class Vendor(
    @PrimaryKey @ColumnInfo(name = "mac_prefix") val macPrefix: String,
    @ColumnInfo(name = "vendor_name") val name: String?,
    @ColumnInfo(name = "block_type") val blockType: String?,
    @ColumnInfo(name = "last_update") val lastUpdateDate: String?
)
