package br.com.vagotche.vagotcheapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    ImageView btnVoltar;
    //ExpandableList
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregularidades);

        btnVoltar = (ImageView) findViewById(R.id.imvVoltarIrregularidade);
        btnVoltar.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imvVoltarIrregularidade:
                finish();
                break;
        }
    }

}
