package br.com.vagotche.vagotcheapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.vagotche.vagotcheapp.util.IabHelper;
import br.com.vagotche.vagotcheapp.util.IabResult;
import br.com.vagotche.vagotcheapp.util.Inventory;
import br.com.vagotche.vagotcheapp.util.Purchase;

public class CreditosActivity extends AppCompatActivity implements View.OnClickListener {

    private IabHelper mHelper;

    // CONSTANTS
    private static final String[] PRODUCT_IDS = new String[]{"credito5",
            "credito10", "credito15"};
    private static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiI/sOn+Gs8sr/zhQn0zR0J7HsQJ8Fnrrvu4lRXwT5vsq7Zhnv6gg7/kr0TZznQrs45SQOyRriUW4VIZ6h4vbNbbvq/CxeTE27zPBXHemaoHMP7hEbmLe8J5HWnvxuPhxMF4iXfz0+z777h7wh8phgITMaYi6Ek7jeCY5QeSJ1qEjUP9jzLsNgPBb4sahb5N52xiIfTFnUe6rziLG4pdZqK2vzk7pFYt5XErf+wikPtkFl4NaNdki8qnb3xoE9YYmn1MzhwKjc1h2luRNRTDfVrj/Zj6neXtzuzE7TI2AHgwFMU+rc9U/41/J/Jc8pjKXNSFWy4Pji3TmxTJBU3H14wIDAQAB";

    //Variaveis
    int cdUsuario;
    TextView seuSaldo, valor, creditos5, creditos10, creditos15;
    Double saldo = 0.00;
    Button btnComprar, btnLimparCred;
    ImageView btnVoltar;
    String url = "";
    String parametros = "";

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


    // VAR. LISTENERS
    private IabHelper.QueryInventoryFinishedListener mQueryInventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener(){
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            Log.i("Script", "onQueryInventoryFinished()");

            if(result.isFailure()){
                Log.i("Script", "onQueryInventoryFinished() : FAIL : "+result);
            }
            else if(inv != null){
                for(int i = 0; i < PRODUCT_IDS.length; i++){
                    if(inv.hasDetails(PRODUCT_IDS[i])){
                        Log.i("Script", inv.getSkuDetails(PRODUCT_IDS[i]).getSku().toUpperCase());
                        Log.i("Script", "Sku: "+inv.getSkuDetails(PRODUCT_IDS[i]).getSku());
                        Log.i("Script", "Title: "+inv.getSkuDetails(PRODUCT_IDS[i]).getTitle());
                        Log.i("Script", "Type: "+inv.getSkuDetails(PRODUCT_IDS[i]).getType());
                        Log.i("Script", "Price: "+inv.getSkuDetails(PRODUCT_IDS[i]).getPrice());
                        Log.i("Script", "Description: "+inv.getSkuDetails(PRODUCT_IDS[i]).getDescription());
                        Log.i("Script", "Status purchase: "+(inv.hasPurchase(PRODUCT_IDS[i]) ? "COMPRADO" : "NÃO COMPRADO"));
                        enableImageView(inv.hasPurchase(PRODUCT_IDS[i]), PRODUCT_IDS[i]);
                        Log.i("Script", "-------------------------------------");
                    }
                }
            }
        }
    };


    private IabHelper.OnIabPurchaseFinishedListener mIabPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener(){
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase info) {
            Log.i("Script", "onIabPurchaseFinished()");

            if(result.isFailure()){
                Log.i("Script", "onIabPurchaseFinished() : FAIL : "+result);
                return;
            }
            else if(info.getSku().equalsIgnoreCase(PRODUCT_IDS[0])){
                //mHelper.consumeAsync(info, mConsumeFinishedListener);
            }

            Log.i("Script", info.getSku().toUpperCase());
            Log.i("Script", "Order ID: "+info.getOrderId());
            Log.i("Script", "DeveloperPayload: "+info.getDeveloperPayload());
        }
    };

    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener(){

        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.i("Script", "onConsumeFinished()");

            if(result.isFailure()){
                Log.i("Script", "onConsumeFinished() : FAIL : "+result);
            }
            else{
                Log.i("Script", "onConsumeFinished("+purchase.getSku()+") : SUCCESS");
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditos);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        //Saldo
        seuSaldo = (TextView) findViewById(R.id.viewSaldoCreditos);
        seuSaldo.setText("R$" + getIntent().getStringExtra("saldo"));

        // Get IDs
        creditos5 = (TextView) findViewById(R.id.creditos5);
        creditos10 = (TextView) findViewById(R.id.creditos10);
        creditos15 = (TextView) findViewById(R.id.creditos15);
        valor = (TextView) findViewById(R.id.CampoValor);
        btnComprar = (Button) findViewById(R.id.btnComprarCred);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarCredito);
        btnLimparCred = (Button) findViewById(R.id.btnLimparCred);
        // Button listeners
        creditos5.setOnClickListener(this);
        creditos10.setOnClickListener(this);
        creditos15.setOnClickListener(this);
        btnComprar.setOnClickListener(this);
        btnVoltar.setOnClickListener(this);
        btnLimparCred.setOnClickListener(this);

        mHelper = ((MyApplication) getApplication()).getmHelper();

        if(mHelper == null){
            mHelper = new IabHelper(CreditosActivity.this, base64EncodedPublicKey);
            ((MyApplication) getApplication()).setmHelper(mHelper);

            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    Log.i("Script", "onIabSetupFinished()");

                    if(result.isFailure()){
                        Log.i("Script", "onIabSetupFinished() : FAIL : "+result);
                    }
                    else{
                        Log.i("Script", "onIabSetupFinished() : SUCCESS");

                        List<String> productsIds = new ArrayList<String>();
                        for(int i = 0; i < PRODUCT_IDS.length; i++){
                            productsIds.add(PRODUCT_IDS[i]);
                        }

                        //mHelper.queryInventoryAsync(true, productsIds, mQueryInventoryFinishedListener);
                    }
                }
            });
        }

    }

