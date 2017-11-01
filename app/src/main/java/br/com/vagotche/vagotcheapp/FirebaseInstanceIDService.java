package br.com.vagotche.vagotcheapp;

/**
 * Created by guilherme on 29/10/17.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

//import okhttp3.FormBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    String url = "";
    String parametros = "";

    //alerta
    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        //int cdUsuario = 0;
        registerToken(token);
    }

    public void registerToken(String token) {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            url = "http://fabrica.govbrsul.com.br/vagotche/register.php";
            parametros = "Token=" + token;

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

            alert(resultado);

        }
    }

//    public static void registerToken(String token) {
//
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("Token",token)
//                //.add("cdUsuario", String.valueOf(cdUsuario))
//                .build();
//
//        Request request = new Request.Builder()
//                .url("http://fabrica.govbrsul.com.br/vagotche/register.php")
//                .post(body)
//                .build();
//
//        try {
//            client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}