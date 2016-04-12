package com.grupootp.Fragment.Fragment_Inicio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grupootp.CreateSQLite;
import com.grupootp.Modelos.Slide;
import com.grupootp.grupootp.R;

import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Slide extends Fragment {

    private CreateSQLite db;

    //Contexto del FRAGMENT
    private ViewGroup rootView;

    //Componentes
    RelativeLayout fondoSlide;
    TextView txtSlideTitulo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = (ViewGroup) inflater.inflate(R.layout.layout_slide, container, false);

        fondoSlide = (RelativeLayout) rootView.findViewById(R.id.fondoSlide);

        return rootView;

    }

   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
