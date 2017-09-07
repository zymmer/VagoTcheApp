package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class ConfiguracaoAlertasActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnCancelar, btnSalvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_alertas);

        btnCancelar = (Button) findViewById(R.id.btnCancelarConf);
        btnSalvar = (Button) findViewById(R.id.btnSalvarConf);

        // Button listeners
        btnCancelar.setOnClickListener(this);
        btnSalvar.setOnClickListener(this);
    }

    private void salvar() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            String nome = editNome.getText().toString();
            String cpf = editCPF2.getText().toString();
            String email = editEmail1.getText().toString();
            String senha = editPassword2.getText().toString();


            if(nome.isEmpty() || cpf.isEmpty() || email.isEmpty() || senha.isEmpty()){
                alert("Nenhum campo pode estar vazio");
            }else {
                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Cadastro/CadastrarLogin";

                parametros = "nome=" + nome + "&cpf=" + cpf + "&email=" + email + "&senha=" + senha;

                new CadastroActivity.SolicitaDados().execute(url);
            }

        } else {
            alert("Nenhuma conexão foi detectada");
        }
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.alerta1:
                if (checked)
                // Put some meat on the sandwich
            else
                // Remove the meat
                break;
            case R.id.alerta2:
                if (checked)
                // Cheese me
            else
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelarConf:
                finish();
                break;
            case R.id.btnSalvarConf:
                salvar();
                break;
        }
    }

}
