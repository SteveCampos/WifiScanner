package com.rupture.jairsteve.rupture;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

import AsyncTask.AsyncTaskScan;
import controlador.controller_wlan;
import controlador.wlan_cursor_adapter;
import modelo.Constants;
import modelo.ScanDbHelper;


public class scan_result extends Activity {



    //Agregando adicionales
    private controller_wlan wlanDB;
    private Cursor cursor;
    private wlan_cursor_adapter wlanCursorAdapter ;
    private ListView lista;
    private ScanDbHelper scanDbHelper;
    private TextView textViewRedesDescubiertas;
    private Context context;
    private TextView textViewReload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INTENT", "CLASE SCAN RESULT INICIADA");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        textViewRedesDescubiertas = (TextView) findViewById(R.id.textViewRedesDescubiertas);
        textViewReload = (TextView) findViewById(R.id.textViewReload);


        textViewReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),splash_screen.class);
                finish();
                startActivity(intent);
            }
        });


        context = this;
        //Agregando

        lista = (ListView) findViewById(R.id.listViewScan);
        wlanDB = new controller_wlan(this);


            wlanDB.abrir();

        try {
            vincular();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                String ssid = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SSID));
                Toast.makeText(getApplicationContext(),
                        "" +ssid, Toast.LENGTH_LONG)
                        .show();
                Intent intent = new Intent(context, wlan.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

        });

    }


    public void vincular() throws SQLException {
        cursor = wlanDB.readWlan();
        textViewRedesDescubiertas.setText(""+cursor.getCount());
        startManagingCursor(cursor);
        wlanCursorAdapter = new wlan_cursor_adapter(this, cursor);
        lista.setAdapter(wlanCursorAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan_result, menu);
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
/*
    public void showResults(Cursor cursor){

        Log.d("DB", "show all records ");

        StringBuilder builder = new StringBuilder("SCAN RESULTS : \n");

        int id_wlan = -1;
        String bssid = null;
        String ssid = null;
        String capabilities = null;
        int frequency = 0;
        int level = 0;
        int timestamp = 0;
        String id_vendor;
        int wlan_type = 0;


        while (cursor.moveToNext()){

            id_wlan = cursor.getInt(0);
            bssid = cursor.getString(1);
            ssid = cursor.getString(2);
            capabilities = cursor.getString(3);
            frequency = cursor.getInt(4);
            level = cursor.getInt(5);
            timestamp = cursor.getInt(6);
            id_vendor = cursor.getString(7);
            wlan_type = cursor.getInt(8);

            builder.append("id_wlan : "+id_wlan+"\n");
            builder.append("bssid : "+bssid+"\n");
            builder.append("ssid : "+ssid+"\n");
            builder.append("capabilities : " + capabilities+"\n");
            builder.append("frequency : " + frequency+"\n");
            builder.append("level : " + level+"\n");
            builder.append("timestamp : " + timestamp+"\n");
            builder.append("id_vendor : " + id_vendor+"\n");
            builder.append("wlan_type : " + wlan_type+"\n");
            builder.append("-------------------------------------\n");

        }

        cursor.close();
        TextView textView = (TextView) findViewById(R.id.textView_show);
        textView.setText(builder);

    }*/
}
