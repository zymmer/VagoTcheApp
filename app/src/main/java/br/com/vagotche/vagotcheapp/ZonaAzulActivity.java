package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.style.TtsSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ZonaAzulActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, valor, creditos5, creditos10, creditos15;
    Double saldo = 0.00;
    Button btnComprar, btnLimparCred;
    ImageView btnVoltar;
    String url = "";
    String parametros = "";

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    //Formato de moeda
    DecimalFormatSymbols dfs = new DecimalFormatSymbols (new Locale("pt", "BR"));
    // Formato com sinal de menos -5.000,00
    //DecimalFormat df1 = new DecimalFormat ("#,##0.00", dfs);
    // Formato com parêntese (5.000,00)
    DecimalFormat df2 = new DecimalFormat ("#,##0.00;(#,##0.00)", dfs);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        //Saldo
        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditos);
        seuSaldo.setText(getIntent().getStringExtra("saldo"));

        // Get IDs
        creditos5 = (TextView) findViewById(R.id.creditos5);
        creditos10 = (TextView) findViewById(R.id.creditos10);
        creditos15 = (TextView) findViewById(R.id.creditos15);
        valor = (TextView) findViewById(R.id.CampoValor);
        btnComprar = (Button) findViewById(R.id.btnComprarCred);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarCredito);
        btnLimparCred = (Button) findViewById(R.id.btnLimparCred);
        // Button listeners
        creditos5.setOnClickListener(this);
        creditos10.setOnClickListener(this);
        creditos15.setOnClickListener(this);
        btnComprar.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);
        btnLimparCred.setOnClickListener(this);

    }

    private void ComprarCredito() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Creditos/ComprarCreditos";

            parametros = "saldo=" + saldo + "&cdUsuario=" + cdUsuario;

            new SolicitaDados().execute(url);
        }

    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            if (resultado.contains("credito_adquirido")) {
                alert("Créditos Adquiridos...");

                finish();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.creditos5:
                saldo = saldo + 5;
                valor.setText(df2.format(saldo));
                break;
            case R.id.creditos10:
                saldo = saldo + 10;
                valor.setText(df2.format(saldo));
                break;
            case R.id.creditos15:
                saldo = saldo + 15;
                valor.setText(df2.format(saldo));
                break;
            case R.id.btnLimparCred:
                saldo = 0.00;
                valor.setText(df2.format(saldo));
                break;
            case R.id.imvVoltarCredito:
                finish();
                break;
            case R.id.btnComprarCred:
                if (saldo >= 5){
                    ComprarCredito();
                } else {
                    alert("Você deve informar um valor");
                }
                break;
        }
    }


}
