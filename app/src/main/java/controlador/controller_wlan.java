package controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

import modelo.Constants;
import modelo.ScanDbHelper;
import modelo.Wlan;

/**
 * Created by JairSteve on 14/11/2014.
 */
public class controller_wlan {


    ScanDbHelper scanDBHelper;
    Cursor cursor;
    controller_wlan_metods metods;
    Context mconContext = null;
    //Agregando
    private SQLiteDatabase db;
    private Context contexto;
    //Fin
    long rowId ;
    int id = -1 ;

    Wlan wlan = null;
    public controller_wlan(Context context) {
        mconContext = context;
        scanDBHelper = new ScanDbHelper(mconContext);
        metods = new controller_wlan_metods();
    }

    //Agregando adicional
    public controller_wlan abrir() throws SQLException
    {
        db = scanDBHelper.getWritableDatabase();
        return this;
    }

    public void cerrar(){
        scanDBHelper.close();
    }

    //Fin

    public long insertWlan(Wlan wlan){

        Log.d("DB", "OBTENIENDO LOS DATOS DE LA TABLA WLAN");
        String bssid = wlan.getBssid();
        Log.d("INSERT BSSID", bssid);
        String ssid = wlan.getSsid();
        Log.d("INSERT SSID", ssid);
        String capabilities = wlan.getCapabilities();
        Log.d("INSERT CAPABILITIES", capabilities);
        int frequency = wlan.getFrequency();
        int level = wlan.getLevel();
        int timestamp = wlan.getTimestamp();
        int current = wlan.getCurrent();

        String id_vendor = metods.obtain_idVendor(bssid);
        int wlan_type = metods.obtain_tipoWlan(ssid);

        String splitBssid = metods.obtain_splitBssid(bssid);

        //String name_vendor = vendor.getVendorForID(id_vendor);
        String twoMiddleDigits = metods.obtain_twoMiddleDitigs(splitBssid);
        String fourLastDigits = metods.obtain_ssidLastFourDigits(ssid);

        //OBTENGO LA BASE DE DATOS EN MODO ESCRIBIBLE
        Log.d("DB", "OBTENIENDO DB EN MODO ESCRIBIBLE...");
        SQLiteDatabase db = scanDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.BSSID, bssid); // BSSID (MAC)
        values.put(Constants.SSID, ssid); // SSID (WLAN NAME)
        values.put(Constants.CAPABILITIES, capabilities); // CAPABILITIES (PROTECTION)
        values.put(Constants.FREQUENCY, frequency); // FREQUENCY (2.4Ghz)
        values.put(Constants.LEVEL, level); // LEVEL (SIGNAL 0 -100)
        values.put(Constants.TIMESTAMP, timestamp); // EL TIEMPO EN QUE SE MANDAN LOS PAQUETES CREOO XD
        values.put(Constants.CURRENT, current); // 1 SI EL REGISTRO ES DEL SCANEO ACTUAL Y 0 SI ES DE UN SCANEO PASADO

        values.put(Constants.ID_VENDOR, id_vendor); // ID_VENDOR (PRIMEROS 6 DÍGITOS DE LA MAC)
        values.put(Constants.WLAN_TYPE, wlan_type);



        String columnsNull[] = {Constants.PASSWORD};
        /*
        if(name_vendor!=null) {
            String firstLetter = ""+name_vendor.charAt(0);
            String password = firstLetter + id_vendor + twoMiddleDigits + fourLastDigits;
            values.put(Constants.red_wlan._COLUMN_NAME_PASSWORD, password ); // VENDOR FIRST LETTER + (ID_VENDOR)MAC REAL 6 DIGITS + 2 DIGITS NEXT + 4 DIGITS TO WLAN
        }
        */

        // Inserting Row
        Log.d("INSERTANDO","insertando registro...");

        rowId = db.insert(Constants.TABLE_NAME, Constants.PASSWORD, values);

        Log.d("INSERTANDO","REGISTRO INSERTADO..");

        db.close(); // Closing database connection

