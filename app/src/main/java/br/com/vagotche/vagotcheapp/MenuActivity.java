package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Variaveis
    int cdUsuario;
    TextView txtNome, txtEmail;
    String nomeUsuario, emailUsuario;

    String url = "";
    String parametros = "";

    //alerta
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pega ID do Usuario em memoria
        cdUsuario = getIntent().getIntExtra("id_usuario", 0);
        //alert("ID in menu= "+ cdUsuario);

        FloatingActionButton gmb = (FloatingActionButton) findViewById(R.id.googleMapsBtn);
        FloatingActionButton cb = (FloatingActionButton) findViewById(R.id.CreditosBtn);
        FloatingActionButton cvb = (FloatingActionButton) findViewById(R.id.CadastroVeiculoBtn);
        FloatingActionButton cab = (FloatingActionButton) findViewById(R.id.ConfiguracaoAlertasBtn);
        FloatingActionButton zab = (FloatingActionButton) findViewById(R.id.ZonaAzulBtn);
        FloatingActionButton teste = (FloatingActionButton) findViewById(R.id.teste6Btn);

        //Redirecionamento Botoes

        gmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, MapsActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });

        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CreditosActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });

        cvb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CadastroVeiculoActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });

        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificaConfs();
                Intent it = new Intent(MenuActivity.this, ConfiguracaoAlertasActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });

        zab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, ZonaAzulActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });

        // Teste
        teste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, DeviceControlActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });

        //Fim Redirecionamento

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // [START signIn]
    private void VerificaConfs() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/ConfAlertas/VerificarAlertas";

            parametros = "cdUsuario=" + cdUsuario;

            new SolicitaDados().execute(url);
        } else {
            alert("Nenhuma conexão de rede foi detectada");
        }
    }


    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado){

            //teste
            alert(resultado);


            if (resultado.contains("response_ok")) {

                //teste
                alert(resultado);

                String[] dados = resultado.split(",");
                alert("1= " + dados[1] + "2= " + dados[2]);

//                Intent it = new Intent(MenuActivity.this, MenuActivity.class);
//                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                it.putExtra("alerta1", cdUsuario);
//                it.putExtra("alerta2", dados[2]);
//                it.putExtra("alerta3", dados[3]);
//                it.putExtra("alerta3", dados[3]);
//                it.putExtra("alerta3", dados[3]);
//                it.putExtra("alerta3", dados[3]);
//                startActivity(it);
            } else if (resultado.contains("cpf_invalido")){
                alert("CPF inválido");

            } else if (resultado.contains("cpf_nao_cadastrado_ou_senha_invalida")){
                alert("CPF não cadastrado ou senha incorreta");

            }

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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        //} //else if (id == R.id.nav_share) {

        //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
