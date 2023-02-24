package com.rupture.jairsteve.scan.repository

import com.rupture.jairsteve.scan.repository.entity.Vendor

interface VendorRepository {
    suspend fun getVendor(oui: String): Vendor?
}