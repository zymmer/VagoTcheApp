package br.com.vagotche.vagotcheapp;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ZonaAzulActivity extends AppCompatActivity implements View.OnClickListener {

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, txvTempo30, txvTempo1, txvTempo130, txvTempo2, parquimetro, cidade;
    Spinner spinnerPlaca, spinnerCidade, spinnerParquimetro;
    Double valor = 0.00;
    int tempo = 0;
    Button btnUtilizarCred;
    ImageView btnVoltar;
    String url = "", parametros = "", saldoExtra;
    Placa placa = new Placa();

    String[] cidades = {"Porto Alegre"};

    ArrayAdapter <String> adapter;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    Date data = new Date();

    //Formato Data completa
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    //Formato Hora
    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

    //Formato de moeda
    DecimalFormatSymbols dfs = new DecimalFormatSymbols (new Locale("pt", "BR"));
    // Formato com sinal de menos -5.000,00
    //DecimalFormat df1 = new DecimalFormat ("#,##0.00", dfs);
    // Formato com parêntese (5.000,00)
    DecimalFormat df2 = new DecimalFormat ("#,##0.00;(#,##0.00)", dfs);

    // Debug tag, for logging
    static final String TAG = "VagoTchê";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zona_azul);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        //Saldo
        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditosZA);
        seuSaldo.setText("R$" + getIntent().getStringExtra("saldoZA"));
        saldoExtra = getIntent().getStringExtra("saldoZA");

        // Spinner Placas
        spinnerPlaca = (Spinner) findViewById(R.id.spinnerPlaca);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getIntent().getStringArrayListExtra("placasArray"));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlaca.setAdapter(adapter);

//        //Spinner Parquimetro
//        spinnerParquimetro = (Spinner) findViewById(R.id.spinnerParquimetro);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getIntent().getStringArrayListExtra("parquimetrosArray"));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerParquimetro.setAdapter(adapter);
        //Saldo
        parquimetro = (TextView) findViewById(R.id.txtParquimetro);
        parquimetro.setText(getIntent().getStringExtra("parquimetro"));

