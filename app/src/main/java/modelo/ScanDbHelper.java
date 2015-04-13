package modelo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

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

    //Ruta por defecto de las bases de datos en el sistema Android
    private static String DB_PATH = "/data/data/com.rupture.jairsteve.rupture/databases/";

    private static String DB_NAME = "scan.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;


    public ScanDbHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL(CREATE_STRUCTURE_DATABASE);
        Log.d("DB", " ON CREATE");
        /*try {
            sobreEscribirDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

        Log.d("DB", " ON UPGRADE");
        //sqLiteDatabase.execSQL(DROP_TABLE_WLAN);
        //sqLiteDatabase.execSQL(DELETE_TABLE_VENDOR);
        /*onCreate(sqLiteDatabase);*/

    }

    //CREAR UN MÉTODO QUE ELIMINE TODAS LAS TABLAS "DROP_TABLES()"
    /**
     * Crea una base de datos vacía en el sistema y la reescribe con nuestro fichero de base de datos.
     * */
    public void createDataBase() throws IOException {


        boolean dbExist = checkDataBase();
        Log.d("DB", "checkea si la DB EXISTE y es "+dbExist);

        if(dbExist){
//la base de datos existe y no hacemos nada.
            Log.d("DB", "LA BASE DE DATOS EXISTE Y NO HACEMOS NADA");
        }else{
//Llamando a este método se crea la base de datos vacía en la ruta por defecto del sistema
//de nuestra aplicación por lo que podremos sobreescribirla con nuestra base de datos.
            this.getReadableDatabase();

                try {


                    copyDataBase();
                    Log.d("DB", "COPIA LA BASE DE DATOS");

                } catch (IOException e) {
                    throw new Error("Error copiando Base de Datos");
            }
        }

    }

    /**
     * Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicación.
     * @return true si existe, false si no existe
     */
    private boolean checkDataBase(){


        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
        /*
        SQLiteDatabase checkDB = null;

        try{

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            Log.d("Exception OPEN", e.getMessage());
            //si llegamos aqui es porque la base de datos no existe todavía.

        }
        if(checkDB != null){

            checkDB.close();

        }
        return checkDB != null ? true : false;*/
    }

    /**
     * Copia nuestra base de datos desde la carpeta assets a la recién creada
     * base de datos en la carpeta de sistema, desde dónde podremos acceder a ella.
     * Esto se hace con bytestream.
     * */
    private void copyDataBase() throws IOException {

        Log.d("DB", "empieza a copiar la base de datos");

//Abrimos el fichero de base de datos como entrada
        InputStream myInput = myContext.getAssets().open(DB_NAME);

//Ruta a la base de datos vacía recién creada
        String outFileName = DB_PATH + DB_NAME;

//Abrimos la base de datos vacía como salida
        OutputStream myOutput = new FileOutputStream(outFileName);

//Transferimos los bytes desde el fichero de entrada al de salida
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

//Liberamos los streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.d("DB", "TERMINA DE COPIAR LA BASE DE DATOS");

    }

    public void sobreEscribirDB() throws SQLException {

//Abre la base de datos
        try {
            Log.d("DB", "trata de crear la db");
            createDataBase();
        } catch (IOException e) {
            throw new Error("Ha sido imposible crear la Base de Datos");
        }

        String myPath = DB_PATH + DB_NAME;
        //myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }


    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    public Cursor readVendor(){

        Cursor cursor;
        Log.d("DB", " READWLAN...");
        myDataBase= this.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Constants.TABLE_VENDOR_ID,
                Constants.TABLE_VENDOR_ID_VENDOR,
                Constants.TABLE_VENDOR_VENDOR_NAME,
        };

        String[] valuesWhere = {"5"};
        //OBTENIENDO LA CONSULTA

        cursor= myDataBase.query(
                Constants.TABLE_VENDOR,             // The table to query
                projection,                               // The columns to return
                "_id < ?",                                     // The columns for the WHERE clause
                valuesWhere,                                   // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                     // The sort order
        );

        //db.close();

        return cursor;
    }


}
