package com.rupture.jairsteve.rupture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import controlador.controller_wlan;
import controlador.wlan_cursor_adapter;
import modelo.Constants;
import modelo.ScanDbHelper;
import modelo.Wlan;
//SWIPE REFRESH LAYOUT
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;


public class scan_result extends Activity {


    /*
    Declarar instancias globales
    */
    private RecyclerView recycler;
    private RecyclerView.LayoutManager lManager;

    private SwipeRefreshLayout refreshLayout;

    //Agregando adicionales
    private controller_wlan wlanDB;
    private Cursor cursor;
    private wlan_cursor_adapter wlanCursorAdapter ;
    private ListView lista;
    private ScanDbHelper scanDbHelper;
    private TextView textViewRedesDescubiertas;
    private Context context;
    private TextView textViewReload;


    private WifiManager wifiManager;
    Wlan wlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("INTENT", "CLASE SCAN RESULT INICIADA");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        context = this;


        lista = (ListView) findViewById(R.id.listViewScan);

        wlanDB = new controller_wlan(this);
        wlanDB.abrir();

        textViewRedesDescubiertas = (TextView) findViewById(R.id.textViewRedesDescubiertas);
        textViewReload = (TextView) findViewById(R.id.textViewReload);

        // Obtener el Recycler



        // Crear un nuestro cursor adapter

        cursor = wlanDB.readWlan();
        textViewRedesDescubiertas.setText(""+cursor.getCount());

        wlanCursorAdapter = new wlan_cursor_adapter(this, cursor, true);


        // Obtener el refreshLayout
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        // Seteamos los colores que se usarán a lo largo de la animación
        refreshLayout.setColorSchemeResources(
                R.color.Dark1,
                R.color.Dark5,
                R.color.Dark4,
                R.color.Dark2
        );

        // Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new Scanear().execute();
                    }
                }
        );



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

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onDestroy() {
        //wlanDB.cerrar();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        //wlanDB.abrir();
        super.onResume();
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
    class Scanear extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
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

            //TERMINÓ LA CARGA DE NUEVAS REDES
            cursor = null;
            cursor = wlanDB.readWlan();
            textViewRedesDescubiertas.setText(""+cursor.getCount());

            wlanCursorAdapter = new wlan_cursor_adapter(context, cursor, true);
            lista.setAdapter(wlanCursorAdapter);
            refreshLayout.setRefreshing(false);



        }
    }

}
