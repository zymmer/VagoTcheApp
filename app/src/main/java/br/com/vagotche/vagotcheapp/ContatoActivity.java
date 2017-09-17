package br.com.vagotche.vagotcheapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * Created by guilherme on 14/09/17.
 */

public class ContatoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MenuItem nav_menu;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_meusdados);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_meusdados);
        setSupportActionBar(toolbar);

        nav_menu = (MenuItem) findViewById(R.id.nav_menu);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_meusdados);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_meusdados);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_meusdados);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent it = new Intent(ContatoActivity.this, MenuActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_contato) {

        } else if (id == R.id.nav_movimentacoes) {

        } else if (id == R.id.nav_manage) {

            //} //else if (id == R.id.nav_share) {

            //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_meusdados);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}