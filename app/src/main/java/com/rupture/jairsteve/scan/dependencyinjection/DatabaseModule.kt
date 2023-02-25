package com.rupture.jairsteve.scan.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.rupture.jairsteve.scan.infrastructure.repository.local.VendorDao
import com.rupture.jairsteve.scan.infrastructure.repository.local.VendorRepositoryLocal
import com.rupture.jairsteve.scan.infrastructure.repository.local.WifiScannerDatabase
import com.rupture.jairsteve.scan.repository.VendorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): WifiScannerDatabase =
        Room.databaseBuilder(appContext, WifiScannerDatabase::class.java, "WifiScannerDatabase.db")
            .createFromAsset("databases/mac-vendors-db.sqlite")
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideVendorDao(wifiScannerDatabase: WifiScannerDatabase): VendorDao =
        wifiScannerDatabase.vendorDao()


    @Singleton
    @Provides
    fun provideVendorRepository(vendorDao: VendorDao): VendorRepository =
        VendorRepositoryLocal(vendorDao)
}