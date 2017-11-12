package br.com.vagotche.vagotcheapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CadastroVeiculoActivity extends AppCompatActivity implements View.OnClickListener{

    //Variaveis
    int cdUsuario;
    TextView txtmarca, txtano, txtcor;
    EditText editplaca;
    Button btnLocalizar,btnCancelar, btnRegistrar, btnAlterar;
    List listMarcasModelos = new Veiculos().marcasModelos;
    List listAno = new Veiculos().ano;
    String url = "";
    String parametros = "";
    ProgressDialog progressDialog;
    Carro carro = new Carro();

    // Debug tag, for logging
    static final String TAG = "VagoTchê";

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

        txtmarca = (TextView) findViewById(R.id.TxtviewMarca);
        txtano = (TextView) findViewById(R.id.TxtviewAno);
        txtcor = (TextView) findViewById(R.id.TxtviewCor);

        //buttons
        btnLocalizar = (Button) findViewById(R.id.btnLocalizarVeic);
        btnCancelar = (Button) findViewById(R.id.btnCancelarVeic);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarVeic);
        btnAlterar = (Button) findViewById(R.id.btnAlterarVeic);
        // Button listeners
        btnLocalizar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnRegistrar.setOnClickListener(this);
        btnAlterar.setOnClickListener(this);
    }

    private Carro degenerateJSON(String data){

        try{
            JSONObject jo = new JSONObject(data);
            //JSONArray ja;

            carro.setPlaca(jo.getString("plate"));
            carro.setModelo(jo.getString("model"));
            carro.setAnoModelo(jo.getString("model_year"));
            carro.setAno(jo.getString("year"));
            carro.setCor(jo.getString("color"));

            jo.put("placa", carro.getPlaca());
            jo.put("modelo", carro.getModelo());
            jo.put("anoModelo", carro.getAnoModelo());
            jo.put("ano", carro.getAno());
            jo.put("cor", carro.getCor());

            txtmarca.setText(carro.getModelo());
            txtano.setText("Ano: " + carro.getAno() + " Modelo: " + carro.getAnoModelo());
            txtcor.setText(carro.getCor());

            // APRESENTAÇÃO
            Log.i("Script", "Placa: "+carro.getPlaca());
            Log.i("Script", "Modelo: "+carro.getModelo());
            Log.i("Script", "ModeloAno: "+carro.getAnoModelo());
            Log.i("Script", "Ano: "+carro.getAno());
            Log.i("Script", "Cor: "+carro.getCor());

        }
        catch(JSONException e){ e.printStackTrace(); }

        return(carro);
    }


    private void localizarVeiculo() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Pattern placaPat = Pattern.compile("[a-zA-Z]{3,3}\\d{4,4}");

        if (networkInfo != null && networkInfo.isConnected()) {

            String placa = editplaca.getText().toString();

            if(placaPat.matcher(placa).matches()){

                progressDialog = ProgressDialog.show(CadastroVeiculoActivity.this, "Por favor aguarde",
                        "Procurando Veículo...");

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/VerificaPlacaSinesp/VerificaPlacaSinesp";
            parametros = "placa=" + placa;
            new SolicitaDados().execute(url);

            }   else {
                complain("Placa incorreta. Ex.: ABC1234");
            }

        } else {
            alert("Nenhuma conexão de rede foi detectada");
        }
    }

    private void registrarVeiculo() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            String marcaModelo = txtmarca.getText().toString();
            String placa = editplaca.getText().toString();

            //Pattern pattern = Pattern.compile("[A-Z]{3}-[0-9]{4}");
            Pattern pattern = Pattern.compile("[a-zA-Z]{3,3}\\d{4,4}");
            Matcher matcher = pattern.matcher(placa);

            if(matcher.find() ){

                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/CadastroVeiculo/CadastrarVeiculo";
                parametros = "marcaModelo=" + marcaModelo + "&placa=" + placa + "&anoFabricacao=" + carro.getAno() +
                        "&anoModelo=" + carro.getAnoModelo() + "&cdUsuario=" + cdUsuario;

                new SolicitaDados().execute(url);

            }else {
                complain("Placa incorreta");
            }

        } else {
            alert("Nenhuma conexão foi detectada");
        }
    }

    private void verificarMeusVeiculos() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/MeusVeiculos/VerificarVeiculos";

            parametros = "cdUsuario=" + cdUsuario;

            new SolicitaDados().execute(url);
        } else {
            alert("Nenhuma conexão de rede foi detectada");
        }
    }

    private class SolicitaDados extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return ConexaoApp.postDados(urls[0], parametros);

        }

        @Override
        protected void onPostExecute(String resultado) {

            progressDialog.dismiss(); // for close the dialog bar.

            degenerateJSON(resultado);

            if (resultado.contains("veiculo_registrado")) {

                alert("Veículo registrado com sucesso.");

                //finish();
            } else if (resultado.contains("placa_ja_cadastrada")) {

                complain("Este veículo já está cadastrado no sistema.");

            } else if (resultado.contains("error_system")) {

                complain("Ocorreu um erro desconhecido. Por favor informe o suporte do sistema.");
            }

            String[] dados = resultado.split(",");

            if (resultado.contains("verifica_meusveiculos_ok")){
                Intent it = new Intent(CadastroVeiculoActivity.this, MeusVeiculosActivity.class);
                it.putExtra("id_usuario", cdUsuario);
                it.putExtra("marcaModelo", dados[1]);
                it.putExtra("placa", dados[2]);
                it.putExtra("ano", dados[3]);
                startActivity(it);
            } else if (resultado.contains("nao_ha_veiculo_cadastrado")){
                Intent it = new Intent(CadastroVeiculoActivity.this, MeusVeiculosActivity.class);
                startActivity(it);
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLocalizarVeic:
                localizarVeiculo();
                break;
            case R.id.btnCancelarVeic:
                finish();
                break;
            case R.id.btnRegistrarVeic:
                registrarVeiculo();
                break;
            case R.id.btnAlterarVeic:
                verificarMeusVeiculos();
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

//    @Override
//    protected void onPause() {
//        super.onPause();
//        finish();
//    }


}
