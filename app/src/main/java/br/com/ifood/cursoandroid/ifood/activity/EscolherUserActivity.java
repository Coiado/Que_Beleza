package br.com.ifood.cursoandroid.ifood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import br.com.ifood.cursoandroid.ifood.R;
import br.com.ifood.cursoandroid.ifood.R.layout;

public class EscolherUserActivity extends AppCompatActivity {

    private Button botaoEmpresa;
    private Button botaoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_escolher_user);

        botaoCliente = findViewById(R.id.buttonAcesso);
        botaoEmpresa = findViewById(R.id.buttonRegistro);

        botaoEmpresa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfiguracoesUsuarioActivity.class);
                intent.putExtra("type", "E");
                startActivity(intent);
            }
        });

        botaoCliente.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfiguracoesUsuarioActivity.class);
                intent.putExtra("type", "U");
                startActivity(intent);
            }
        });

    }

}
