package com.rupture.jairsteve.rupture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

import AsyncTask.AsyncTaskScan;
import modelo.ScanDbHelper;
import modelo.Wlan;
import controlador.controller_wlan;


public class splash_screen extends Activity {


    private splash_screen mainAtivity;
    private AsyncTaskScan asyncTaskScan;

    public static final String fontPathMonserratBold = "fonts/Montserrat-Bold.ttf";

    private TextView textView_Splash_Screen_App_Name;
    private TextView textView_Splash_Screen_Developer_Name;

    public ScanDbHelper dbHelperScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        dbHelperScan = new ScanDbHelper(this);
        mainAtivity = this;



        //Cursor cursor = dbHelperScan.readVendor();



        Typeface typefaceMonserratBold = Typeface.createFromAsset(getAssets(), fontPathMonserratBold);
        //CAMBIAR ESTILO DE FUENTE A MONTSERRAT( se ve más cool)
        textView_Splash_Screen_App_Name = (TextView) findViewById(R.id.textView_Splash_Screen_App_Name);
        textView_Splash_Screen_App_Name.setTypeface(typefaceMonserratBold);
        textView_Splash_Screen_Developer_Name = (TextView) findViewById(R.id.textView_Splash_Screen_Developer_Name);
        textView_Splash_Screen_Developer_Name.setTypeface(typefaceMonserratBold);



        new OverrideDB().execute();

        Log.d("UI"," CREA LA UI");
        //wlanDB.deleteWlan();
/*
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if(wifiManager.getWifiState() == wifiManager.WIFI_STATE_ENABLED){

            //Register Wifi Scan Results Receiver
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    List<ScanResult> scanResultsList = wifiManager.getScanResults();
                    final int sizeListScan = scanResultsList.size();
                    Log.d("Wifi Scan count ...", "" + sizeListScan);

                    for(int i=0; i<sizeListScan; i++){
                        wlan = new Wlan();
                        Log.d("SSID",scanResultsList.get(i).SSID.toString());
                        wlan.setSsid(scanResultsList.get(i).SSID.toString());
                        Log.d("BSSID", scanResultsList.get(i).BSSID.toString());
                        wlan.setBssid(scanResultsList.get(i).BSSID.toString());
                        Log.d("CAPABILITIES", scanResultsList.get(i).capabilities.toString());
                        wlan.setCapabilities(scanResultsList.get(i).capabilities.toString());
                        Log.d("FREQUENCY", "" + scanResultsList.get(i).frequency);
                        wlan.setFrequency(scanResultsList.get(i).frequency);
                        Log.d("LEVEL", "" + scanResultsList.get(i).level);
                        wlan.setLevel(scanResultsList.get(i).level);
                        Log.d("TIMESTAMP", ""+scanResultsList.get(i).timestamp);
                        wlan.setTimestamp((int) scanResultsList.get(i).timestamp);


                        //INSERTO CADA REGISTRO EN LA TABLA WLAN (:
                        wlanDB.insertWlan(wlan);

                    }
                }
            }, intentFilter);
        }else{
            Log.d("WIFI", "SU WIFI ESTÁ DESACTIVADOS");
        }

        //start Wifi Scan
        wifiManager.startScan();

        Cursor cursor = wlanDB.readWlan();





        wlanDB = new controller_wlan(this);


        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        if(wifiManager.getWifiState() == wifiManager.WIFI_STATE_ENABLED){

            intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

            Log.d("REGISTER RECEIVER","REGISTER RECEIVER");
            scanResultBroadcastReceiver = new ScanResultBroadcastReceiver(this, wifiManager);
            registedReceived = true;
            registerReceiver(this.scanResultBroadcastReceiver, intentFilter);

        }else{

            //El wifi está desactivado
            showDialog();
        }
        //start Wifi Scan
        wifiManager.startScan();

        Log.d("INTENT", "INICIANDO LA CLASE SCAN RESULTS");
        Intent intent = new Intent(this, scan_result.class);
        startActivity(intent);
*/


    }

    public void showDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Wifi Desactivado")
                .setMessage("Active el wifi por favor")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelperScan.close();
        if (asyncTaskScan.isRegistedReceived()) {
            unregisterReceiver(asyncTaskScan.getScanResultBroadcastReceiver());
            Log.d("REGISTER RECEIVER","UNREGISTER RECEIVER");
        }else{
            Log.d("REGISTER RECEIVER", "NO UNREGISTER BECAUSE NOT REGISTER");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelperScan.close();

    }



    @Override
    protected void onResume() {
        super.onResume();
        try {
            dbHelperScan.sobreEscribirDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

class OverrideDB extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... strings) {
        try {
            dbHelperScan.sobreEscribirDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        asyncTaskScan = new AsyncTaskScan(mainAtivity);
        asyncTaskScan.execute();

    }
}


    /*
    public void ShowWlan(Cursor cursor){

        StringBuilder builder = new StringBuilder("Table vendor results : \n");
        while (cursor.moveToNext()){

            int id_wlan = cursor.getInt(0);
            String bssid = cursor.getString(1);
            String ssid = cursor.getString(2);
            String capabilities = cursor.getString(3);
            int frequency = cursor.getInt(4);
            int level = cursor.getInt(5);
            int timestamp = cursor.getInt(6);
            String id_vendor = cursor.getString(7);
            int wlan_type = cursor.getInt(8);




            builder.append(id_wlan).append(": ");
            builder.append(bssid).append("\n");
            builder.append(ssid).append("\n");
            builder.append(capabilities).append("\n");
            builder.append(frequency).append("\n");
            builder.append(level).append("\n");
            builder.append(timestamp).append("\n");
            builder.append(id_vendor).append("\n");
            builder.append(wlan_type).append("\n");
            builder.append("---------------------------------------").append("\n");


        }
        TextView textView = (TextView) findViewById(R.id.textView_id);
        textView.setText(builder);

    }*/
}
