package com.example.myanimelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Loguearse extends AppCompatActivity {

    static public String Usuario;
    static public String Contra;
    static public String IP;
    static public String Puerto;
    static public String BaseDatos;

    static public String PrivilegiosU;
    static public String PrivilegiosC;
    RelativeLayout Imagen;
    Handler handler = new Handler();
    Handler handler2 = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Imagen.setVisibility(View.VISIBLE);

        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {

            Imagen.setVisibility(View.GONE);

        }
    };
    private EditText txtUsuario;
    private EditText txtContraseña;
    private Button Conectar;
    private Boolean A;
    private Boolean I;
    private Connection conexionSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loguearse);

        Imagen = findViewById(R.id.Imagen);

        Bundle bundle = getIntent().getExtras();

        Usuario = bundle.getString("Usuario");
        Contra = bundle.getString("Contra");
        IP = bundle.getString("IP");
        Puerto = bundle.getString("Puerto");
        BaseDatos = bundle.getString("BaseDatos");


        txtUsuario = findViewById(R.id.username);
        txtContraseña = findViewById(R.id.password);

        Conectar = findViewById(R.id.login);


        Conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                A = false;
                I = false;

                PrivilegiosU = txtUsuario.getText().toString();
                PrivilegiosC = txtContraseña.getText().toString();

                Toast.makeText(getApplicationContext(), "Conectando...", Toast.LENGTH_SHORT).show();

                new ProbandoPrivilegios().execute();

            }
        });

    }

    private class ProbandoPrivilegios extends AsyncTask<Void, Void, Void> {
        @Override
        public Void doInBackground(Void... arg0) {
            try {

                if (conexionSQL == null) {
                    DriverManager.setLoginTimeout(4);

                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM Cuentas WHERE Usuarios='" + PrivilegiosU + "' AND Contra='" + PrivilegiosC + "'";
                ResultSet rs = st.executeQuery(SQL);

                if (rs.next()) {
                    handler.postDelayed(runnable, 10);
                    A = true;
                    Thread.sleep(1000);
                } else {
                    I = true;
                    handler.postDelayed(runnable2, 10);
                    Thread.sleep(250);
                }

            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error en la conexion a la base de datos", Toast.LENGTH_LONG).show();
                    }
                });

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (A) {
                A = false;


                Intent i = new Intent(Loguearse.this, Privilegios.class);

                Bundle a = new Bundle();
                a.putString("IP", IP);
                a.putString("Puerto", Puerto);
                a.putString("BaseDatos", BaseDatos);
                a.putString("Usuario", Usuario);
                a.putString("Contra", Contra);

                i.putExtras(a);

                startActivity(i);


            } else {
                AlertaPersonalizada cdd = new AlertaPersonalizada(Loguearse.this);
                cdd.getWindow();
                cdd.show();

            }
        }
    }
}
