package br.com.vagotche.vagotcheapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;
import br.com.vagotche.vagotcheapp.Validações.ValidaCPF;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editNome, editCPF2, editEmail1, editPassword2, editPassword3;
    Button btnCancelar, btnRegistrar;
    CheckBox rdbIdoso, rdbDF;
    Boolean b1, b2;
    String url = "";
    String parametros = "";

    static final String TAG = "VagoTchê";

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

        Pattern passwordPat = Pattern.compile("(?=^.{6,32}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        Pattern emailPat = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+");

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
            complain("Nenhum campo pode estar vazio");

        } else if (!ValidaCPF.isCPF(cpf) == true) {
            complain("O CPF digitado está incorreto");

        } else if (!emailPat.matcher(email).matches()) {
            complain("O e-mail digitado está incorreto");

        } else if (!passwordPat.matcher(senha).matches()){
            complain(   "* A senha deve conter entre 6 à 32 caracteres;\n" +
                        "* Um caractere maiúsculo; \n" +
                        "* Um caractere especial;\n" +
                        "* Um caractere numérico.");

        } else {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Cadastro/CadastrarLogin";
            parametros = "nome=" + nome + "&cpf=" + cpf + "&email=" + email + "&senha=" + senha + "&idoso=" + b1 + "&df=" + b2;
            new SolicitaDados().execute(url);

            }

        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
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
                complain("Este CPF já está cadastrado");

            } else if (resultado.contains("email_ja_cadastrado")) {
                complain("Endereço de e-mail já está cadastrado");

            } else if (resultado.contains("error_system")) {
                complain("Ocorreu um erro. Por favor informe ao administrador do sistema.");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelar:
                finish();
                break;
            case R.id.btnRegistrar:
                registrar();

//                String token = FirebaseInstanceId.getInstance().getToken();
//                registerToken(token);

                //FirebaseMessaging.getInstance().subscribeToTopic("test");
                //alert("Token: " + FirebaseInstanceId.getInstance().getToken());


                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
