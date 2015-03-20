package controlador;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rupture.jairsteve.rupture.R;

import java.sql.SQLException;

import modelo.Constants;

/**
 * Created by JairSteve on 29/11/2014.
 */
public class wlan_cursor_adapter extends CursorAdapter{
    private controller_wlan dbAdapter = null ;
    private LayoutInflater cursorInflater;



    public wlan_cursor_adapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

        dbAdapter = new controller_wlan(context);
        dbAdapter.abrir();

        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {

        //OBTENGO LAS VISTAS
        TextView textViewSSID = (TextView) view.findViewById(R.id.textView_ssid);
        TextView textViewLevel = (TextView) view.findViewById(R.id.textView_level);
        TextView textViewFabricante = (TextView) view.findViewById(R.id.textView_fabricante);
        LinearLayout linearLayoutDetail = (LinearLayout) view.findViewById(R.id.linearLayoutDetail);


        //OBTENGO LOS DATOS
        String ssid = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SSID));
        //String ssid = "IMAGINE DRAGONS, AHORA BASTILLE";
        int level = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.LEVEL));
        String id_vendor = cursor.getString(cursor.getColumnIndexOrThrow(Constants.ID_VENDOR));
        int current = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.CURRENT));
        String fabricante = cursor.getString(cursor.getColumnIndexOrThrow(Constants.FABRICANTE));

        if (ssid.length()==0){
            //ssid = "RED OCULTA";
        }


        if (current==0){
            linearLayoutDetail.setAlpha((float) 0.3);
        }else if (current==1){
            linearLayoutDetail.setAlpha(1);
        }else{
            linearLayoutDetail.setAlpha((float) 0.3);
        }


        int convertLevel =convertLevel(level);

        textViewSSID.setText(ssid);
        textViewLevel.setText(""+convertLevel);
        textViewFabricante.setText(fabricante);


        if (isBetween(convertLevel,-100,20)){
            textViewLevel.setTextColor(context.getResources().getColor(R.color.rojo));
        }else if (isBetween(convertLevel, 21, 40)){
            textViewLevel.setTextColor(context.getResources().getColor(R.color.amarillo));
        }else if (isBetween(convertLevel, 41, 70)){
            textViewLevel.setTextColor(context.getResources().getColor(R.color.verde));
        }else if(isBetween(convertLevel, 71, 200)){
            textViewLevel.setTextColor(context.getResources().getColor(R.color.azul));
        }

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return cursorInflater.inflate(R.layout.detail_scan, parent, false);
    }

    public int convertLevel(int level){
        return 100 + level;
    }
    public static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }
}
