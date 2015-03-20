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
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import AsyncTask.AsyncTaskScan;
import modelo.ScanDbHelper;
import modelo.Wlan;
import controlador.controller_wlan;


public class splash_screen extends Activity {


    private Activity mainAtivity;
    private AsyncTaskScan asyncTaskScan;

    public static final String fontPathMonserratBold = "fonts/Montserrat-Bold.ttf";

    private TextView textView_Splash_Screen_App_Name;
    private TextView textView_Splash_Screen_Developer_Name;
    private ProgressBar progressBar;

    public ScanDbHelper dbHelperScan;

    private WifiManager wifiManager;
    Wlan wlan;
    controller_wlan wlanDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mainAtivity = this;

        Typeface typefaceMonserratBold = Typeface.createFromAsset(getAssets(), fontPathMonserratBold);
        //CAMBIAR ESTILO DE FUENTE A MONTSERRAT( se ve más cool)
        textView_Splash_Screen_App_Name = (TextView) findViewById(R.id.textView_Splash_Screen_App_Name);
        textView_Splash_Screen_App_Name.setTypeface(typefaceMonserratBold);
        textView_Splash_Screen_Developer_Name = (TextView) findViewById(R.id.textView_Splash_Screen_Developer_Name);
        textView_Splash_Screen_Developer_Name.setTypeface(typefaceMonserratBold);

        Log.d("UI"," CREA LA UI");


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            Log.d("CODE","RUN FIRST TIME");
            new OverrideDB().execute();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }else{

            Log.d("CODE","IS NOT THE  FIRST TIME");
            new Scanear().execute();
        }


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
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

class OverrideDB extends AsyncTask<String, String, String>{

    @Override
    protected String doInBackground(String... strings) {
        try {
            Log.d("CODE DB","OVERRIDE EXECUTE");
            dbHelperScan = new ScanDbHelper(mainAtivity);
            dbHelperScan.sobreEscribirDB();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("CODE DB",e.getMessage());
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

        new Scanear().execute();



    }
}


    class Scanear extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {


            wlanDB = new controller_wlan(mainAtivity);
            wlanDB.abrir();

            wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

            if(wifiManager.getWifiState() == wifiManager.WIFI_STATE_ENABLED){

                //Register Wifi Scan Results Receiver
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

                /*
                registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {*/

                List<ScanResult> scanResultsList = wifiManager.getScanResults();
                List<String> listaIds = new ArrayList<String>();
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
                    wlan.setCurrent(0);

                    int id = wlanDB.getIdForBssid(scanResultsList.get(i).BSSID);
                    long rowId;
                    if (id==-1) {
                        Log.d("DB", "ESTA ES UNA NUEVA BSSID");
                        rowId = wlanDB.insertWlan(wlan);
                        Log.d("DB", "REGISTRO INSERTADO CON ID :" + rowId);
                        listaIds.add(""+rowId);

                    }else {
                        Log.d("DB","ESTA BSSID YA EXISTE SU ID ES : " + id);
                        Log.d("DB","SE MODIGICARÁ " + id);
                        listaIds.add(""+id);
                        int count =  wlanDB.updateWlan(wlan, id);
                        if (count==1){
                            Log.d("DB","REGISTRO MODIFICADO SATISFACTORIAMENTE" );
                        }
                    }

                }

                String[] ids = new String[listaIds.size()];
                listaIds.toArray(ids);

                for (int i = 0; i < ids.length; i++) {
                    Log.d("ID ACTUALIZADOS ", "" + ids[i]);
                }
                if (listaIds.size()>0){
                    wlanDB.changeCurrent(ids);
                }
                //  }
                //}, intentFilter);
            }else{
                Log.d("WIFI", "SU WIFI ESTÁ DESACTIVADOS");
            }

            //start Wifi Scan
            wifiManager.startScan();

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Intent startActivity = new Intent(mainAtivity, scan_result.class);
            mainAtivity.finish();
            //startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainAtivity.startActivity(startActivity);
        }
    }
}
