package modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static modelo.Constants.DATABASE_NAME;
import static modelo.Constants.DATABASE_VERSION;
import static modelo.Constants.CREATE_STRUCTURE_DATABASE;

import static modelo.Constants.DROP_TABLE_WLAN;
import static modelo.Constants.DROP_TABLE_VENDOR;

//import static modelo.Constants.DELETE_TABLE_VENDOR;


/**
 * Created by JairSteve on 14/11/2014.
 */

public class ScanDbHelper extends SQLiteOpenHelper {


    public ScanDbHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_STRUCTURE_DATABASE);
        Log.d("DB", "DB CREADA");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        sqLiteDatabase.execSQL(DROP_TABLE_WLAN);
        sqLiteDatabase.execSQL(DROP_TABLE_VENDOR);
        //sqLiteDatabase.execSQL(DELETE_TABLE_VENDOR);
        onCreate(sqLiteDatabase);
    }

    //CREAR UN MÉTODO QUE ELIMINE TODAS LAS TABLAS "DROP_TABLES()"




}
