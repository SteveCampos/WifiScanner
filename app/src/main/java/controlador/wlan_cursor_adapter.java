package controlador;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.sql.SQLException;

import modelo.Constants;

/**
 * Created by JairSteve on 29/11/2014.
 */
public class wlan_cursor_adapter extends CursorAdapter{
    private controller_wlan dbAdapter = null ;

    public wlan_cursor_adapter(Context context, Cursor c) throws SQLException {
        super(context, c);
        dbAdapter = new controller_wlan(context);
        dbAdapter.abrir();
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView tv = (TextView) view ;
        tv.setText(cursor.getString(cursor.getColumnIndex(Constants.SSID)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        return view;
    }
}
