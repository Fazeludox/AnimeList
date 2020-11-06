package com.example.myanimelist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SinPrivilegios extends AppCompatActivity {

    private String Usuario;
    private String Contra;
    private String IP;
    private String Puerto;
    private String BaseDatos;

    private Button ConsultaAnimesVistos;
    private Button ConsultaAnimesPendientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sin_privilegios);

        Bundle bundle = getIntent().getExtras();

        Usuario = bundle.getString("Usuario");
        Contra = bundle.getString("Contra");
        IP = bundle.getString("IP");
        Puerto = bundle.getString("Puerto");
        BaseDatos = bundle.getString("BaseDatos");

        ConsultaAnimesVistos = findViewById(R.id.consultaAV);
        ConsultaAnimesPendientes = findViewById(R.id.consultaPendientes);


        ConsultaAnimesVistos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinPrivilegios.this, TablaAnimesVistos.class);

                Bundle a = new Bundle();
                a.putString("IP", IP);
                a.putString("Puerto", Puerto);
                a.putString("BaseDatos", BaseDatos);
                a.putString("Usuario", Usuario);
                a.putString("Contra", Contra);

                i.putExtras(a);

                startActivity(i);
            }
        });

        ConsultaAnimesPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SinPrivilegios.this, TablaAnimesPendientes.class);

                Bundle a = new Bundle();
                a.putString("IP", IP);
                a.putString("Puerto", Puerto);
                a.putString("BaseDatos", BaseDatos);
                a.putString("Usuario", Usuario);
                a.putString("Contra", Contra);

                i.putExtras(a);

                startActivity(i);
            }
        });


    }
}
