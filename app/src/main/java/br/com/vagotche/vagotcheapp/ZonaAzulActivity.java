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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class ZonaAzulActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, txvTempo30, txvTempo1, txvTempo130, txvTempo2;
    Spinner spinnerPlaca, spinnerCidade, spinnerParquimetro;
    Double valor = 0.00;
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
        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditosZA);
        seuSaldo.setText(getIntent().getStringExtra("saldo"));

        // Get IDs
        spinnerPlaca = (Spinner) findViewById(R.id.spinnerPlaca);
        //spinnerPlaca.getSelectedItem(getIntent().getStringExtra("placa"));
        spinnerCidade = (Spinner) findViewById(R.id.spinnerCidade);
        spinnerParquimetro = (Spinner) findViewById(R.id.spinnerParquimetro);
        txvTempo30 = (TextView) findViewById(R.id.txvTempo30);
        txvTempo1 = (TextView) findViewById(R.id.txvTempo1);
        txvTempo130 = (TextView) findViewById(R.id.txvTempo130);
        txvTempo2 = (TextView) findViewById(R.id.txvTempo2);

        btnComprar = (Button) findViewById(R.id.btnComprarCred);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarZonaAzul);
        btnLimparCred = (Button) findViewById(R.id.btnLimparCred);
        // Button listeners
        txvTempo30.setOnClickListener(this);
        txvTempo1.setOnClickListener(this);
        txvTempo130.setOnClickListener(this);
        txvTempo2.setOnClickListener(this);
        spinnerPlaca.setOnClickListener(this);
        spinnerCidade.setOnClickListener(this);
        spinnerParquimetro.setOnClickListener(this);
        btnComprar.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);
        btnLimparCred.setOnClickListener(this);

    }

    private void UtilizarCredito() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Creditos/ComprarCreditos";

            parametros = "saldo=" + valor + "&cdUsuario=" + cdUsuario;

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
            case R.id.txvTempo30:
                //valor = valor + 5;
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo1:
                //valor = valor + 10;
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo130:
                //valor = valor + 15;
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo2:
                //valor = 0.00;
                //valor.setText(df2.format(valor));
                break;
            case R.id.imvVoltarZonaAzul:
                finish();
                break;
            case R.id.btnComprarCred:
                if (valor >= 5){
                    UtilizarCredito();
                } else {
                    alert("Você deve informar um valor");
                }
                break;
        }
    }


}
