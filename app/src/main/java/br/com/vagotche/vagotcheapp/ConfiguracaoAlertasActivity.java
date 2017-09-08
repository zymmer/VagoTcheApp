package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class ConfiguracaoAlertasActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis
    int cdUsuario;
    Button btnCancelar, btnSalvar;
    Boolean alerta1, alerta2, alerta3, alerta4;
    String url = "";
    String parametros = "";

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_alertas);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        // Get IDs
        btnCancelar = (Button) findViewById(R.id.btnCancelarConf);
        btnSalvar = (Button) findViewById(R.id.btnSalvarConf);
        // Button listeners
        btnCancelar.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);
    }

    private void salvarConf() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            if (alerta1 == null ){
                alerta1 = false;
            }
            if (alerta2 == null ){
                alerta2 = false;
            }
            if (alerta3 == null ){
                alerta3 = false;
            }
            if (alerta4 == null ){
                alerta4 = false;
            }

                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/ConfAlertas/ConfigurarAlertas";

                parametros = "alerta1=" + alerta1 + "&alerta2=" + alerta2 + "&alerta3=" + alerta3 + "&alerta4=" + alerta4 + "&cdUsuario=" + cdUsuario;

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

            //teste
            //alert(resultado);

            if (resultado.contains("conf_atualizada")) {
                alert("Configurações Atualizadas...");

                finish();
            }

        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.alerta1:
                if (checked)
                    alerta1 = true;
            else
                    alerta1 = false;
                break;
            case R.id.alerta2:
                if (checked)
                    alerta2 = true;
            else
                    alerta2 = false;
                break;
            case R.id.alerta3:
                if (checked)
                    alerta3 = true;
                else
                    alerta3 = false;
                break;
            case R.id.alerta4:
                if (checked)
                    alerta4 = true;
                else
                    alerta4 = false;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelarConf:
                finish();
                break;
            case R.id.btnSalvarConf:
                salvarConf();
                break;
        }
    }

}