//    private void ComprarCredito() {
//
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()){
//
//            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/Creditos/ComprarCreditos";
//
//            parametros = "saldo=" + saldo + "&cdUsuario=" + cdUsuario;
//
//            new SolicitaDados().execute(url);
//        }
//
//    }

//    private class SolicitaDados extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... urls) {
//
//            return ConexaoApp.postDados(urls[0], parametros);
//
//        }
//
//        @Override
//        protected void onPostExecute(String resultado) {
//
//            //Data Atual do Celular
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(data);
//            Date data_atual = cal.getTime();
//
//            String data_completa = dateFormat.format(data_atual);
//            String hora_atual = dateFormat_hora.format(data_atual);
//
//            if (resultado.contains("credito_adquirido")) {
//                alert("Créditos Adquiridos...");
//
//                //TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//                //String number = tm.getLine1Number();
//                //alert("numero: " +number);
//                //SmsManager smsManager = SmsManager.getDefault();
//                //smsManager.sendTextMessage("51997152881", null, "VagoTchê: Adquirido R$"+ df2.format(saldo) +" para o credVAGO." + " Data: " + data_completa, null, null);
//
//                finish();
//            }
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("Script", "onActivityResult("+requestCode+")");

        if(requestCode == 1002 && resultCode == RESULT_OK){
            if(mHelper != null && !mHelper.handleActivityResult(requestCode, resultCode, data)){
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

        if(mHelper != null){
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        mHelper = null;
        ((MyApplication) getApplication()).setmHelper(null);
    }




    // UTIL
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }


    private void enableImageView(boolean status, String productId){
        if(status){
            if(productId.equalsIgnoreCase(PRODUCT_IDS[0])){
                creditos5.setVisibility(View.VISIBLE);
            }
            else if(productId.equalsIgnoreCase(PRODUCT_IDS[1])){
                creditos10.setVisibility(View.VISIBLE);
            }
            else if(productId.equalsIgnoreCase(PRODUCT_IDS[2])){
                creditos15.setVisibility(View.VISIBLE);
            }
        }
    }

    // LISTENER
    public void buy(View view) throws IabHelper.IabAsyncInProgressException {
        Log.i("Script", "buy()");

        if(mHelper == null){
            return;
        }

        if(view.getId() == R.id.creditos5){
            mHelper.launchPurchaseFlow(CreditosActivity.this, PRODUCT_IDS[0], 1002, mIabPurchaseFinishedListener, "token-"+randInt(1000, 9999));
        } else if(view.getId() == R.id.creditos10){
            mHelper.launchPurchaseFlow(CreditosActivity.this, PRODUCT_IDS[1], 1002, mIabPurchaseFinishedListener, "token-"+randInt(1000, 9999));
        } else if(view.getId() == R.id.creditos15){
            mHelper.launchPurchaseFlow(CreditosActivity.this, PRODUCT_IDS[2], 1002, mIabPurchaseFinishedListener, "token-"+randInt(1000, 9999));
        }

    }


    public void bought(View view) throws IabHelper.IabAsyncInProgressException {
        Log.i("Script", "bought()");

        if(mHelper == null){
            return;
        }

        mHelper.queryInventoryAsync(mQueryInventoryFinishedListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.creditos5:
                saldo = saldo + 5;
                valor.setText(df2.format(saldo));
                break;
            case R.id.creditos10:
                saldo = saldo + 10;
                valor.setText(df2.format(saldo));
                break;
            case R.id.creditos15:
                saldo = saldo + 15;
                valor.setText(df2.format(saldo));
                break;
            case R.id.btnLimparCred:
                saldo = 0.00;
                valor.setText(df2.format(saldo));
                break;
            case R.id.imvVoltarCredito:
                finish();
                break;
            case R.id.btnComprarCred:
                if (saldo >= 5){
                //ComprarCredito();
                } else {
                    alert("Você deve informar um valor");
                }
                break;
        }
    }


}
