package br.com.vagotche.vagotcheapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by guilherme on 14/09/17.
 */

public class IrregularidadesActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView btnVoltar;

    private void alert(String s){
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irregularidades);

        btnVoltar = (ImageView) findViewById(R.id.imvVoltarIrregularidade);
        btnVoltar.setOnClickListener(this);
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
