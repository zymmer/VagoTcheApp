package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

public class CadastroVeiculoActivity extends AppCompatActivity implements View.OnClickListener{

    //Variaveis
    int cdUsuario;
    AutoCompleteTextView txtmarca, txtano;
    EditText editplaca;
    Button btnCancelar, btnRegistrar;
    List listMarcasModelos = new Veiculos().marcasModelos;
    List listAno = new Veiculos().ano;
    String url = "";
    String parametros = "";

    //Alerta
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        editplaca = (EditText) findViewById(R.id.editPlaca);

        txtmarca = (AutoCompleteTextView) findViewById(R.id.ACviewMarca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listMarcasModelos);
        txtmarca.setThreshold(3);
        txtmarca.setAdapter(adapter);

        txtano = (AutoCompleteTextView) findViewById(R.id.ACviewAno);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listAno);
        txtano.setThreshold(3);
        txtano.setAdapter(adapter2);

        btnCancelar = (Button) findViewById(R.id.btnCancelarVeic);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarVeic);
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

            alert("Id= " + cdUsuario);

            if(marcaModelo.isEmpty() || placa.isEmpty() || ano.isEmpty()){
                alert("Nenhum campo pode estar vazio");
            }else {
                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/CadastroVeiculo/CadastrarVeiculo";

                parametros = "marcaModelo=" + marcaModelo + "&placa=" + placa + "&ano=" + ano + "&cdUsuario=" + cdUsuario;

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
            //alert(resultado);

            if (resultado.contains("veiculo_registrado")) {
                alert("Veículo registrado com sucesso...");

                Intent it = new Intent(CadastroVeiculoActivity.this, MenuActivity.class);
                startActivity(it);
            } else if (resultado.contains("placa_ja_cadastrada")) {
                alert("A placa informada já está cadastrada");

            } else if (resultado.contains("error_system")) {
                alert("Ocorreu um erro");
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelarVeic:
                finish();
                break;
            case R.id.btnRegistrarVeic:
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
