package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CadastroVeiculoActivity extends AppCompatActivity implements View.OnClickListener{

    TextView txtmarca, txtano;
    EditText editplaca;
    Button btnCancelar, btnRegistrar;

    //teste AutoComplete
    List listMarcasModelos = new Veiculos().marcasModelos;
    List listAno = new Veiculos().ano;
    AutoCompleteTextView textView, textView2;

    String url = "";
    String parametros = "";

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);

        txtmarca = (TextView) findViewById(R.id.ACviewMarca);
        txtano = (TextView) findViewById(R.id.ACviewAno);
        editplaca = (EditText) findViewById(R.id.editPlaca);

        textView = (AutoCompleteTextView) findViewById(R.id.ACviewMarca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listMarcasModelos);
        textView.setThreshold(3);
        textView.setAdapter(adapter);

        textView2 = (AutoCompleteTextView) findViewById(R.id.ACviewAno);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listAno);
        textView2.setThreshold(3);
        textView2.setAdapter(adapter2);

        btnCancelar = (Button) findViewById(R.id.btnCancelar2);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar2);
        // Button listeners
        btnCancelar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
    }

    private void registrar() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            String marcaModelo = txtmarca.getText().toString();
            String placa = editplaca.getText().toString();
            String ano = txtano.getText().toString();
            Integer cdUsuario = getIntent().getExtras().getInt("id_usuario");

            if(marcaModelo.isEmpty() || placa.isEmpty() || ano.isEmpty()){
                alert("Nenhum campo pode estar vazio");
            }else {
                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/CadastroVeiculo/CadastrarVeiculo";

                parametros = "marcaModelo=" + marcaModelo + "&placa=" + placa + "&ano=" + ano + "&cdUsuario" + cdUsuario;

                new SolicitaDados().execute(url);
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
            alert(resultado);

//            if (resultado.contains("cadastro_ok")) {
//                alert("Registro realizado com sucesso...");
//
//                Intent it = new Intent(CadastroVeiculoActivity.this, MenuActivity.class);
//                startActivity(it);
//            } else if (resultado.contains("cpf_ja_cadastrado")) {
//                alert("CPF já está cadastrado");
//
//            } else if (resultado.contains("email_ja_cadastrado")) {
//                alert("Endereço de e-mail já está cadastrado");
//
//            } else if (resultado.contains("error_system")) {
//                alert("Ocorreu um erro");
//            }

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
