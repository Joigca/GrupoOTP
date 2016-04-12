package com.grupootp.Fragment.Fragment_Encuentranos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grupootp.CreateSQLite;
import com.grupootp.Modelos.Centros;
import com.grupootp.grupootp.R;

import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Fragment_Encuentranos extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View view;
    private ArrayList<Centros> arrayCentros = new ArrayList<Centros>();

    public Fragment_Encuentranos(){

        /**
         * Constructor VACIO
         */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (View)inflater.inflate(R.layout.fragment_encuentranos, container, false);

        /**
         * Tablayout
         */

        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.listaOficinas));
        //tabLayout.addTab(tabLayout.newTab().setText(R.string.mapa));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.buscador));


        /**
         * ViewPager
         */

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Cambiar el Titulo del Activity
        getActivity().setTitle(R.string.encuentranos);

        viewPagerListener();

        return view;
    }


    /**
     * SUBCLASE ViewPagerAdapter
     */

    public class ViewPagerAdapter extends FragmentPagerAdapter{

        int numTabs;

        public ViewPagerAdapter(FragmentManager manager, int numTabs) {
            super(manager);
            this.numTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){

                /**
                 * En cada CASE pondriamos los arguments que pasaran a todas los tabs del fragment, ya sean datos consultados de base de datos ó modificaciones
                 */
/*
                case 0:

                    Fragment_Encuentranos_Oficinas oficnas = new Fragment_Encuentranos_Oficinas();
                    return oficnas;

                case 1:

                    Fragment_Encuentranos_Maps mapa = new Fragment_Encuentranos_Maps();
                    return mapa;
*/
                case 2:

                    Fragment_Encuentranos_Buscador buscador = new Fragment_Encuentranos_Buscador();
                    return buscador;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return numTabs;
        }
    }


    public void viewPagerListener(){

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
