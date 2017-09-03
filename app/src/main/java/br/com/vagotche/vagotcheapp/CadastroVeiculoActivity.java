package br.com.vagotche.vagotcheapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.List;

public class CadastroVeiculoActivity extends AppCompatActivity {

    //teste AutoComplete
    List listMarcasModelos = new Veiculos().marcasModelos;
    List listAno = new Veiculos().ano;
    AutoCompleteTextView textView, textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_veiculo);

        textView = (AutoCompleteTextView) findViewById(R.id.ACviewMarca);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listMarcasModelos);
        textView.setThreshold(3);
        textView.setAdapter(adapter);

        textView2 = (AutoCompleteTextView) findViewById(R.id.ACviewAno);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listAno);
        textView2.setThreshold(3);
        textView2.setAdapter(adapter2);
    }
}
