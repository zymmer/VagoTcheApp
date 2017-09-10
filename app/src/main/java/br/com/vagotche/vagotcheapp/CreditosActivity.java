package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreditosActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, valor, credito5, credito10, credito15;
    Float saldo;
    Button btnComprar;
    ImageView btnVoltar;
    String url = "";
    String parametros = "";

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditos);
        seuSaldo.setText(getIntent().getStringExtra("saldo"));

        // Get IDs
        btnComprar = (Button) findViewById(R.id.btnComprarCred);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarCredito);
        // Button listeners
        btnComprar.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);

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

            if (resultado.contains("conf_atualizada")) {
                alert("Configurações Atualizadas...");

                finish();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvVoltarCredito:
                finish();
                break;
            case R.id.btnComprarCred:
                ComprarCredito();
                break;
        }
    }


}
