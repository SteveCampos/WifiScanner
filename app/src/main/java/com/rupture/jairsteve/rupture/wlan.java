package com.rupture.jairsteve.rupture;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.SQLException;

import controlador.controller_wlan;
import modelo.Constants;
import modelo.Wlan;


public class wlan extends Activity {

    Wlan wlan;
    controller_wlan wlanDB;

    TextView textView_bssid;
    TextView textView_ssid;
    TextView textView_capabilities;
    TextView textView_frequency;
    ProgressBar progressBar_level;
    TextView textView_timestamp;
    TextView textView_current;
    TextView textViewFabricante;
    TextView textViewLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlan);

        wlanDB = new controller_wlan(this);
        long id = getIntent().getLongExtra("id", 1);
        wlan = wlanDB.getWlanForID(id);

        textView_ssid = (TextView) findViewById(R.id.textView_scan_result_ssid);
        progressBar_level = (ProgressBar) findViewById(R.id.progressBar_scan_result_signal);


        textViewFabricante = (TextView) findViewById(R.id.textViewFabricante);
        textView_bssid = (TextView) findViewById(R.id.textViewMac);
        textView_capabilities = (TextView) findViewById(R.id.textViewCapabilities);
        textView_frequency = (TextView) findViewById(R.id.textViewFrecuencia);
        textViewLevel = (TextView) findViewById(R.id.textViewLevel);
        textView_timestamp = (TextView) findViewById(R.id.textViewTimestamp);




        String id_vendor = wlan.getId_vendor();

        Cursor cursorVendor = wlanDB.getVendor(id_vendor);

        String fabricante = "FABRICANTE DESCONOCIDO";
        if (cursorVendor.getCount()>0){
            fabricante = cursorVendor.getString(cursorVendor.getColumnIndexOrThrow(Constants.TABLE_VENDOR_VENDOR_NAME));
        }


        textView_ssid.setText(wlan.getSsid());
        progressBar_level.setProgress(100 + wlan.getLevel());

        textViewFabricante.setText(""+fabricante);
        textView_bssid.setText(""+wlan.getBssid().toUpperCase());
        textView_capabilities.setText(""+wlan.getCapabilities());
        textView_frequency.setText(""+wlan.getFrequency() + " MHz");
        int levelConvert  = wlan.getLevel() + 100;
        textViewLevel.setText(""+levelConvert);
        textView_timestamp.setText(""+wlan.getTimestamp());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wlan, menu);
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
}
