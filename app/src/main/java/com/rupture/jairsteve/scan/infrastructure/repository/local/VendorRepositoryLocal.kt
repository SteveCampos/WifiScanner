package com.rupture.jairsteve.scan.infrastructure.repository.local

import com.rupture.jairsteve.scan.repository.VendorRepository
import com.rupture.jairsteve.scan.repository.entity.Vendor

class VendorRepositoryLocal(private val vendorDao: VendorDao) : VendorRepository {
    override suspend fun getVendor(oui: String): Vendor? = vendorDao.findByOui(oui)
}