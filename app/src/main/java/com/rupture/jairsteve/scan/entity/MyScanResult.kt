package com.rupture.jairsteve.scan.entity

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "wlan")
data class MyScanResult(
    @PrimaryKey val bssid: String,
    val ssid: String,
    val capabilities: String,
    val vendorMacPrefix: String,
    val vendorName: String?
) {

    //fun getVendorMacPrefix() = if (bssid.length > 8) bssid.substring(0, 8) else ""

    fun canCalculateDefaultPassword() = getDefaultPassword() != null

    private fun isWlanType() =
        (ssid.toUpperCase(Locale.current).contains("WLAN")
                &&
                ssid.length == "WLAN_XXXX".length)

    fun getDefaultPassword(): String? {


        val isWlanType = isWlanType()
        if (!isWlanType) return null

        return try {

            val cleanedOui = vendorMacPrefix.replace(":", "")
            val firstLetterVendor = vendorName?.first().toString().toUpperCase(Locale.current)
            val bssid4Pair = bssid.split(':')[3]
            val ssidLast4Digits = ssid.takeLast(4)

            firstLetterVendor + cleanedOui + bssid4Pair + ssidLast4Digits
        } catch (t: Throwable) {
            null
        }
    }
}
