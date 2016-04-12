package com.grupootp.Fragment.Fragment_Inicio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grupootp.Modelos.Noticias;
import com.grupootp.grupootp.R;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Elemento_Blog extends Fragment {

    private TextView txtTituloBlog, txtDescripcionBlog;
    private ImageView imgBlog;
    private Noticias not;

    public Elemento_Blog(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.elemento_blog, container, false);

        txtTituloBlog = (TextView)view.findViewById(R.id.txtTituloBlog);
        //txtDescripcionBlog = (TextView) view.findViewById(R.id.txtDescripcionBlog);
        //imgBlog = (ImageView) view.findViewById(R.id.imgFeed);

        txtTituloBlog.setText(not.getTitulo());
        txtDescripcionBlog.setText(not.getDesc());

        return view;
    }

    public void setItem(Noticias not){

        this.not = not;

    }

}
