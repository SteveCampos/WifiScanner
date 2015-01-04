package com.rupture.jairsteve.rupture;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import controlador.controller_wlan;
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
    TextView textView_id_vendor;
    TextView textView_wlanType;
    TextView textView_current;
    TextView textView_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlan);

        wlanDB = new controller_wlan(this);
        long id = getIntent().getLongExtra("id", 1);
        wlan = wlanDB.getWlanForID(id);

        textView_ssid = (TextView) findViewById(R.id.textView_scan_result_ssid);
        progressBar_level = (ProgressBar) findViewById(R.id.progressBar_scan_result_signal);
        textView_password = (TextView) findViewById(R.id.textView_scan_result_password);
        textView_id_vendor = (TextView) findViewById(R.id.textView_scan_result_vendor);
        textView_capabilities = (TextView) findViewById(R.id.textView_scan_result_capabilities);


        textView_ssid.setText(wlan.getSsid());
        progressBar_level.setProgress(100+wlan.getLevel());
        textView_password.setText(wlan.getBssid());
        textView_id_vendor.setText(wlan.getId_vendor());
        textView_capabilities.setText(wlan.getCapabilities());

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
