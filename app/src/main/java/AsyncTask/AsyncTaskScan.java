package AsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.rupture.jairsteve.rupture.ScanResultBroadcastReceiver;
import com.rupture.jairsteve.rupture.scan_result;

import controlador.controller_wlan;
import modelo.Wlan;

/**
 * Created by Usuario on 23/02/2015.
 */
public class AsyncTaskScan extends AsyncTask<String, String, String> {

    private Activity mainActivity;


    private WifiManager wifiManager;
    private Wlan wlan;
    private controller_wlan wlanDB;
    private IntentFilter intentFilter;
    boolean registedReceived = false;
    private ScanResultBroadcastReceiver scanResultBroadcastReceiver;

    public boolean isRegistedReceived() {
        return registedReceived;
    }

    public void setRegistedReceived(boolean registedReceived) {
        this.registedReceived = registedReceived;
    }

    public ScanResultBroadcastReceiver getScanResultBroadcastReceiver() {
        return scanResultBroadcastReceiver;
    }

    public void setScanResultBroadcastReceiver(ScanResultBroadcastReceiver scanResultBroadcastReceiver) {
        this.scanResultBroadcastReceiver = scanResultBroadcastReceiver;
    }

    public AsyncTaskScan(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        wlanDB = new controller_wlan(mainActivity);


        wifiManager = (WifiManager) mainActivity.getSystemService(mainActivity.WIFI_SERVICE);

        if(wifiManager.getWifiState() == wifiManager.WIFI_STATE_ENABLED){

            intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

            Log.d("REGISTER RECEIVER", "REGISTER RECEIVER");
            scanResultBroadcastReceiver = new ScanResultBroadcastReceiver(mainActivity, wifiManager);
            setRegistedReceived(true);
            mainActivity.registerReceiver(this.scanResultBroadcastReceiver, intentFilter);
            setScanResultBroadcastReceiver(scanResultBroadcastReceiver);

        }else{

            //El wifi está desactivado
            showDialog();
        }
        //start Wifi Scan
        wifiManager.startScan();
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
    }
    public void showDialog(){


        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainActivity.getApplicationContext(), "Active el wifi y Regrese", Toast.LENGTH_LONG).show();
            }
        });

    }

}
