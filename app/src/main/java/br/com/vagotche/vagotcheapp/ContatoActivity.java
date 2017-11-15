package br.com.vagotche.vagotcheapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by guilherme on 14/09/17.
 */

public class ContatoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    MenuItem nav_menu, nav_meusdados, nav_movimentacoes, nav_info, itemwww;
    TextView txtNome, txtEmail;
    String nomeUsuario, emailUsuario;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_contato);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_contato);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nav_menu = (MenuItem) findViewById(R.id.nav_menu);
        nav_meusdados = (MenuItem) findViewById(R.id.nav_meusdados);
        nav_movimentacoes = (MenuItem) findViewById(R.id.nav_movimentacoes);
        nav_info = (MenuItem) findViewById(R.id.nav_info);
        itemwww = (MenuItem) findViewById(R.id.itemwww);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_contato);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_contato);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_contato);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //NavHeader
        txtNome = (TextView) findViewById(R.id.txtNome);
        txtEmail = (TextView) findViewById(R.id.txtEmail3);

        nomeUsuario = getIntent().getExtras().getString("nome_usuario");
        emailUsuario = getIntent().getExtras().getString("email_usuario");

        txtNome.setText(nomeUsuario);
        txtEmail.setText(emailUsuario);

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            Intent it = new Intent(ContatoActivity.this, MenuActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.nav_meusdados) {
            Intent it = new Intent(ContatoActivity.this, MeusDadosActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.nav_movimentacoes) {
            Intent it = new Intent(ContatoActivity.this, MovimentacoesActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.nav_info) {
            Intent it = new Intent(ContatoActivity.this, InfoActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.itemwww) {
            Uri uri = Uri.parse("http://www.vagotche.com.br");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_contato);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
