package br.com.vagotche.vagotcheapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
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
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.widget.ShareDialog;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import br.com.vagotche.vagotcheapp.FirebaseInstanceIDService;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Variaveis
    int cdUsuario;
    TextView txtNome, txtEmail;
    String nomeUsuario, emailUsuario, auxiliar, token, parquimetro;
    String url = "";
    String parametros = "";
    private ShareDialog shareDialog;
    private Button logout;
    MenuItem nav_meusdados, nav_contato, nav_movimentacoes, nav_info, itemwww;
    Intent zonaAzul, Maps, creditoIntent, movimentacoes;
    FloatingActionButton zab;
    private Chronometer chronometer;
    private long milliseconds;
    private long millisecondsStop;

    //alerta
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    //Barra de Progresso
    private ProgressDialog mProgressBar;
    int progress;

    //Formato de moeda
    DecimalFormatSymbols dfs = new DecimalFormatSymbols (new Locale("pt", "BR"));
    // Formato com sinal de menos -5.000,00
    //DecimalFormat df1 = new DecimalFormat ("#,##0.00", dfs);
    // Formato com parêntese (5.000,00)
    DecimalFormat df2 = new DecimalFormat ("#,##0.00;(#,##0.00)", dfs);

    static final String TAG = "VagoTchê";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Pega ID do Usuario em memoria
        cdUsuario = getIntent().getIntExtra("id_usuario", 0);
        //Pega o token do firebase de usuário único
        token = getIntent().getStringExtra("Token");
        //Registra um token do firebase caso o usuário esteja logando pela primeira vez no sistema
        registerToken();

//        //Cronometro
//        chronometer = (Chronometer) findViewById(R.id.chronometer);
//        chronometer.setText(DateFormat.format("kk:mm:ss", 0));
//        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener(){
//            @Override
//            public void onChronometerTick(Chronometer chronometer) {
//                long aux = SystemClock.elapsedRealtime() - chronometer.getBase();
//                chronometer.setText(DateFormat.format("kk:mm:ss", aux));
//            }
//        });
//
//        milliseconds = 0;
//        millisecondsStop = 0;

//        //Test Habilitar Bottao de Zona Azul
//        if(getIntent().getStringExtra("parquimetro") == null || getIntent().getStringExtra("parquimetro").equals("")){
//            zab.setVisibility(View.GONE);
//        } else {
//            zab.setVisibility(View.VISIBLE);
//        }

        //String token = FirebaseInstanceId.getInstance().getToken();
        //FirebaseInstanceIDService.registerToken(FirebaseInstanceId.getInstance().getToken());

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
        zab = (FloatingActionButton) findViewById(R.id.ZonaAzulBtn);
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

                mProgressBar = new ProgressDialog(MenuActivity.this);
                mProgressBar.setCancelable(false);
                mProgressBar.setTitle("Processo de Atualização");
                mProgressBar.setMessage("Iniciando...");
                mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressBar.setMax(100);
                mProgressBar.setProgress(0);
                mProgressBar.show();

                ProcessData p = new ProcessData();
                p.execute(7);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Maps = new Intent(MenuActivity.this, MapsActivity.class);
                        VerificaParquimetrosToMaps();
                        VerificaParquimetrosIdososToMaps();
                        VerificaParquimetrosDFToMaps();
                        Maps.putExtra("id_usuario", cdUsuario);
                    }
                }, 5000);

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
                auxiliar = "confAlertas";
                VerificaConfs();
            }
        });

        zab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getStringExtra("parquimetro") != null) {
                    zonaAzul = new Intent(MenuActivity.this, ZonaAzulActivity.class);
                    zonaAzul.putExtra("id_usuario", cdUsuario);
                    zonaAzul.putExtra("parquimetro", getIntent().getStringExtra("parquimetro"));
                    auxiliar = "zonaazul";
                    VerificaCreditos();
                    VerificaConfs();
                    VerificaPlacas();
                    VerificaParquimetrosZonaAzul();
                } else {
                    complain("Você precisa primeiro localizar e reservar uma vaga.");
                }

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

    public class ProcessData extends AsyncTask<Integer, String, String> {

        @Override
        protected String doInBackground(Integer... integers) {

            progress = 0;
            int total = integers[0];

            while (progress <= total) {

                try {

                    Thread.sleep(2000); // 2 segundos

                } catch (InterruptedException e) {

                }

                String m = progress % 2 == 0 ? "Carregando mapas..." +"0.0."+ progress : "Carregando parquímetros..." +"0.0."+ progress;

                // exibimos o progresso
                this.publishProgress(String.valueOf(progress), String.valueOf(total), m);

                progress++;
            }

            return "DONE";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Float progress = Float.valueOf(values[0]);
            Float total = Float.valueOf(values[1]);

            String message = values[2];

            mProgressBar.setProgress((int) ((progress / total) * 100));
            mProgressBar.setMessage(message);

            // se os valores são iguais, termianos nosso processamento
            if (values[0].equals(values[1])) {
                // removemos a dialog
                mProgressBar.dismiss();
            }
        }
    }

