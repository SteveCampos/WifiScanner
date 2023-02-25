package com.rupture.jairsteve.scan.entity

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase


data class MyScanResult(
    val bssid: String,
    val ssid: String,
    val capabilities: String,
    val vendorMacPrefix: String?,
    val vendorName: String?
) {

    fun canCalculateDefaultPassword() = getDefaultPassword() != null

    private fun isWlanType() =
        (ssid.toUpperCase(Locale.current).contains("WLAN")
                &&
                ssid.length == "WLAN_XXXX".length)

    fun getDefaultPassword(): String? {

        if (vendorMacPrefix == null) return null

        val isWlanType = isWlanType()
        if (!isWlanType) return null

        return try {

            val firstLetterVendor = vendorName?.first().toString().toUpperCase(Locale.current)
            val bssid4Pair = bssid.split(':')[3]
            val ssidLast4Digits = ssid.takeLast(4)

            firstLetterVendor + vendorMacPrefix + bssid4Pair + ssidLast4Digits
        } catch (t: Throwable) {
            null
        }
    }
}
