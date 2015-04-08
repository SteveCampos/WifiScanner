package com.rupture.jairsteve.rupture;

import android.app.Activity;
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

import java.sql.SQLException;

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
        context = this;

        wlanDB = new controller_wlan(this);
        wlanDB.abrir();

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


        lista = (ListView) findViewById(R.id.listViewScan);


        cursor = wlanDB.readWlan();
        textViewRedesDescubiertas.setText(""+cursor.getCount());

        wlanCursorAdapter = new wlan_cursor_adapter(this, cursor, true);
        lista.setAdapter(wlanCursorAdapter);


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                String ssid = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SSID));
//                Toast.makeText(getApplicationContext(),
//                        "" +ssid, Toast.LENGTH_LONG)
//                        .show();
                Intent intent = new Intent(context, wlan.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

        });


    }


    public void vincular() throws SQLException {
        cursor = wlanDB.readWlan();
        textViewRedesDescubiertas.setText(""+cursor.getCount());
        //startManagingCursor(cursor);
        wlanCursorAdapter = new wlan_cursor_adapter(this, cursor, true);
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
}