//    public void startChronometer(){
//        millisecondsStop = millisecondsStop > 0 ? System.currentTimeMillis() - millisecondsStop : 0;
//        chronometer.setBase(SystemClock.elapsedRealtime() - (milliseconds + millisecondsStop));
//        chronometer.start();
//        millisecondsStop = 0;
//    }
//
//    public void pauseChronometer(View view){
//        millisecondsStop = System.currentTimeMillis();
//        milliseconds = SystemClock.elapsedRealtime() - chronometer.getBase();
//        chronometer.stop();
//    }


//    public void onTokenRefresh() {
//
//        String token = FirebaseInstanceId.getInstance().getToken();
//        //int cdUsuario = 0;
//        registerToken(token);
//    }

    public void registerToken() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/PushNotification/Register";
            parametros = "token=" + token + "&cdUsuario=" + cdUsuario;

            new SolicitaDados().execute(url);
        } else {
            alert("Nenhuma conexão de rede foi detectada");
        }
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

    private void degenerateJSONMovimentacoes(String data){

        ArrayList<String> movimentacoesArray = new ArrayList<String>();

        try{
            JSONArray ja = new JSONArray(data);

            for(int i=0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String CodPagamento  = jo.getString("CodPagamento");
                movimentacoesArray.add(CodPagamento);
                String cidade  = jo.getString("Cidade");
                movimentacoesArray.add(cidade);
                String estado  = jo.getString("Estado");
                movimentacoesArray.add(estado);
                String placa  = jo.getString("Placa");
                movimentacoesArray.add(placa);
                String parquimetro  = jo.getString("Parquimetro");
                movimentacoesArray.add(parquimetro);
                String Debito  = jo.getString("Debito");
                movimentacoesArray.add(Debito);
                String DataReferencia  = jo.getString("DataReferencia");
                movimentacoesArray.add(DataReferencia);
                String Hora  = jo.getString("Hora");
                movimentacoesArray.add(Hora);
            }

            movimentacoes.putStringArrayListExtra("movimentacoesArray", movimentacoesArray);
            startActivity(movimentacoes);

        }
        catch(JSONException e){ e.printStackTrace(); }

    }

    private void degenerateJSONPlacas(String data){

        ArrayList<String> placasArray = new ArrayList<String>();

        try{
            JSONArray ja = new JSONArray(data);

            for(int i=0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String placa  = jo.getString("dsPlaca");
                placasArray.add(placa);
            }

            zonaAzul.putStringArrayListExtra("placasArray", placasArray);

        }
        catch(JSONException e){ e.printStackTrace(); }

    }

    private void degenerateJSONParquimetros(String data){

        ArrayList<String> parquimetrosArray = new ArrayList<String>();

        try{
            JSONArray ja = new JSONArray(data);

            for(int i=0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String parquimetro  = jo.getString("cdEndereco");
                parquimetrosArray.add(parquimetro);
            }

            zonaAzul.putStringArrayListExtra("parquimetrosArray", parquimetrosArray);
            startActivity(zonaAzul);

        }
        catch(JSONException e){ e.printStackTrace(); }

    }

    private void degenerateJSONParquimetrosToMaps(String data){

        try{
            JSONArray ja = new JSONArray(data);
            Maps.putExtra("parquimetrosArray", ja.toString());

        }
         catch(JSONException e){ e.printStackTrace(); }

    }

    private void degenerateJSONParquimetrosIdososToMaps(String data){

        //ArrayList<String> parquimetrosArray = new ArrayList<String>();

        try{
            JSONArray ja = new JSONArray(data);
            Maps.putExtra("parquimetrosIdososArray", ja.toString());

        }
        catch(JSONException e){ e.printStackTrace(); }

    }

    private void degenerateJSONParquimetrosDFToMaps(String data){

        //ArrayList<String> parquimetrosArray = new ArrayList<String>();

        try{
            JSONArray ja = new JSONArray(data);
            Maps.putExtra("parquimetrosDFArray", ja.toString());

//            Maps = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");
//            Maps.setAction(Intent.ACTION_VIEW);
//            Maps.setData(Uri.parse("google.navigation:/?free=1&mode=d&entry=fnls"));
//            startActivity(Maps);

            startActivityForResult(Maps,0);
        }
        catch(JSONException e){ e.printStackTrace(); }

    }

    // Verifica movimentações do usuário
    private void VerificaMovimentacoes() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Movimentacoes/VerificarMovimentacoes";
            parametros = "cdUsuario=" + cdUsuario;

            new SolicitaDadosMovimentacoes().execute(url);
        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
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
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
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
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
        }
    }

    // Verifica placas do usuário
    private void VerificaPlacas() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/ZonaAzul/VerificarPlacas";
            parametros = "cdUsuario=" + cdUsuario;
            new SolicitaDadosPlaca().execute(url);

        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
        }
    }

    // Verifica parquimetros
    private void VerificaParquimetrosZonaAzul() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/ZonaAzul/VerificarParquimetros";
            new SolicitaDadosParquimetros().execute(url);

        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
        }
    }

    // Verifica parquimetros disponiveis no mapa
    private void VerificaParquimetrosToMaps() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {


            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Maps/VerificarParquimetros";
            new SolicitaDadosParquimetrosToMaps().execute(url);

        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
        }
    }

    // Verifica parquimetros Idosos
    private void VerificaParquimetrosIdososToMaps() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Maps/VerificarParquimetrosIdosos";
            new SolicitaDadosParquimetrosIdososToMaps().execute(url);

        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
        }
    }

    // Verifica parquimetros DF
    private void VerificaParquimetrosDFToMaps() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Maps/VerificarParquimetrosDF";
            new SolicitaDadosParquimetrosDFToMaps().execute(url);

        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
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

                switch (auxiliar) {
                    case "credito":
                        creditoIntent.putExtra("status2", b3new);
                        break;
                    case "zonaazul":
                        zonaAzul.putExtra("status3", b3new);
                        break;
                    case "confAlertas":
                        Intent it = new Intent(MenuActivity.this, ConfiguracaoAlertasActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        it.putExtra("status1", b1new);
                        it.putExtra("status2", b2new);
                        it.putExtra("status3", b3new);
                        it.putExtra("status4", b4new);
                        it.putExtra("id_usuario", cdUsuario);
                        startActivity(it);
                        break;
                }
            }

            //VerificaSaldo
            if (resultado.contains("verifica_creditos_ok")) {

                String[] dadosSaldo = resultado.split(",");

                if(auxiliar.contains("credito")) {
                    creditoIntent = new Intent(MenuActivity.this, CreditosActivity.class);
                    creditoIntent.putExtra("saldo", df2.format(Double.parseDouble(dadosSaldo[1])));
                    creditoIntent.putExtra("id_usuario", cdUsuario);
                    creditoIntent.putExtra("token", token);
                    startActivity(creditoIntent);
                } else if (auxiliar.contains("zonaazul")) {

                    zonaAzul.putExtra("saldoZA", df2.format(Double.parseDouble(dadosSaldo[1])));

                };

            }

        }
    }

    private class SolicitaDadosMovimentacoes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            degenerateJSONMovimentacoes(resultado);

        }
    }

    private class SolicitaDadosPlaca extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            degenerateJSONPlacas(resultado);

        }
    }

    private class SolicitaDadosParquimetros extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            degenerateJSONParquimetros(resultado);

        }
    }

    private class SolicitaDadosParquimetrosToMaps extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            degenerateJSONParquimetrosToMaps(resultado);

        }
    }

    private class SolicitaDadosParquimetrosIdososToMaps extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            degenerateJSONParquimetrosIdososToMaps(resultado);

        }
    }

    private class SolicitaDadosParquimetrosDFToMaps extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            degenerateJSONParquimetrosDFToMaps(resultado);

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

    void complain(String message) {
        Log.e(TAG, "**** Vago Tchê Error: " + message);
        alertDialog(message);
        //alert("Error: " + message);
    }

    void alertDialog(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_meusdados) {
            Intent it = new Intent(this, MeusDadosActivity.class);
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
            Intent it = new Intent(this, ContatoActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.nav_movimentacoes) {
            movimentacoes = new Intent(this, MovimentacoesActivity.class);
            movimentacoes.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            movimentacoes.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            movimentacoes.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            VerificaMovimentacoes();
        } else if (id == R.id.nav_info) {
            Intent it = new Intent(this, InfoActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            startActivity(it);
        } else if (id == R.id.itemwww) {
            Uri uri = Uri.parse("http://www.vagotche.com.br");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.putExtra("nome_usuario", getIntent().getExtras().getString("nome_usuario"));
            it.putExtra("email_usuario", getIntent().getExtras().getString("email_usuario"));
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
            //} else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
