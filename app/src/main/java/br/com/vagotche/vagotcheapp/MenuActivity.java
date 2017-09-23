package br.com.vagotche.vagotcheapp;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Variaveis
    int cdUsuario;
    TextView txtNome, txtEmail;
    String nomeUsuario, emailUsuario, auxiliar;
    String url = "";
    String parametros = "";
    private ShareDialog shareDialog;
    private Button logout;
    MenuItem nav_meusdados, nav_contato, nav_movimentacoes, nav_info, itemwww;




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

//        //FB
//        FacebookSdk.sdkInitialize(this);
//
//        //Testes FB
//        shareDialog = new ShareDialog(this);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);dados
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ShareLinkContent content = new ShareLinkContent.Builder().build();
//                shareDialog.show(content);
//            }
//        });
//
//        Bundle inBundle = getIntent().getExtras();
//        String name = inBundle.get("name").toString();
//        String surname = inBundle.get("surname").toString();
//        String imageUrl = inBundle.get("imageUrl").toString();
//
//        TextView nameView = (TextView) findViewById(R.id.txtNameAndSurname);
//        nameView.setText("" + name + " " + surname);
//        Button logout = (Button) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginManager.getInstance().logOut();
//                Intent login = new Intent(MenuActivity.this, MainActivity.class);
//                startActivity(login);
//                finish();
//            }
//
//        });
//        new MenuActivity.DownloadImage((ImageView)findViewById(R.id.fbImg)).execute(imageUrl);

        FloatingActionButton gmb = (FloatingActionButton) findViewById(R.id.googleMapsBtn);
        FloatingActionButton cb = (FloatingActionButton) findViewById(R.id.CreditosBtn);
        FloatingActionButton cvb = (FloatingActionButton) findViewById(R.id.CadastroVeiculoBtn);
        FloatingActionButton cab = (FloatingActionButton) findViewById(R.id.ConfiguracaoAlertasBtn);
        FloatingActionButton zab = (FloatingActionButton) findViewById(R.id.ZonaAzulBtn);
        FloatingActionButton Irrb = (FloatingActionButton) findViewById(R.id.IrregularidadesBtn);
        nav_meusdados = (MenuItem)findViewById(R.id.nav_meusdados);
        nav_contato = (MenuItem)findViewById(R.id.nav_contato);
        nav_movimentacoes = (MenuItem)findViewById(R.id.nav_movimentacoes);
        nav_info = (MenuItem)findViewById(R.id.nav_info);
        itemwww = (MenuItem)findViewById(R.id.itemwww);

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
                auxiliar = "credito";
                VerificaCreditos();
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
            }
        });

        zab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auxiliar = "zonaazul";
                VerificaCreditos();
                //VerificaPlacas();
            }
        });

        Irrb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, IrregularidadesActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
            }
        });
//
//        //Fim Redirecionamento
//
//        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        //fab.setOnClickListener(new View.OnClickListener() {
//        //    @Override
//        //    public void onClick(View view) {
//        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//        //                .setAction("Action", null).show();
//        //    }
//        //});
//
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

//    public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
//        ImageView bmImage;
//
//        public DownloadImage(ImageView bmImage){
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls){
//            String urldisplay = urls[0];
//            Bitmap mIconll = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIconll = BitmapFactory.decodeStream(in);
//            } catch (Exception e){
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIconll;
//        }
//
//        protected void onPostExecute(Bitmap result){
//            bmImage.setImageBitmap(result);
//        }
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // Verifica configurações de alerta do usuário
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

    // Verifica saldo do usuário
    private void VerificaCreditos() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Creditos/VerificarCreditos";

            parametros = "cdUsuario=" + cdUsuario;

            new SolicitaDados().execute(url);
        } else {
            alert("Nenhuma conexão de rede foi detectada");
        }
    }

    // Verifica placas do usuário
    private void VerificaPlacas() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/ZonaAzul/VerificarCarros";

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

            //VerificaAlertas
            if (resultado.contains("verifica_alertas_ok")) {

                String[] dados = resultado.split(",");

                boolean b1new = false;
                boolean b2new = false;
                boolean b3new = false;
                boolean b4new = false;

                if (dados[1].contains("1")) {
                    b1new = true;
                }
                if (dados[2].contains("1")) {
                    b2new = true;
                }
                if (dados[3].contains("1")) {
                    b3new = true;
                }
                if (dados[4].contains("1")) {
                    b4new = true;
                }

                Intent it = new Intent(MenuActivity.this, ConfiguracaoAlertasActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.putExtra("status1", b1new);
                it.putExtra("status2", b2new);
                it.putExtra("status3", b3new);
                it.putExtra("status4", b4new);
                it.putExtra("id_usuario", cdUsuario);

                startActivity(it);
            }

            //VerificaSaldo
            if (resultado.contains("verifica_creditos_ok")) {

                String[] dadosSaldo = resultado.split(",");

                if(auxiliar.contains("credito")) {
                    Intent it = new Intent(MenuActivity.this, CreditosActivity.class);
                    it.putExtra("saldo", dadosSaldo[1]);
                    it.putExtra("id_usuario", cdUsuario);
                    startActivity(it);
                } else if (auxiliar.contains("zonaazul")) {
                    Intent it = new Intent(MenuActivity.this, ZonaAzulActivity.class);
                    it.putExtra("saldoZA", dadosSaldo[1]);
                    it.putExtra("id_usuario", cdUsuario);
                    startActivity(it);
                };

            }

            //VerificaPlacas
            if (resultado.contains("verifica_placas_ok")) {

                String[] dadosPlacas = resultado.split(",");

                Intent it = new Intent(MenuActivity.this, ZonaAzulActivity.class);
                it.putExtra("ArrayPlacas", dadosPlacas);
                it.putExtra("id_usuario", cdUsuario);
                startActivity(it);
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meusdados) {
            Intent it = new Intent(MenuActivity.this, MeusDadosActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_contato) {
            Intent it = new Intent(MenuActivity.this, ContatoActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_movimentacoes) {
            Intent it = new Intent(MenuActivity.this, MovimentacoesActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_info) {
            Intent it = new Intent(MenuActivity.this, InfoActivity.class);
            startActivity(it);
        } else if (id == R.id.itemwww) {
            Uri uri = Uri.parse("http://www.vagotche.com.br");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
            //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
