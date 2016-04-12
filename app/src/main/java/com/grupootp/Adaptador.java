package com.grupootp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public abstract class Adaptador extends BaseAdapter {

    private Context context;
    private int idLayout;
    private ArrayList<?> datosEntrada;

    public Adaptador(Context context, int idLayout, ArrayList<?> datosEntrada){

        this.context = context;
        this.idLayout = idLayout;
        this.datosEntrada = datosEntrada;

    }

    @Override
    public int getCount() {
        return datosEntrada.size();
    }

    @Override
    public Object getItem(int position) {
        return this.datosEntrada.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(idLayout, null);

        }

        onEntrada(datosEntrada.get(position), convertView);
        return convertView;

    }

    public abstract void onEntrada(Object object, View v);

}
