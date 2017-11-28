package br.com.vagotche.vagotcheapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guilherme on 14/09/17.
 */

public class IrregularidadesActivity extends AppCompatActivity implements View.OnClickListener{

    int cdUsuario;
    ImageView btnVoltar;
    Button btnMinhasIrregularidades;
    //ExpandableList
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    // Debug tag, for logging
    static final String TAG = "VagoTchê";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregularidades);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);

        //buttons
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarIrregularidade);
        btnMinhasIrregularidades = (Button) findViewById(R.id.btnMinhasIrregularidades);
        // Button listeners
        btnVoltar.setOnClickListener(this);
        btnMinhasIrregularidades.setOnClickListener(this);

        //ExpandableList
        buildList();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewIrregularidades);
        expandableListView.setAdapter(new ExpandableAdapter(IrregularidadesActivity.this, listGroup, listData) {
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //alert("Group: " +groupPosition+ " Item: "+childPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //alert("Group(Expand): " +groupPosition);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //alert("Group(Collapse): " +groupPosition);
            }
        });

        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.icon_group));

    }

    public void buildList(){
        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<String>>();

        //GROUP
        listGroup.add("Veículo sem ticket");
        listGroup.add("Exceder o tempo limite pago");
        listGroup.add("Ultrapassar o limite para permanecer na vaga(2 horas)");
        listGroup.add("Utilização indevida da vaga para DF ou idosos");

        //CHILDREM
        List<String> auxList = new ArrayList<String>();
        auxList.add("Severidade: Grave");
        auxList.add("Pontos na carteira: 5");
        auxList.add("Valor da Multa: R$195,23");
        listData.put(listGroup.get(0), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Severidade: Grave");
        auxList.add("Pontos na carteira: 5");
        auxList.add("Valor da Multa: R$195,23");
        listData.put(listGroup.get(1), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Severidade: Grave");
        auxList.add("Pontos na carteira: 5");
        auxList.add("Valor da Multa: R$195,23");
        listData.put(listGroup.get(2), auxList);

        auxList = new ArrayList<String>();
        auxList.add("Severidade: Gravíssima");
        auxList.add("Pontos na carteira: 7");
        auxList.add("Valor da Multa: R$293,47");
        listData.put(listGroup.get(3), auxList);

    }

    private void verificarMinhasIrregularidades() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

//            url = "http://fabrica.govbrsul.com.br/vagotche/index.php/MeusVeiculos/VerificarVeiculos";
//
//            parametros = "cdUsuario=" + cdUsuario;

//            new SolicitaDados().execute(url);

            Intent it = new Intent(IrregularidadesActivity.this, MeusVeiculosActivity.class);
            it.putExtra("id_usuario", cdUsuario);
            startActivity(it);


        } else {
            complain("Sem conexão com a Internet. O Wi-Fi ou os dados da rede celular devem estar ativos. Tente novamente.");
        }
    }

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
//            progressDialog.dismiss(); // for close the dialog bar.
//
//            degenerateJSON(resultado);
//
//            if (resultado.contains("data_not_found")) {
//
//                alert("Veículo não localizado.");
//
//                //finish();
//
//            } else if (resultado.contains("veiculo_registrado")) {
//
//                alert("Veículo registrado com sucesso.");
//
//                //finish();
//            } else if (resultado.contains("placa_ja_cadastrada")) {
//
//                complain("Este veículo já está cadastrado no sistema.");
//
//            } else if (resultado.contains("error_system")) {
//
//                complain("Ocorreu um erro desconhecido. Por favor informe o suporte do sistema.");
//            }
//
//
//        }
//    }

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
            case R.id.imvVoltarIrregularidade:
                finish();
                break;
            case R.id.btnMinhasIrregularidades:
                verificarMinhasIrregularidades();
                break;
        }
    }

}
