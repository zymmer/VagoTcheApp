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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editNome, editCPF2, editEmail1, editPassword2, editPassword3;
    Button btnCancelar, btnRegistrar;
    CheckBox rdbIdoso, rdbDF;
    Boolean b1, b2;
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
        editEmail1 = (EditText) findViewById(R.id.editEmail1);
        editPassword2 = (EditText) findViewById(R.id.editPassword2);
        //editPassword3 = (EditText) findViewById(R.id.editPassword3);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        // Get RadioButton
        rdbIdoso = (CheckBox) findViewById(R.id.rbtnIdoso);
        rdbDF = (CheckBox) findViewById(R.id.rbtnDF);

        // Button listeners
        btnCancelar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);

    }

    private void registrar() {

        ConnectivityManager connMgr = (ConnectivityManager)
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Pattern password = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\\\S+$).{6,32}$");

        if (networkInfo != null && networkInfo.isConnected()){

            String nome = editNome.getText().toString();
            String cpf = editCPF2.getText().toString();
            String email = editEmail1.getText().toString();
            String senha = editPassword2.getText().toString();

            if (rdbIdoso.isChecked()){
                b1 = true;
            } else {
                b1 = false;
            }

            if (rdbDF.isChecked()){
                b2 = true;
            } else {
                b2 = false;
            }

        if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()){
            alert("Nenhum campo pode estar vazio");
        } else if (password.matcher(senha).matches()){

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Cadastro/CadastrarLogin";

            parametros = "nome=" + nome + "&cpf=" + cpf + "&email=" + email + "&senha=" + senha + "&idoso=" + b1 + "&df=" + b2;

            new SolicitaDados().execute(url);

        } else {
            alert("A senha deve conter entre 6~32 caracteres que podem ser a-z, A-Z, especiais ou numéricos.");
        }

        } else {
            alert("Nenhuma conexão foi detectada");
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

            if (resultado.contains("cadastro_ok")) {
                alert("Cadastro realizado com sucesso...");

                Intent it = new Intent(CadastroActivity.this, MainActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            } else if (resultado.contains("cpf_ja_cadastrado")) {
                alert("CPF já está cadastrado");

            } else if (resultado.contains("email_ja_cadastrado")) {
                alert("Endereço de e-mail já está cadastrado");

            } else if (resultado.contains("error_system")) {
                alert("Ocorreu um erro");
            }

        }
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.rbtnIdoso:
                if (checked)
                    b1 = true;
                else
                    b1 = false;
                break;
            case R.id.rbtnDF:
                if (checked)
                    b2 = true;
                else
                    b2 = false;
                break;
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
