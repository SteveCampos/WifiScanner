package com.rupture.jairsteve.rupture;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import AsyncTask.AsyncTaskScan;
import modelo.ScanDbHelper;
import modelo.Wlan;
import controlador.controller_wlan;

import com.afollestad.materialdialogs.MaterialDialog;
import  com.rey.material.widget.CheckBox;


public class splash_screen extends Activity {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 4650;
    private Activity mainAtivity;
    private AsyncTaskScan asyncTaskScan;

    public static final String fontPathMonserratBold = "fonts/Montserrat-Bold.ttf";

    SharedPreferences prefs;
    private TextView textView_Splash_Screen_App_Name;
    private TextView textView_Splash_Screen_Developer_Name;
    private ProgressBar progressBar;

    public ScanDbHelper dbHelperScan;

    private WifiManager wifiManager;
    Wlan wlan;
    controller_wlan wlanDB;


    //VARIABLES TÉRMINOS DE USO
    private CheckBox checkBoxTerms;
    private com.rey.material.widget.Button buttonInstall;
    private TextView textViewTermsUse;


    MaterialDialog.Builder builder = null;
    MaterialDialog dialog  =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mainAtivity = this;
        checkPermision();
    }

    private void init(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (!prefs.getBoolean("termsUseAceppted",false)){
            setContentView(R.layout.terms_use);
            checkBoxTerms = (CheckBox) findViewById(R.id.checkBoxTerms);
            buttonInstall = (com.rey.material.widget.Button) findViewById(R.id.buttonInstall);
            textViewTermsUse = (TextView) findViewById(R.id.textViewShowTermsUse);


            textViewTermsUse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTermsUse();
                }
            });
            buttonInstall.setEnabled(false);
            if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            }else{
                buttonInstall.setAlpha((float)0.8);
            }

            checkBoxTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    buttonInstall.setEnabled(isChecked);
                    if (isChecked){
                        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

                        }else{
                            buttonInstall.setAlpha(1);
                        }
                    }else {
                        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

                        }else{
                            buttonInstall.setAlpha((float)0.8);
                        }
                    }
                }
            });
            buttonInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TERMINOS DE USO","ACEPTADOS");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("termsUseAceppted", true);
                    editor.commit();
                    showSplashScreen();
                }
            });

        }else{
            showSplashScreen();
        }
    }

    private void checkPermision() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    init();

                } else {


                    TextView textDev = (TextView) findViewById(R.id.textView_Splash_Screen_Developer_Name) ;
                    textDev.setText(R.string.permission_message_needed);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showSplashScreen(){

        setContentView(R.layout.activity_splash_screen);


        Typeface typefaceMonserratBold = Typeface.createFromAsset(getAssets(), fontPathMonserratBold);
        //CAMBIAR ESTILO DE FUENTE A MONTSERRAT( se ve más cool)
        textView_Splash_Screen_App_Name = (TextView) findViewById(R.id.textView_Splash_Screen_App_Name);
        textView_Splash_Screen_App_Name.setTypeface(typefaceMonserratBold);
        textView_Splash_Screen_Developer_Name = (TextView) findViewById(R.id.textView_Splash_Screen_Developer_Name);
        textView_Splash_Screen_Developer_Name.setTypeface(typefaceMonserratBold);

        Log.d("UI"," CREA LA UI");

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
        if (dialog!=null){
            dialog.dismiss();
        }
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
                    if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        wlan.setTimestamp(0);
                    }else{
                        wlan.setTimestamp((int) scanResultsList.get(i).timestamp);
                    }
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
            wlanDB.cerrar();
            Intent startActivity = new Intent(mainAtivity, scan_result.class);
            mainAtivity.finish();
            //startActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainAtivity.startActivity(startActivity);
        }
    }

    public MaterialDialog showTermsUse(){
        builder = new MaterialDialog.Builder(this);

        builder
                .title("Términos de Uso")
                .content(R.string.terms_use)
                .positiveText("ACEPTO")
                .negativeText("NO, ACEPTO")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Log.d("TERMINOS DE USO","ACEPTADOS");
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("termsUseAceppted", true);
                        editor.commit();
                        showSplashScreen();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        mainAtivity.finish();
                        super.onNegative(dialog);

                    }
                });
        builder.autoDismiss(true);
        dialog = builder.show();
        return dialog;
    }

}
