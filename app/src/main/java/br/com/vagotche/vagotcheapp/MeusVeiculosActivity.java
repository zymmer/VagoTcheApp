package br.com.vagotche.vagotcheapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meusveiculos);

        cdUsuario = getIntent().getIntExtra("id_usuario", 0);
        btnVoltar = (ImageView) findViewById(R.id.imvVoltarMeusVeiculos);
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
