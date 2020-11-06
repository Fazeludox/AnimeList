package com.example.myanimelist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;

public class PruebaBD extends AppCompatActivity {

    RelativeLayout CapaDatos;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            CapaDatos.setVisibility(View.VISIBLE);

        }
    };
    /* Botones */
    private Button PruebaBD;
    private Button Confirmar;
    private Switch DatosBD;
    /* Datos para la conexion a la base de datos */
    private EditText txtIP;
    private EditText txtPuerto;
    private EditText txtBaseDatos;
    private EditText txtUsuario;
    private EditText txtContra;
    /* String de almacenamientos*/
    private String IP;
    private String Puerto;

    /* Las capas lineas de la BD */
    private String BaseDatos;
    private String Usuario;
    private String Contra;
    private LinearLayout MostrarDatos;
    /* Conexion Based de datos */
    private Connection conexionSQL;
    private Boolean Error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebabd);

        CapaDatos = findViewById(R.id.CapaDatos);
        handler.postDelayed(runnable, 2000);

        txtIP = findViewById(R.id.txtIP);
        txtPuerto = findViewById(R.id.txtPuerto);
        txtBaseDatos = findViewById(R.id.txtNameBD);
        txtUsuario = findViewById(R.id.editUser);
        txtContra = findViewById(R.id.editPassword);

        PruebaBD = findViewById(R.id.btnTest);
        Confirmar = findViewById(R.id.btnLogin);
        DatosBD = findViewById(R.id.InfoDB);

        MostrarDatos = findViewById(R.id.CampoBD);

        Confirmar.setEnabled(false);
        conexionSQL = null;

        DatosBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DatosBD.isChecked()) {

                    MostrarDatos.setVisibility(View.VISIBLE);

                    txtIP.setText("");
                    txtPuerto.setText("");
                    txtBaseDatos.setText("");

                } else {
                    MostrarDatos.setVisibility(View.GONE);

                    txtIP.setText("");
                    txtPuerto.setText("");
                    txtBaseDatos.setText("");

                }
            }
        });

        PruebaBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IP = txtIP.getText().toString();
                Puerto = txtPuerto.getText().toString();
                BaseDatos = txtBaseDatos.getText().toString();
                Usuario = txtUsuario.getText().toString();
                Contra = txtContra.getText().toString();

                if (!Usuario.isEmpty() && !IP.isEmpty()) {
                    new ConexionBD().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Confirmando conexion a la base de datos", Toast.LENGTH_SHORT).show();
                new Confirmando().execute();
            }
        });
    }

    private class ConexionBD extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                Error = false;
                if (conexionSQL == null) {
                    DriverManager.setLoginTimeout(3);

                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }
            } catch (Exception e) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error al conectar", Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            if (conexionSQL == null) {


                runOnUiThread(new Runnable() {
                    public void run() {
                        Confirmar.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Error al conectar.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                conexionSQL = null;
                runOnUiThread(new Runnable() {
                    public void run() {
                        Confirmar.setEnabled(true);
                        Toast.makeText(getApplicationContext(), "Exito al conextar.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private class Confirmando extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                Error = false;


                if (conexionSQL == null) {
                    DriverManager.setLoginTimeout(4);

                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Confirmar.setEnabled(false);
                        Toast.makeText(getApplicationContext(), "Fallo al conectar", Toast.LENGTH_LONG).show();
                    }
                });

                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            conexionSQL = null;


            runOnUiThread(new Runnable() {
                public void run() {

                    Intent i = new Intent(PruebaBD.this, Loguearse.class);
                    Bundle b = new Bundle();

                    b.putString("IP", txtIP.getText().toString());
                    b.putString("Puerto", txtPuerto.getText().toString());
                    b.putString("BaseDatos", txtBaseDatos.getText().toString());
                    b.putString("Usuario", txtUsuario.getText().toString());
                    b.putString("Contra", txtContra.getText().toString());

                    i.putExtras(b);
                    Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_SHORT).show();

                    startActivity(i);
                }
            });
        }
    }


}
