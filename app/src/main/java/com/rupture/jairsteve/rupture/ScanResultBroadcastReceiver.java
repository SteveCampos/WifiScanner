package com.rupture.jairsteve.rupture;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

import controlador.controller_wlan;
import modelo.Wlan;

/**
 * Created by JairSteve on 29/11/2014.
 */
public class ScanResultBroadcastReceiver extends BroadcastReceiver {
    WifiManager wifiManager;
    Wlan wlan;
    controller_wlan wlanDB;

      public ScanResultBroadcastReceiver(Context context, WifiManager wifiManager){
        this.wifiManager = wifiManager;
        wlanDB = new controller_wlan(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        List<ScanResult> scanResultsList = wifiManager.getScanResults();
        final int sizeListScan = scanResultsList.size();
        for(int i=0; i<sizeListScan; i++){
            wlan = new Wlan();

            Log.d("SSID", scanResultsList.get(i).SSID);
            Log.d("BSSID", scanResultsList.get(i).BSSID);

            wlan.setBssid(scanResultsList.get(i).BSSID);
            wlan.setSsid(scanResultsList.get(i).SSID);
            wlan.setCapabilities(scanResultsList.get(i).capabilities);
            wlan.setFrequency(scanResultsList.get(i).frequency);
            wlan.setLevel(scanResultsList.get(i).level);
            wlan.setTimestamp((int) scanResultsList.get(i).timestamp);
            wlan.setCurrent(1);

            int id = wlanDB.getIdForBssid(scanResultsList.get(i).BSSID);
            long rowId;
            if (id==-1) {
                Log.d("DB", "ESTA ES UNA NUEVA BSSID");
                rowId = wlanDB.insertWlan(wlan);
                Log.d("DB", "REGISTRO INSERTADO CON ID :" + rowId);

            }else {
                Log.d("DB","ESTA BSSID YA EXISTE SU ID ES : " + id);
                Log.d("DB","SE MODIGICARÁ " + id);
                int count =  wlanDB.updateWlan(id);
                if (count==1){
                    Log.d("DB","REGISTRO MODIFICADO SATISFACTORIAMENTE" );
                }
            }

        }

        Intent startActivity = new Intent(context, scan_result.class);
        //startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startActivity);
    }
}
