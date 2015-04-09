package com.rupture.jairsteve.rupture;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;


import controlador.controller_wlan;
import controlador.controller_wlan_metods;
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
    String password;
    private Activity mainActivity;

    //DIALOG MATERIAL
    private Context context;
    MaterialDialog.Builder builder = null;
    MaterialDialog dialog  =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LIFECYCLE","ON CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlan);

        mainActivity = this;
        context = this;
        wlanDB = new controller_wlan(this);
        wlanDB.abrir();

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
        Cursor cursorRealVendor = wlanDB.getRealVendor(id_vendor);


        String fabricante = "FABRICANTE DESCONOCIDO";
        if (cursorVendor.getCount()>0){
            fabricante = cursorVendor.getString(cursorVendor.getColumnIndexOrThrow(Constants.TABLE_VENDOR_VENDOR_NAME));
        }else if (cursorRealVendor.getCount()>0){
            fabricante = cursorRealVendor.getString(cursorRealVendor.getColumnIndexOrThrow(Constants.TABLE_VENDOR_VENDOR_NAME));
            id_vendor = cursorRealVendor.getString(cursorRealVendor.getColumnIndexOrThrow("real"));
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

        if(wlan.getWlan_type()==1){
            Log.d("YEAH", "ES UNA WLAN_XXXX");
            if(!fabricante.equals("FABRICANTE DESCONOCIDO")){
                Log.d("FUCK YEAH", "TENEMOS LA CONTRASEÑA");

                controller_wlan_metods c = new controller_wlan_metods();
                password = c.getPassword(wlan.getSsid(),id_vendor, wlan.getBssid(), fabricante).toUpperCase();
                Log.d("FUCKING", password);
                //dialogFuckYeah().show();

               showCustomDialog();

            }
        }


    }

    public MaterialDialog showCustomDialog(){
        builder = new MaterialDialog.Builder(this);
        /*final View layout = View.inflate(this, R.layout.layout_dialog_custom, null);
        TextView textView = (TextView)layout.findViewById(R.id.textViewPassword);
        textView.setText(""+password);*/


                builder
                .title("" + wlan.getSsid())
                .content("" + password)
//                .customView(layout,true)
                .positiveText("COPY")
                .negativeText("DISCARD")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(mainActivity.CLIPBOARD_SERVICE);
                            clipboard.setText(password.toUpperCase());
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(mainActivity.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", password.toUpperCase());
                            clipboard.setPrimaryClip(clip);
                        }
                        Toast.makeText(mainActivity.getApplicationContext(), "Copiado al portapapeles", Toast.LENGTH_LONG).show();
                        mainActivity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        super.onPositive(dialog);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }

                });

        dialog = builder.show();
        return dialog;
    }

    @Override
    protected void onPause() {
        Log.d("LIFECYCLE", "ON PAUSE");
        super.onPause();
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        Log.d("LIFECYCLE","ON RESUMEN");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d("LIFECYCLE","ON STOP");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("LIFECYCLE","ON DESTROY");
        super.onDestroy();
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
    /*
    private Dialog dialogFuckYeah() {
        final View layout = View.inflate(this, R.layout.fuck_yeah, null);
        final TextView textViewPassword = ((TextView) layout.findViewById(R.id.textViewPassword));
        final TextView textViewBitches = ((TextView) layout.findViewById(R.id.idFuck));


        textViewPassword.setText(""+password.toUpperCase());
        textViewBitches.setText("¡Fuck Yeah\nBitches!");
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(mainActivity.CLIPBOARD_SERVICE);
                    clipboard.setText(password.toUpperCase());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(mainActivity.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", password.toUpperCase());
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(mainActivity.getApplicationContext(), "Copiado al portapapeles", Toast.LENGTH_LONG).show();
                mainActivity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Fuck Yeah!");
        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        //alertDialog.setInverseBackgroundForced(true);
        //alertDialog.setIcon(mainActivity.getResources().getDrawable(R.drawable.ic_launcher));
        //alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //alertDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        return alertDialog;
    }
    */
}
