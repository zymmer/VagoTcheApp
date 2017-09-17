package br.com.vagotche.vagotcheapp;

import android.content.Intent;
import android.icu.text.IDNA;
import android.net.Uri;
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

public class MovimentacoesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MenuItem nav_menu, nav_meusdados, nav_contato, nav_info, itemwww;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_movimentacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movimentacoes);
        setSupportActionBar(toolbar);

        nav_menu = (MenuItem) findViewById(R.id.nav_menu);
        nav_meusdados = (MenuItem) findViewById(R.id.nav_meusdados);
        nav_contato = (MenuItem) findViewById(R.id.nav_contato);
        nav_info = (MenuItem) findViewById(R.id.nav_info);
        itemwww = (MenuItem) findViewById(R.id.itemwww);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_movimentacoes);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_movimentacoes);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_movimentacoes);
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
            Intent it = new Intent(MovimentacoesActivity.this, MenuActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_meusdados) {
            Intent it = new Intent(MovimentacoesActivity.this, MeusDadosActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_contato) {
            Intent it = new Intent(MovimentacoesActivity.this, ContatoActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_info) {
            Intent it = new Intent(MovimentacoesActivity.this, InfoActivity.class);
            startActivity(it);
        } else if (id == R.id.itemwww) {
            Uri uri = Uri.parse("www.vagotche.com.br");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
            //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_movimentacoes);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
