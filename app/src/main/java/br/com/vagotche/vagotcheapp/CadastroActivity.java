package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editNome, editSobrenome, editEmail2, editPassword2, editPassword3;
    Button btnCancelar, btnRegistrar;

    String url = "";
    String parametros = "";

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editNome = (EditText) findViewById(R.id.editNome);
        editSobrenome = (EditText) findViewById(R.id.editSobrenome);
        editEmail2 = (EditText) findViewById(R.id.editEmail2);
        editPassword2 = (EditText) findViewById(R.id.editPassword2);
        editPassword3 = (EditText) findViewById(R.id.editPassword3);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        // Button listeners
        btnCancelar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);

    }

    private void registrar() {

    ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){

            String nome = editNome.getText().toString();
            String sobrenome = editNome.getText().toString();
            String email = editEmail2.getText().toString();
            String senha = editPassword2.getText().toString();
            String repitasenha = editPassword3.getText().toString();

        if(nome.isEmpty() || sobrenome.isEmpty() || email.isEmpty() || senha.isEmpty()){
            Toast.makeText(getApplicationContext(), "Nenhum campo pode estar vazio", Toast.LENGTH_LONG).show();
        }else {
            url = "http://192.168.100.9:8090/login/logar.php";

            parametros = "nome" + nome + "&sobrenome" + sobrenome + "&email" + email + "&senha" + senha + "&repitasenha" + repitasenha;

            new SolicitaDados().execute(url);
        }

    } else {
        Toast.makeText(getApplicationContext(), "Nenhuma conexão foi detectada", Toast.LENGTH_LONG).show();
    }
    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            return ConexaoBD.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado){

            if (resultado.contains("email_erro")) {
                alert("Este e-mail já está cadastrado");
            }
            else if (resultado.contains("registro_ok")) {
                alert("Registro concluído com sucesso");

                Intent it = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(it);
            }
            else {

                alert("Ocorreu um erro");
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelar:
                finish();
                break;
            case R.id.btnRegistrar:
                registrar();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
