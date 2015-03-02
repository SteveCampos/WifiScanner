package modelo;

/**
 * Created by JairSteve on 14/11/2014.
 */
public interface Constants {

    public static final String DATABASE_NAME = "scan.db";
    public static final int DATABASE_VERSION = 1;


        public static final String CREATE_STRUCTURE_DATABASE = "CREATE TABLE wlan(_id  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, bssid    TEXT NOT NULL UNIQUE, id_vendor    TEXT NOT NULL, ssid TEXT NOT NULL, capabilities TEXT NOT NULL, frequency INTEGER NOT NULL, level INTEGER NOT NULL, timestamp    INTEGER NOT NULL, wlan_type INTEGER NOT NULL, password TEXT, current INTEGER NOT NULL)";

    //public static final String DELETE_TABLE_WLAN = "DROP TABLE IF EXISTS " + wlan.TABLE_NAME + ";";
    //public static final String DELETE_TABLE_VENDOR = "DROP TABLE IF EXISTS " + vendor.TABLE_NAME + ";";



    public static final String TABLE_NAME = "wlan";
    public static final String ID_WLAN = "_id";
    public static final String BSSID = "bssid";
    public static final String ID_VENDOR = "id_vendor";
    public static final String SSID = "ssid";
    public static final String CAPABILITIES = "capabilities";
    public static final String FREQUENCY = "frequency";
    public static final String LEVEL = "level";
    public static final String TIMESTAMP = "timestamp";
    public static final String WLAN_TYPE = "wlan_type";
    public static final String PASSWORD = "password";
    public static final String CURRENT = "current";


    public static final String DROP_TABLE_WLAN = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";



    public static final String TABLE_VENDOR = "vendor";
    public static final String TABLE_VENDOR_ID = "_id";
    public static final String TABLE_VENDOR_ID_VENDOR = "id_vendor";
    public static final String TABLE_VENDOR_VENDOR_NAME = "vendor_name";

    public static final String DROP_TABLE_VENDOR = "DROP TABLE IF EXISTS " + TABLE_VENDOR + ";";






}
