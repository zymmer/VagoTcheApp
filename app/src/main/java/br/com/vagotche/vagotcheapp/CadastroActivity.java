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

    EditText editNome, editCPF2, editEmail2, editPassword2, editPassword3;
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
        editCPF2 = (EditText) findViewById(R.id.editCPF2);
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
            String cpf = editCPF2.getText().toString();
            String email = editEmail2.getText().toString();
            String senha = editPassword2.getText().toString();

        if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()){
            alert("Nenhum campo pode estar vazio");
        }else {
            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Cadastro/CadastrarLogin";

            parametros = "nome" + nome + "&cpf" + cpf + "&email" + email + "&senha" + senha;

            new SolicitaDados().execute(url);
        }

        } else {
            alert("Nenhuma conexão foi detectada");
        }
    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls){

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado){

            if (resultado.contains("cadastro_ok")) {
                alert("Registro realizado com sucesso...");

                Intent it = new Intent(CadastroActivity.this, MainActivity.class);
                startActivity(it);

            } else if (resultado.contains("cpf_invalido")) {
                alert("CPF inválido");

            } else if (resultado.contains("email_invalido")) {
                alert("E-mail inválido");

            } else if (resultado.contains("senha_invalida")) {
                alert("Senha deve conter ao menos 8 caracteres");

            } else {
                alert("Erro inesperado");

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
