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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guilherme on 14/09/17.
 */

public class MovimentacoesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    //Variaveis
    int cdUsuario;
    MenuItem nav_menu, nav_meusdados, nav_contato, nav_info, itemwww;
    TextView txtNome, txtEmail;
    String nomeUsuario, emailUsuario;

    //ExpandableList
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_movimentacoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_movimentacoes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Pega ID do Usuario em memoria
        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        nav_menu = (MenuItem) findViewById(R.id.nav_menu);
        nav_meusdados = (MenuItem) findViewById(R.id.nav_meusdados);
        nav_contato = (MenuItem) findViewById(R.id.nav_contato);
        nav_info = (MenuItem) findViewById(R.id.nav_info);
        itemwww = (MenuItem) findViewById(R.id.itemwww);


        //ExpandableList
        buildList();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewMeusVeiculos);
        expandableListView.setAdapter(new ExpandableAdapter(MovimentacoesActivity.this, listGroup, listData) {
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //alert("Group: " +groupPosition+ " Item: "+childPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //alert("Group(Expand): " +groupPosition);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //alert("Group(Collapse): " +groupPosition);
            }
        });

        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_movimentacoes);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_movimentacoes);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void buildList() {
        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<String>>();

        getIntent().getStringArrayListExtra("veiculosArray");

        int x = 0;

        for (int i = 0; i < getIntent().getStringArrayListExtra("veiculosArray").size(); i = i + 4) {

            //GROUP
            listGroup.add(getIntent().getStringArrayListExtra("veiculosArray").get(i));

            //CHILDREM
            List<String> auxList = new ArrayList<String>();
            auxList.add("Placa: " + getIntent().getStringArrayListExtra("veiculosArray").get(i + 1));
            auxList.add("Ano-Fabricação: " + getIntent().getStringArrayListExtra("veiculosArray").get(i + 2));
            auxList.add("Ano-Modelo: " + getIntent().getStringArrayListExtra("veiculosArray").get(i + 3));

            listData.put(listGroup.get(x), auxList);
            x = x + 1;

        }
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
            Intent it = new Intent(MovimentacoesActivity.this, MenuActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.nav_meusdados) {
            Intent it = new Intent(MovimentacoesActivity.this, MeusDadosActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("id_usuario", cdUsuario);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            it.putExtra("cpf", getIntent().getExtras().getString("cpf"));
            it.putExtra("df", getIntent().getExtras().getString("df"));
            it.putExtra("idoso", getIntent().getExtras().getString("idoso"));
            it.putExtra("data", getIntent().getExtras().getString("data"));
            startActivity(it);
        } else if (id == R.id.nav_contato) {
            Intent it = new Intent(MovimentacoesActivity.this, ContatoActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.nav_info) {
            Intent it = new Intent(MovimentacoesActivity.this, InfoActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_movimentacoes);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