        Log.d("DB","base de datos cerrada despupes de insertar registro");
        return rowId;
    }


    public Cursor readWlan(){

        Log.d("DB", " READWLAN...");
        SQLiteDatabase db = scanDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Constants.ID_WLAN,
                Constants.BSSID,
                Constants.SSID,
                Constants.CAPABILITIES,
                Constants.FREQUENCY,
                Constants.LEVEL,
                Constants.TIMESTAMP,
                Constants.ID_VENDOR,
                Constants.WLAN_TYPE,
                Constants.CURRENT,
        };

        //OBTENIENDO LA CONSULTA

        cursor= db.query(
                Constants.TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );

        //db.close();

        return cursor;
    }

    public void deleteWlan(){

        SQLiteDatabase db = scanDBHelper.getReadableDatabase();
        //db.execSQL("DELETE FROM wlan");
        db.delete(Constants.TABLE_NAME, null, null);
    }

    public int getIdForBssid(String bssid){

        SQLiteDatabase db = scanDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Constants.ID_WLAN,
                Constants.BSSID,
             };

        String[] valuesWhere = {bssid};
        //OBTENIENDO LA CONSULTA

        try {


        cursor= db.query(
                Constants.TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                Constants.BSSID + " LIKE ?",                                     // The columns for the WHERE clause
                valuesWhere,                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );


        if (cursor!=null) {
            Log.d("BSSID", "ESTE BSSID YA EXISTE :");
//            Log.d("BSSID", "es " + cursor.getString(1));
            cursor.moveToFirst();
            id = cursor.getInt(0);

        }else{
            Log.d("BSSID", "EL NOMBRE DE LA MAC ES NUEVA XD");
            id=-1;
        }

        }catch (Exception e){
            Log.d("Exception : ", ""+e);
        }
        db.close();
        return id;
    }

    public Wlan getWlanForID(long id){


        SQLiteDatabase db = scanDBHelper.getReadableDatabase();
        wlan = new Wlan();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Constants.ID_WLAN,
                Constants.BSSID,
                Constants.SSID,
                Constants.CAPABILITIES,
                Constants.FREQUENCY,
                Constants.LEVEL,
                Constants.TIMESTAMP,
                Constants.ID_VENDOR,
                Constants.WLAN_TYPE,
                Constants.CURRENT,
        };

        String[] valuesWhere = {""+id};
        //OBTENIENDO LA CONSULTA

        try {


            cursor= db.query(
                    Constants.TABLE_NAME,             // The table to query
                    projection,                               // The columns to return
                    Constants.ID_WLAN + " LIKE ?",                                     // The columns for the WHERE clause
                    valuesWhere,                                   // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                     // The sort order
            );


            if (cursor!=null) {
                cursor.moveToFirst();
                wlan.setId_wlan(cursor.getInt(0));
                wlan.setBssid(cursor.getString(1));
                wlan.setSsid(cursor.getString(2));
                wlan.setCapabilities(cursor.getString(3));
                wlan.setFrequency(cursor.getInt(4));
                wlan.setLevel(cursor.getInt(5));
                wlan.setTimestamp(cursor.getInt(6));
                wlan.setId_vendor(cursor.getString(7));
                wlan.setWlan_type(cursor.getInt(8));
                wlan.setCurrent(cursor.getInt(9));
            }else{
                Log.d("ERROR", "NO SE PUDO OBTENER LA WLAN POR EL ID wlan vacía" );

            }

        }catch (Exception e){
            Log.d("Exception : ", ""+e);
        }
        db.close();
        return wlan;
    }


    public int updateWlan(int id){

        SQLiteDatabase db = scanDBHelper.getReadableDatabase();

        String[] projection = {
                Constants.ID_WLAN,
                Constants.BSSID,
                Constants.SSID,
                Constants.CAPABILITIES,
                Constants.FREQUENCY,
                Constants.LEVEL,
                Constants.TIMESTAMP,
                Constants.ID_VENDOR,
                Constants.WLAN_TYPE,
                Constants.CURRENT
        };

        String[] valuesWhere = {String.valueOf(id)};
        //OBTENIENDO LA CONSULTA

        cursor= db.query(
                Constants.TABLE_NAME,             // The table to query
                projection,                               // The columns to return
                Constants.ID_WLAN + " LIKE ?",                                     // The columns for the WHERE clause
                valuesWhere,                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );



        String bssid;
        String ssid = null;
        String capabilities = null;
        int frequency = 0;
        int level = 0;
        int timestamp = 0;
        String id_vendor;
        int wlan_type = 0;
        int current = 0;

        if (cursor.moveToFirst()) {
            bssid = cursor.getString(1);
            ssid = cursor.getString(2);
            capabilities = cursor.getString(3);
            frequency = cursor.getInt(4);
            level = cursor.getInt(5);
            timestamp = cursor.getInt(6);
            id_vendor = cursor.getString(7);
            wlan_type = cursor.getInt(8);
            current = cursor.getInt(9);
        }else{
            Log.d("DB", "NO SE HA PODIDO OBTENER EL OBJETO WLAN POR EL ID_WLAN");
        }


// New value for one column
        ContentValues values = new ContentValues();
        values.put(Constants.SSID, ssid); // SSID (WLAN NAME)
        values.put(Constants.CAPABILITIES, capabilities); // CAPABILITIES (PROTECTION)
        values.put(Constants.FREQUENCY, frequency); // FREQUENCY (2.4Ghz)
        values.put(Constants.LEVEL, level); // LEVEL (SIGNAL 0 -100)
        values.put(Constants.TIMESTAMP, timestamp); // EL TIEMPO EN QUE SE MANDAN LOS PAQUETES CREOO XD
        values.put(Constants.WLAN_TYPE, wlan_type); // EL TIEMPO EN QUE SE MANDAN LOS PAQUETES CREOO XD
        values.put(Constants.CURRENT, current);


// Which row to update, based on the ID
        String selection = Constants.ID_WLAN + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                Constants.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

}
