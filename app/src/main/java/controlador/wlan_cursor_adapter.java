package controlador;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
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
        ImageView imageViewLevel = (ImageView) view.findViewById(R.id.imageViewSignal);
        TextView textViewFabricante = (TextView) view.findViewById(R.id.textView_fabricante);
        LinearLayout linearLayoutDetail = (LinearLayout) view.findViewById(R.id.linearLayoutDetail);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        //OBTENGO LOS DATOS
        String ssid = cursor.getString(cursor.getColumnIndexOrThrow(Constants.SSID));
        //String ssid = "IMAGINE DRAGONS, AHORA BASTILLE";
        int level = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.LEVEL));
        String id_vendor = cursor.getString(cursor.getColumnIndexOrThrow(Constants.ID_VENDOR));
        int current = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.CURRENT));
        String fabricante = cursor.getString(cursor.getColumnIndexOrThrow(Constants.FABRICANTE));
        int wlan_type = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.WLAN_TYPE));

        if (ssid.length()==0){
            //ssid = "RED OCULTA";
        }

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            if (current==0){
                textViewSSID.setTextColor(context.getResources().getColor(R.color.VerdeClaro));
                textViewFabricante.setTextColor(context.getResources().getColor(R.color.VerdeClaro));
            }else if (current==1){
                //DO NOTHING
                textViewSSID.setTextColor(context.getResources().getColor(R.color.PersonalizadoSteve4));
                textViewFabricante.setTextColor(context.getResources().getColor(R.color.PersonalizadoSteve4));
            }else{
                textViewSSID.setTextColor(context.getResources().getColor(R.color.VerdeClaro));
                textViewFabricante.setTextColor(context.getResources().getColor(R.color.VerdeClaro));
            }
        }else{
            if (current==0){
                linearLayoutDetail.setAlpha((float) 0.3);
            }else if (current==1){
                linearLayoutDetail.setAlpha(1);
            }else{
                linearLayoutDetail.setAlpha((float) 0.3);
            }

        }

        int convertLevel =convertLevel(level);

        textViewSSID.setText(ssid);
        textViewFabricante.setText(fabricante);


        if (isBetween(convertLevel,-100,10)){
            //textViewLevel.setTextColor(context.getResources().getColor(R.color.rojo));
            imageViewLevel.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_0_bar_black_36dp));
        }else if (isBetween(convertLevel, 11, 20)){
            imageViewLevel.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_1_bar_black_36dp));
        }else if (isBetween(convertLevel, 21, 30)){
            imageViewLevel.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_2_bar_black_36dp));
        }else if (isBetween(convertLevel, 31, 50)){
            imageViewLevel.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_3_bar_black_36dp));
        }else if(isBetween(convertLevel, 51, 200)){
            imageViewLevel.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_signal_cellular_4_bar_black_36dp));
        }

        if (wlan_type==1&&(!fabricante.equals("FABRICANTE DESCONOCIDO"))){
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_done_black_48dp));
        }else{
            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_wifi_lock_white_48dp));
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
