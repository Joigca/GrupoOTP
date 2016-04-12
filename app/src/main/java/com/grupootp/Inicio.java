package com.grupootp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.grupootp.Fragment.Fragment_Encuentranos.Activity_Mostrar_Oficina;
import com.grupootp.Fragment.Fragment_Encuentranos.Fragment_Encuentranos;
import com.grupootp.Fragment.Fragment_Encuentranos.Fragment_Encuentranos_Buscador;
import com.grupootp.Fragment.Fragment_Inicio.Fragment_Inicio_Cuadricula;
import com.grupootp.grupootp.R;

import java.util.ArrayList;

/**
 * Created by Jose Igualada
 *            joseigualda@gmail.com
 */

public class Inicio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private ArrayList<Fragment> contFragment = new ArrayList<Fragment>();
    private int numFragment = 0;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout linear;
    private DrawerLayout drawerLayout;
    Fragment_Inicio_Cuadricula fragment_inicio;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contFragment.add(fragment_inicio);

        linear = (LinearLayout)findViewById(R.id.linear);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pager = (ViewPager) findViewById(R.id.viewPager);

        cargaToolbar();
        cargaFragmentXDefecto();
        drawerIcon();

    }

    public void cargaFragmentXDefecto(){

        /**
         * Fragment Por Defecto
         */

        fragment_inicio = new Fragment_Inicio_Cuadricula();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment_inicio).commit();

    }

    public void viewPager(String url){

    }

    public void cargaToolbar(){

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

    @Override
    public void onBackPressed() {

        //Toast.makeText(getApplicationContext(), "atras", Toast.LENGTH_SHORT).show();
        //Log.d("FRAGMENTS APILADOR", getSupportFragmentManager().getFragments().toString());
        //Log.d("APILADOR", contFragment.get(numFragment).toString());
        if (numFragment < 0){

            Fragment fragment = contFragment.get(numFragment);
            Log.d("mi tamanyo es", ""+contFragment.size());

            getSupportFragmentManager().beginTransaction().replace(R.id.main_content,contFragment.get(numFragment)).commit();
            //numFragment--;

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        /**
         * VENTANA INICIO
         */

        if (id == R.id.inicioCuadricula){

            fragment = new Fragment_Inicio_Cuadricula();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
            Log.d("IDentificadr INICIO", "" + fragment.getId());

        }

        /**
         * VENTANA CONOCENOS (BLOG)
         */

        if (id == R.id.conocenos){

            Intent URL = new Intent(Intent.ACTION_VIEW);
            URL.setData(Uri.parse("http://www.grupotp.org/index.php"));
            startActivity(URL);

        }

        if (id == R.id.avisoLegal){

            Intent URL = new Intent(Intent.ACTION_VIEW);
            URL.setData(Uri.parse("http://www.grupotp.org/politica-aviso.php"));
            startActivity(URL);

        }

        /**
         * VENTANA CONOCENOS (BLOG)
         */

        if (id == R.id.descargas){

            Intent URL = new Intent(Intent.ACTION_VIEW);
            URL.setData(Uri.parse("http://blog.grupotp.org/fichas-promocion-la-salud/"));
            startActivity(URL);

        }

        /**
         * VENTANA ENCUENTRANOS
         */

        if (id == R.id.encuentranos){


            if (fragment instanceof Fragment_Encuentranos_Buscador){
                getSupportFragmentManager().beginTransaction()
                        .remove(fragment).commit();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            fragment = new Fragment_Encuentranos_Buscador();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment).addToBackStack(null).commit();
            numFragment++;
           contFragment.add(fragment);
            Log.d("IDentificadr", "" + fragment.getId());

        }

        /**
         * VENTANA COORDINACION
         */

        if (id == R.id.coordinacion){

            Intent URL = new Intent(Intent.ACTION_VIEW);
            URL.setData(Uri.parse("http://coordinacion.grupotp-previnet.es/"));
            startActivity(URL);

        }

        /**
         * VENTANA EXAMEN SALUD
         */

        if (id == R.id.examenSalud){

            Intent URL = new Intent(Intent.ACTION_VIEW);
            URL.setData(Uri.parse("http://www.grupotp-previnet.es/descargas/"));
            startActivity(URL);

        }

        /**
         * VENTANA AREA CLIENTES PREVINET
         */

        if (id == R.id.areaClientesPrevinet){

            Intent URL = new Intent(Intent.ACTION_VIEW);
            URL.setData(Uri.parse("http://clientes.grupotp-previnet.es/"));
            startActivity(URL);

        }

        /**
         * VENTANA PREFERENCIAS
         */
/*
        if (id == R.id.preferencias){

            Intent mostrarOficinas = new Intent(getApplicationContext(), Activity_Mostrar_Oficina.class);
            startActivity(mostrarOficinas);

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void drawerIcon(){

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }
}
