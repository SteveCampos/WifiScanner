package com.rupture.jairsteve.scan.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendor")
data class Vendor(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") val identifierCounter: Int?,
    @ColumnInfo(name = "id_vendor") val id: String,
    @ColumnInfo(name = "vendor_name") val name: String
)
