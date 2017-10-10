package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ZonaAzulActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, txvTempo30, txvTempo1, txvTempo130, txvTempo2;
    Spinner spinnerPlaca, spinnerCidade, spinnerParquimetro;
    Double valor = 0.00;
    Button btnUtilizarCred;
    ImageView btnVoltar;
    String url = "";
    String parametros = "";

    Placa placa = new Placa();

    String[] cidades = {"Porto Alegre"};

    ArrayAdapter <String> adapter;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    //Formato de moeda
    DecimalFormatSymbols dfs = new DecimalFormatSymbols (new Locale("pt", "BR"));
    // Formato com sinal de menos -5.000,00
    //DecimalFormat df1 = new DecimalFormat ("#,##0.00", dfs);
    // Formato com parêntese (5.000,00)
    DecimalFormat df2 = new DecimalFormat ("#,##0.00;(#,##0.00)", dfs);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zona_azul);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);


        //Saldo
//        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditosZA);
//        seuSaldo.setText(getIntent().getStringExtra("saldoZA"));
//        btnVoltar = (ImageView) findViewById(R.id.imvVoltarZonaAzul);
//        btnVoltar.setOnClickListener(this);

//        String[] dadosPlacas = getIntent().getStringExtra("ArrayPlacas").split(",");
//        List<String> stringListPlacas = new ArrayList<String>(Arrays.asList(dadosPlacas));
//        //Spinner Placas
////        spinnerPlaca = (Spinner) findViewById(R.id.spinnerPlaca);
////        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringListPlacas);
////        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////        spinnerPlaca.setAdapter(adapter);

        //Spinner Cidade
        spinnerCidade = (Spinner) findViewById(R.id.spinnerCidade);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCidade.setAdapter(adapter);

        //alert(getIntent().getStringExtra("ArrayParquimetros"));
        String[] dadosParquimetros = getIntent().getStringExtra("ArrayParquimetros").split(",");
        List<String> stringListParquimetros = new ArrayList<String>(Arrays.asList(dadosParquimetros));
        //Spinner Parquimetro
        spinnerParquimetro = (Spinner) findViewById(R.id.spinnerParquimetro);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringListParquimetros);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerParquimetro.setAdapter(adapter);


        //spinnerPlaca.getSelectedItem(getIntent().getStringExtra("placa"));
//        spinnerCidade = (Spinner) findViewById(R.id.spinnerCidade);
//        spinnerParquimetro = (Spinner) findViewById(R.id.spinnerParquimetro);
//        txvTempo30 = (TextView) findViewById(R.id.txvTempo30);
//        txvTempo1 = (TextView) findViewById(R.id.txvTempo1);
//        txvTempo130 = (TextView) findViewById(R.id.txvTempo130);
//        txvTempo2 = (TextView) findViewById(R.id.txvTempo2);
//
//        btnUtilizarCred = (Button) findViewById(R.id.btnUtilizarCred);
//        btnVoltar = (ImageView) findViewById(R.id.imvVoltarZonaAzul);
//
//        // Button listeners
//        txvTempo30.setOnClickListener(this);
//        txvTempo1.setOnClickListener(this);
//        txvTempo130.setOnClickListener(this);
//        txvTempo2.setOnClickListener(this);
          //spinnerPlaca.setOnClickListener(this);
//        spinnerCidade.setOnClickListener(this);
//        spinnerParquimetro.setOnClickListener(this);
//        btnUtilizarCred.setOnClickListener(this);
//        btnVoltar.setOnClickListener(this);

    }

//    private Carro degenerateJSON(String data){
//
//        Carro carro = new Carro();
//        placasArray = new ArrayList<String>();
//
//        try{
//            JSONObject jo = new JSONObject(data);
//            JSONArray ja;
//
//            carro.setPlaca(jo.getString("plate"));
//            placasArray.add(jo.getString("dsPlaca"));
//
//            jo.put("placa", carro.getPlaca());
//
////            ja = jo.getJSONArray("placas");
////            for(int i = 0, tam = ja.length(); i < tam; i++){
////
////                Placa p = new Placa();
////                p.setPlaca(ja.getJSONObject(i).getString("placa"));
////
////                carro.getPlacas().add(p);
////            }
//
//            // APRESENTAÇÃO
//            Log.i("Script", "Placa: "+carro.getPlaca());
//            for(int i = 0, tam = carro.getPlacas().size(); i < tam; i++){
//                Log.i("Script", "Motor: "+carro.getPlacas().get(i));
//            }
//
//            alert(carro.toString());
//
//            // Spinner Placas
//            spinnerPlaca = (Spinner) findViewById(R.id.spinnerPlaca);
//            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placasArray);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinnerPlaca.setAdapter(adapter);
//
//            // APRESENTAÇÃO
//            Log.i("Script", "Placa: "+carro.getPlacas());
//
//        }
//        catch(JSONException e){ e.printStackTrace(); }
//
//        return(carro);
//    }

    private void UtilizarCredito() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Creditos/ComprarCreditos";
            parametros = "saldo=" + valor + "&cdUsuario=" + cdUsuario;

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

            //degenerateJSON(resultado);

            //alert(resultado);

            if (resultado.contains("credito_adquirido")) {
                alert("Créditos Adquiridos...");

                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                //String number = tm.getLine1Number();
                //alert("numero: " +number);
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("51997152881", null, "Você utilizou R$"+ valor +" reais do credVAGO", null, null);

                finish();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvTempo30:
                //valor = valor + 5;
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo1:
                //valor = valor + 10;
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo130:
                //valor = valor + 15;
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo2:
                //valor = 0.00;
                //valor.setText(df2.format(valor));
                break;
            case R.id.imvVoltarZonaAzul:
                finish();
                break;
            case R.id.btnUtilizarCred:
                if (valor >= 5){
                    UtilizarCredito();
                } else {
                    alert("Você deve informar um valor");
                }
                break;
        }
    }


}