//        //Spinner Cidade
//        spinnerCidade = (Spinner) findViewById(R.id.spinnerCidade);
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cidades);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCidade.setAdapter(adapter);
        cidade = (TextView) findViewById(R.id.txtCidade);
        cidade.setText("Porto Alegre");


        //Views
        txvTempo30 = (TextView) findViewById(R.id.txvTempo30);
        txvTempo1 = (TextView) findViewById(R.id.txvTempo1);
        txvTempo130 = (TextView) findViewById(R.id.txvTempo130);
        txvTempo2 = (TextView) findViewById(R.id.txvTempo2);
        btnUtilizarCred = (Button) findViewById(R.id.btnUtilizarCred);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarZonaAzul);

        // Button listeners
        txvTempo30.setOnClickListener(this);
        txvTempo1.setOnClickListener(this);
        txvTempo130.setOnClickListener(this);
        txvTempo2.setOnClickListener(this);
        btnUtilizarCred.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);

    }

    private void UtilizarCredito() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        saldoExtra = saldoExtra.replace("R$", "");
        saldoExtra = saldoExtra.replace(",", ".");

        //alert("|" + parquimetro.getText() +"|");

        if (networkInfo != null && networkInfo.isConnected()) {

            //String placa = spinnerPlaca.getSelectedItem().toString();
            //String cidade = spinnerCidade.getSelectedItem().toString();
            //String parquimetro = spinnerParquimetro.getSelectedItem().toString();

            if (spinnerPlaca.getSelectedItem() == "" || spinnerPlaca.getSelectedItem() == null) {

                complain("Nenhum veículo registrado foi encontrado");

            } else if (Double.parseDouble(saldoExtra) < valor) {

                complain("Saldo indisponível");
                
            } else {

                //.toString().replaceAll("\\s+$", "")
                String park = "Av. Osvaldo Aranha, n 374";

                url = "http://fabrica.govbrsul.com.br/vagotche/index.php/ZonaAzul/PagarZonaAzul";
                parametros = "cdUsuario=" + cdUsuario + "&placa=" + spinnerPlaca.getSelectedItem().toString() + "&cidade=" + cidade.getText() +
                        "&parquimetro=" + park + "&valorUtilizado=" + valor;

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
            //            alert(parametros);
//            alert(resultado);

        }

        @Override
        protected void onPostExecute(String resultado) {

//            alert(parametros);
            //alert(resultado);

            //Data Atual do Celular
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);

            Date data_atual = cal.getTime();
            String data_completa = dateFormat.format(data_atual);
            cal.add(Calendar.MINUTE, tempo);
            Date data_atual_somado = cal.getTime();
            String hora_atual = dateFormat_hora.format(data_atual_somado);

            if (resultado.contains("Pagamento_efetuado")) {
//                        "" +
//                        "para utilização do parquímetro " + spinnerParquimetro.getSelectedItem().toString()+
//                                " Sua vaga estará disponível até às " + hora_atual);

                showNotification("Confirmação de Reserva","Reserva efetuada para utilização do parquímetro (" + parquimetro.getText().toString()+
                        "). Sua vaga estará disponível até às " + hora_atual + ".");
                //TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                //String number = tm.getLine1Number();
                //alert("numero: " +number);
                //SmsManager smsManager = SmsManager.getDefault();
                //smsManager.sendTextMessage("51997152881", null, "VagoTchê: Utilizado R$"+ df2.format(valor) +" do credVAGO." + " Data: " + data_completa, null, null);

                //complain("Reserva efetuada com sucesso...");
                MenuActivity menuActivity = new MenuActivity();
                menuActivity.startChronometer();
                finish();

            } else if (resultado.contains("Crédito_insuficiente")) {
                alert("Você não possui crédito suficiente para reservar esta vaga");
            }

        }
    }

    private void showNotification(String title, String message) {

//        Intent i = new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setSmallIcon(R.drawable.cast_ic_stop_circle_filled_white);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.vagotcheestacionamento40x40px);
        builder.setLargeIcon(bm);
        //.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    @Override
    public void onClick(View v) {
        // Is the view now checked?
        //boolean checked = ((CheckBox) v).isChecked();

        // Check which textview was clicked
        switch (v.getId()) {
            case R.id.txvTempo30:
                txvTempo30.setBackgroundColor(Color.DKGRAY);
                txvTempo1.setBackgroundColor(0x00000000);
                txvTempo130.setBackgroundColor(0x00000000);
                txvTempo2.setBackgroundColor(0x00000000);
                valor = 0.50;
                tempo = 30;
                //alert("Valor: " +valor+ " Tempo: "+tempo);
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo1:
                txvTempo1.setBackgroundColor(Color.DKGRAY);
                txvTempo30.setBackgroundColor(0x00000000);
                txvTempo130.setBackgroundColor(0x00000000);
                txvTempo2.setBackgroundColor(0x00000000);
                valor = 1.00;
                tempo = 60;
                //alert("Valor: " +valor+ " Tempo: "+tempo);
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo130:
                txvTempo130.setBackgroundColor(Color.DKGRAY);
                txvTempo30.setBackgroundColor(0x00000000);
                txvTempo1.setBackgroundColor(0x00000000);
                txvTempo2.setBackgroundColor(0x00000000);
                valor = 1.50;
                tempo = 90;
                //alert("Valor: " +valor+ " Tempo: "+tempo);
                //valor.setText(df2.format(valor));
                break;
            case R.id.txvTempo2:
                txvTempo2.setBackgroundColor(Color.DKGRAY);
                txvTempo30.setBackgroundColor(0x00000000);
                txvTempo1.setBackgroundColor(0x00000000);
                txvTempo130.setBackgroundColor(0x00000000);
                valor = 2.00;
                tempo = 120;
                //alert("Valor: " +valor+ " Tempo: "+tempo);
                //valor.setText(df2.format(valor));
                break;
            case R.id.imvVoltarZonaAzul:
                finish();
                break;
            case R.id.btnUtilizarCred:
                if (valor >= 0.50){
                    UtilizarCredito();
                } else {
                    complain("Você deve informar um valor/tempo.");
                }
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


}
