package br.com.vagotche.vagotcheapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.vagotche.vagotcheapp.R;

/**
 * Created by guilherme on 17/09/17.
 */

public class MeusVeiculosActivity extends AppCompatActivity implements View.OnClickListener{

    //Variaveis
    int cdUsuario;
    ImageView btnVoltar;
    String url = "";
    String parametros = "";

    //ExpandableList
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meusveiculos);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarMeusVeiculos);
        btnVoltar.setOnClickListener(this);

        //ExpandableList
        buildList();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListViewMeusVeiculos);
        expandableListView.setAdapter(new ExpandableAdapter(MeusVeiculosActivity.this, listGroup, listData) {
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                alert("Group: " +groupPosition+ " Item: "+childPosition);
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                alert("Group(Expand): " +groupPosition);
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                alert("Group(Collapse): " +groupPosition);
            }
        });

        expandableListView.setGroupIndicator(getResources().getDrawable(R.mipmap.vehicle));

    }

    public void buildList(){
        listGroup = new ArrayList<String>();
        listData = new HashMap<String, List<String>>();

        getIntent().getStringArrayListExtra("veiculosArray");

        int x = 0;

        for(int i=0; i < getIntent().getStringArrayListExtra("veiculosArray").size(); i = i + 4) {

            //GROUP
            listGroup.add(getIntent().getStringArrayListExtra("veiculosArray").get(i));

            //CHILDREM
            List<String> auxList = new ArrayList<String>();
            auxList.add("Placa: " + getIntent().getStringArrayListExtra("veiculosArray").get(i+1));
            auxList.add("Ano-Fabricação: " + getIntent().getStringArrayListExtra("veiculosArray").get(i+2));
            auxList.add("Ano-Modelo: " + getIntent().getStringArrayListExtra("veiculosArray").get(i+3));

            listData.put(listGroup.get(x), auxList);
            x = x + 1;

        }

//        //GROUP
//            listGroup.add(getIntent().getStringArrayListExtra("veiculosArray").get(0));
//            listGroup.add("Grupo 2");
//            listGroup.add("Grupo 3");
//            listGroup.add("Grupo 4");
//
//        //CHILDREM
//        List<String> auxList = new ArrayList<String>();
//        auxList.add("Item 1");
//        auxList.add("Item 2");
//        auxList.add("Item 3");
//        auxList.add("Item 4");
//        listData.put(listGroup.get(0), auxList);
//
//        auxList = new ArrayList<String>();
//        auxList.add("Item 5");
//        auxList.add("Item 6");
//        auxList.add("Item 7");
//        auxList.add("Item 8");
//        listData.put(listGroup.get(1), auxList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvVoltarMeusVeiculos:
                finish();
                break;
        }
    }

}
