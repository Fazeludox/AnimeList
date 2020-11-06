package com.example.myanimelist;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class Privilegios extends AppCompatActivity {

    private String Usuario, Contra, IP, Puerto, BaseDatos;

    private Connection conexionSQL;
    private String D1, D2, D3, D4;


    private String AD;
    private int nFilas;

    private Switch SWAnimeVistos;
    private LinearLayout MostrarAP, MostrarAU;

    private Switch SWAnimePendientes;
    private LinearLayout MostrarApE, MostrarApTA;

    private RadioGroup RadioSelect;
    private RadioButton chkAnimesVistos;
    private RadioButton chkAnimesPendientes;


    private String Anime, Puntuacion, EpisodioActualV, EpisodioUltimo;
    private String PAnime, EpisodioActualP, Temporada, Año;

    private EditText txtAV, txtPuntuacion, txtActual, txtEpUltimo;
    private EditText txtAP, txtPActual, txtTemporada, txtAno;
    private EditText txtFila;


    private Button Consultar, Insertar, Modificar, Eliminar;

    private Boolean ClaveDuplicada;


    private TextView Info1, Info2, Info3, Info4;
    private LinearLayout ListaDatos;

    private ListView ListadoDato;
    private AdaptadorAnimesVistos mAdaptador;
    private List<DatosAnimesVistos> DatosAnimesV;

    private AdaptadorAnimesPendientes mAdaptador2;
    private List<DatosAnimesPendientes> DatosAnimesP;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privilegios);

        Bundle bundle = getIntent().getExtras();

        Usuario = bundle.getString("Usuario");
        Contra = bundle.getString("Contra");
        IP = bundle.getString("IP");
        Puerto = bundle.getString("Puerto");
        BaseDatos = bundle.getString("BaseDatos");

        txtFila = findViewById(R.id.txtFila);

        ListadoDato = findViewById(R.id.ListaConsulta);

        SWAnimeVistos = findViewById(R.id.idAV);
        MostrarAP = findViewById(R.id.mostrarAP);
        txtAV = findViewById(R.id.txtAVisto);
        txtPuntuacion = findViewById(R.id.txtPuntacion);
        MostrarAU = findViewById(R.id.mostrarAU);
        txtActual = findViewById(R.id.txtActual);
        txtEpUltimo = findViewById(R.id.txtUltimo);


        SWAnimePendientes = findViewById(R.id.idAP);
        MostrarApE = findViewById(R.id.mostrarApE);
        txtAP = findViewById(R.id.txtCPAnime);
        txtPActual = findViewById(R.id.txtPActual);
        MostrarApTA = findViewById(R.id.mostrarApTA);
        txtTemporada = findViewById(R.id.txtTemporada);
        txtAno = findViewById(R.id.txtAño);


        RadioSelect = findViewById(R.id.RadioSelect);
        chkAnimesVistos = findViewById(R.id.chkAV);
        chkAnimesPendientes = findViewById(R.id.chkAP);

        Consultar = findViewById(R.id.btnConsulta);
        Insertar = findViewById(R.id.btnInsertar);
        Modificar = findViewById(R.id.btnModificar);
        Eliminar = findViewById(R.id.btnEliminar);

        mContext = this;

        Info1 = findViewById(R.id.Dato1);
        Info2 = findViewById(R.id.Dato2);
        Info3 = findViewById(R.id.Dato3);
        Info4 = findViewById(R.id.Dato4);

        Info1.setVisibility(View.GONE);
        Info2.setVisibility(View.GONE);
        Info3.setVisibility(View.GONE);
        Info4.setVisibility(View.GONE);

        Consultar.setEnabled(false);
        Eliminar.setEnabled(false);

        ListaDatos = findViewById(R.id.ListaDatos);

        SWAnimeVistos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SWAnimeVistos.isChecked()) {
                    MostrarAP.setVisibility(View.VISIBLE);
                    MostrarAU.setVisibility(View.VISIBLE);
                    txtAV.setText("");
                    txtPuntuacion.setText("");
                    txtActual.setText("");
                    txtEpUltimo.setText("");
                    conexionSQL = null;
                } else {
                    MostrarAP.setVisibility(View.GONE);
                    MostrarAU.setVisibility(View.GONE);
                    txtAV.setText("");
                    txtPuntuacion.setText("");
                    txtActual.setText("");
                    txtEpUltimo.setText("");
                    conexionSQL = null;
                }
            }
        });

        SWAnimePendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SWAnimePendientes.isChecked()) {
                    MostrarApE.setVisibility(View.VISIBLE);
                    MostrarApTA.setVisibility(View.VISIBLE);
                    txtAP.setText("");
                    txtPActual.setText("");
                    txtTemporada.setText("");
                    txtAno.setText("");
                    conexionSQL = null;
                } else {
                    MostrarApE.setVisibility(View.GONE);
                    MostrarApTA.setVisibility(View.GONE);
                    txtAP.setText("");
                    txtPActual.setText("");
                    txtTemporada.setText("");
                    txtAno.setText("");
                    conexionSQL = null;
                }
            }
        });


        RadioSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.chkAV) {
                    Toast.makeText(getApplicationContext(), "Animes Vistos", Toast.LENGTH_SHORT).show();
                    Consultar.setEnabled(true);
                    Eliminar.setEnabled(true);
                } else {
                    if (checkedId == R.id.chkAP) {
                        Toast.makeText(getApplicationContext(), "Animes Pendientes", Toast.LENGTH_SHORT).show();
                        Consultar.setEnabled(true);
                        Eliminar.setEnabled(true);
                    }
                }
            }
        });

        Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conexionSQL = null;

                int selectedId = RadioSelect.getCheckedRadioButtonId();


                D1 = Info1.getText().toString();
                D2 = Info2.getText().toString();
                D3 = Info3.getText().toString();
                D4 = Info4.getText().toString();

                Info1.setVisibility(View.GONE);
                Info2.setVisibility(View.GONE);
                Info3.setVisibility(View.GONE);
                Info4.setVisibility(View.GONE);

                ListadoDato.setAdapter(null);


                if (txtFila.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), "La fila no es valida", Toast.LENGTH_SHORT).show();

                } else {

                    nFilas = Integer.parseInt(txtFila.getText().toString());

                    if (selectedId == chkAnimesVistos.getId()) {
                        ListadoDato.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Consultando los animes vistos", Toast.LENGTH_SHORT).show();
                        new ConsultaRellenarAnimesVistos().execute();
                        new ConsultaAnimesVistos().execute();
                    } else {
                        if (selectedId == chkAnimesPendientes.getId()) {
                            ListadoDato.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Consultando los animes pendientes", Toast.LENGTH_SHORT).show();
                            new ConsultaRellenarAnimesPendientes().execute();
                            new ConsultaAnimesPendientes().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Seleccione una tabla para consultar los datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        Insertar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClaveDuplicada = false;
                conexionSQL = null;

                if (!SWAnimeVistos.isChecked() && !SWAnimePendientes.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Seleccione una tabla y rellene los datos", Toast.LENGTH_SHORT).show();
                } else {

                    if (SWAnimeVistos.isChecked()) {

                        Anime = txtAV.getText().toString();
                        Puntuacion = txtPuntuacion.getText().toString();
                        EpisodioActualV = txtActual.getText().toString();
                        EpisodioUltimo = txtEpUltimo.getText().toString();

                        if (!Anime.isEmpty() && !Puntuacion.isEmpty() && !EpisodioActualV.isEmpty() && !EpisodioUltimo.isEmpty()) {

                            new InsertarAnimesVistos().execute();

                        } else {
                            Toast.makeText(getApplicationContext(), "Rellena todos los datos de los Animes vistos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (SWAnimePendientes.isChecked()) {

                        PAnime = txtAP.getText().toString();
                        EpisodioActualP = txtPActual.getText().toString();
                        Temporada = txtTemporada.getText().toString();
                        Año = txtAno.getText().toString();

                        if (!PAnime.isEmpty() && !EpisodioActualP.isEmpty() && !Temporada.isEmpty() && !Año.isEmpty()) {
                            new InsertarAnimesPendientes().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Rellena todos los datos de los Animes pendientes", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });


        Modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClaveDuplicada = false;
                conexionSQL = null;

                if (!SWAnimeVistos.isChecked() && !SWAnimePendientes.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Seleccione alguna tabla y rellene los datos", Toast.LENGTH_SHORT).show();
                } else {

                    nFilas = Integer.parseInt(txtFila.getText().toString());

                    if (SWAnimeVistos.isChecked()) {

                        Anime = txtAV.getText().toString();
                        Puntuacion = txtPuntuacion.getText().toString();
                        EpisodioActualV = txtActual.getText().toString();
                        EpisodioUltimo = txtEpUltimo.getText().toString();

                        if (!Anime.isEmpty() && !Puntuacion.isEmpty() && !EpisodioActualV.isEmpty() && !EpisodioUltimo.isEmpty()) {

                            new ModificarAnimesVistos().execute();

                        } else {
                            Toast.makeText(getApplicationContext(), "Rellena todos los datos de los Animes vistos", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (SWAnimePendientes.isChecked()) {

                        PAnime = txtAP.getText().toString();
                        EpisodioActualP = txtPActual.getText().toString();
                        Temporada = txtTemporada.getText().toString();
                        Año = txtAno.getText().toString();

                        if (!PAnime.isEmpty() && !EpisodioActualP.isEmpty() && !Temporada.isEmpty() && !Año.isEmpty()) {
                            new ModificarAnimesPendientes().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "Rellena todos los datos de los Animes vistos", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        });


        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conexionSQL = null;

                int selectedId = RadioSelect.getCheckedRadioButtonId();

                if (txtFila.getText().toString().isEmpty()) {

                    Toast.makeText(getApplicationContext(), "La fila no es valida", Toast.LENGTH_SHORT).show();

                } else {

                    nFilas = Integer.parseInt(txtFila.getText().toString());
                    if (selectedId == chkAnimesVistos.getId()) {
                        Toast.makeText(getApplicationContext(), "Eliminando datos de Animes Visots", Toast.LENGTH_SHORT).show();
                        new EliminarAnimesVistos().execute();
                    } else {
                        if (selectedId == chkAnimesPendientes.getId()) {
                            Toast.makeText(getApplicationContext(), "Eliminando datos de Animes Pendientes", Toast.LENGTH_SHORT).show();
                            new EliminarAnimesPendientes().execute();
                        } else {

                            Toast.makeText(getApplicationContext(), "Seleccione una tabla para eliminar los datos", Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }
        });

    }


    /*Consultas*/

    private class ConsultaAnimesVistos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {

                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesVisto";

                int i = 0;
                int Existe = 0;

                ResultSet rs = st.executeQuery(SQL);

                while (rs.next()) {
                    if ((i + 1) == nFilas) {
                        Existe = 1;

                        D1 = rs.getString("Anime");
                        D2 = rs.getString("Puntuacion");
                        D3 = rs.getString("EpiActual");
                        D4 = rs.getString("EpiUltimo");

                    }
                    i = i + 1;
                }

                if (Existe == 0) {
                    D1 = "No existe la fila";
                    D2 = "No existe la fila";
                    D3 = "No existe la fila";
                    D4 = "No existe la fila";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Info1.setVisibility(View.VISIBLE);
            Info2.setVisibility(View.VISIBLE);
            Info3.setVisibility(View.VISIBLE);
            Info4.setVisibility(View.VISIBLE);

            Info1.setText("Anime: " + D1);
            Info2.setText("Puntacion: " + D2);
            Info3.setText("Episodio Actual: " + D3);
            Info4.setText("Ultimo Episodio: " + D4);

        }
    }

    private class ConsultaAnimesPendientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesPendientes";

                int i = 0;
                int Existe = 0;

                ResultSet rs = st.executeQuery(SQL);

                while (rs.next()) {
                    if ((i + 1) == nFilas) {
                        Existe = 1;

                        D1 = rs.getString("Anime");
                        D2 = rs.getString("EpiActual");
                        D3 = rs.getString("Temporada");
                        D4 = rs.getString("Año");

                    }
                    i = i + 1;
                }

                if (Existe == 0) {
                    D1 = "No existe la fila";
                    D2 = "No existe la fila";
                    D3 = "No existe la fila";
                    D4 = "No existe la fila";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Info1.setVisibility(View.VISIBLE);
            Info2.setVisibility(View.VISIBLE);
            Info3.setVisibility(View.VISIBLE);
            Info4.setVisibility(View.VISIBLE);

            Info1.setText("Anime: " + D1);
            Info2.setText("Episodio Actual: " + D2);
            Info3.setText("Temporada: " + D3);
            Info4.setText("Año: " + D4);

        }
    }


    /*Insertar*/


    private class InsertarAnimesVistos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesVisto WHERE Anime = '" + Anime + "'";
                ResultSet rs = st.executeQuery(SQL);

                ClaveDuplicada = rs.next();


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (ClaveDuplicada) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "El anime ya existe", Toast.LENGTH_LONG).show();
                        }
                    });

                    conexionSQL.close();

                } else {

                    Statement st = conexionSQL.createStatement();
                    String SQL = "INSERT INTO AnimesVisto (Anime, Puntuacion, EpiActual, EpiUltimo) VALUES('" + Anime + "', '" + Puntuacion + "', '" + EpisodioActualV + "', '" + EpisodioUltimo + "')";
                    st.execute(SQL);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Datos insertados.", Toast.LENGTH_LONG).show();
                        }
                    });

                    conexionSQL.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    private class InsertarAnimesPendientes extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesPendientes WHERE Anime = '" + PAnime + "'";
                ResultSet rs = st.executeQuery(SQL);

                ClaveDuplicada = rs.next();


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (ClaveDuplicada) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "El anime ya existe", Toast.LENGTH_LONG).show();
                        }
                    });

                    conexionSQL.close();

                } else {

                    Statement st = conexionSQL.createStatement();
                    String SQL = "INSERT INTO AnimesPendientes (Anime, EpiActual, Temporada, Año) VALUES('" + PAnime + "', '" + EpisodioActualP + "', '" + Temporada + "', '" + Año + "')";
                    st.execute(SQL);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Datos insertados.", Toast.LENGTH_LONG).show();
                        }
                    });

                    conexionSQL.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }


    /*Modificar*/

    private class ModificarAnimesVistos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesVisto WHERE Anime = '" + Anime + "'";
                ResultSet rs = st.executeQuery(SQL);

                ClaveDuplicada = rs.next();


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (ClaveDuplicada) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "El anime ya existe", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    if (conexionSQL == null) {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                    }

                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT * FROM AnimesVisto";

                    ResultSet rs = st.executeQuery(SQL);

                    int i;
                    i = 0;

                    while (i < nFilas) {
                        rs.next();
                        i++;
                    }

                    if (nFilas == i) {
                        D1 = rs.getString("Anime");

                        String SQL2 = "UPDATE AnimesVisto SET Anime = '" + Anime + "' , Puntuacion = '" + Puntuacion + "' , EpiActual = '" + EpisodioActualV + "' , EpiUltimo = '" + EpisodioUltimo + "' WHERE Anime = '" + D1 + "'";
                        st.execute(SQL2);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Se actualizo la " + nFilas + " con el anime " + D1 + " por  " + Anime, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class ModificarAnimesPendientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesPendientes WHERE Anime = '" + Anime + "'";
                ResultSet rs = st.executeQuery(SQL);

                ClaveDuplicada = rs.next();


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                if (ClaveDuplicada) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "El anime ya existe", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    if (conexionSQL == null) {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                    }

                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT * FROM AnimesPendientes";

                    ResultSet rs = st.executeQuery(SQL);

                    int i;
                    i = 0;

                    while (i < nFilas) {
                        rs.next();
                        i++;
                    }

                    if (nFilas == i) {
                        D1 = rs.getString("Anime");

                        String SQL2 = "UPDATE AnimesPendientes SET Anime = '" + PAnime + "' , EpiActual = '" + EpisodioActualP + "' , Temporada = '" + Temporada + "' , Año = '" + Año + "' WHERE Anime = '" + D1 + "'";
                        st.execute(SQL2);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Se actualizo la " + nFilas + " con el anime " + D1 + " por " + PAnime, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*Eliminar*/

    private class EliminarAnimesVistos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesVisto";

                ResultSet rs = st.executeQuery(SQL);

                int i;
                i = 0;

                while (i < nFilas) {
                    rs.next();
                    i++;
                }

                if (nFilas == i) {
                    D1 = rs.getString("Anime");

                    String SQL2 = "DELETE FROM AnimesVisto WHERE Anime= '" + D1 + "'";
                    st.execute(SQL2);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Se elimino la fila " + nFilas + " con el personaje " + D1, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
            }


            return null;
        }
    }

    private class EliminarAnimesPendientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesPendientes";

                ResultSet rs = st.executeQuery(SQL);

                int i;
                i = 0;

                while (i < nFilas) {
                    rs.next();
                    i++;
                }

                if (nFilas == i) {
                    D1 = rs.getString("Anime");

                    String SQL2 = "DELETE FROM AnimesPendientes WHERE Anime= '" + D1 + "'";
                    st.execute(SQL2);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Se elimino la fila " + nFilas + " con el anime " + D1, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } catch (Exception e) {
            }


            return null;
        }
    }

    /*ListView*/

    private class AdaptadorAnimesVistos extends ArrayAdapter<DatosAnimesVistos> {
        public AdaptadorAnimesVistos(Context context, int textViewResourceId, List<DatosAnimesVistos> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            return getViewOptimize(pos, convertView, parent);
        }

        public View getViewOptimize(int pos, View convertView, ViewGroup parent) {
            AdaptadorAnimesVistos.ViewHolder viewHolder = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.mostrar_datos_animes_vistos, null);
                viewHolder = new AdaptadorAnimesVistos.ViewHolder();


                viewHolder.mostrarAV = convertView.findViewById(R.id.mostrarAnimeVisto);
                viewHolder.mostrarP = convertView.findViewById(R.id.mostrarPuntuacion);
                viewHolder.mostrarEPA = convertView.findViewById(R.id.mostrarEpiActual);
                viewHolder.mostrarEPU = convertView.findViewById(R.id.mostrarEpiUltimo);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (AdaptadorAnimesVistos.ViewHolder) convertView.getTag();
            }

            DatosAnimesVistos Valor = getItem(pos);

            viewHolder.mostrarAV.setText(Valor.getAnimeVisto());
            viewHolder.mostrarP.setText(Valor.getPuntuacion());
            viewHolder.mostrarEPA.setText(Valor.getEpisodioActual());
            viewHolder.mostrarEPU.setText(Valor.getEpisodioUltimo());

            return convertView;
        }

        private class ViewHolder {
            public TextView mostrarAV;
            public TextView mostrarP;
            public TextView mostrarEPA;
            public TextView mostrarEPU;

        }
    }

    private class ConsultaRellenarAnimesVistos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            mAdaptador = null;
            DatosAnimesV = null;

            try {
                if (DatosAnimesV == null) {
                    DatosAnimesV = new LinkedList<DatosAnimesVistos>();

                    if (conexionSQL == null) {
                        DriverManager.setLoginTimeout(4);

                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);

                    }
                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT * FROM AnimesVisto";

                    ResultSet rs = st.executeQuery(SQL);
                    while (rs.next()) {
                        DatosAnimesVistos AnimeVistosL = new DatosAnimesVistos();

                        AnimeVistosL.setAnimeVisto("Anime: " + rs.getString("Anime"));
                        AnimeVistosL.setPuntuacion("Puntuacion: " + rs.getString("Puntuacion"));
                        AnimeVistosL.setEpisodioActual("Episodio Actual: " + rs.getString("EpiActual"));
                        AnimeVistosL.setEpisodioUltimo("Ultimo Episodio: " + rs.getString("EpiUltimo"));


                        DatosAnimesV.add(AnimeVistosL);
                    }
                }

            } catch (Exception e) {
                D1 = "Fallo en conexion a BD";
                D2 = "Fallo en conexion a BD";
                D3 = "Fallo en conexion a BD";
                D4 = "Fallo en conexion a BD";
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            mAdaptador = new AdaptadorAnimesVistos(mContext, R.layout.mostrar_datos_animes_vistos, DatosAnimesV);
            ListadoDato.setAdapter(mAdaptador);

        }
    }


    private class AdaptadorAnimesPendientes extends ArrayAdapter<DatosAnimesPendientes> {
        public AdaptadorAnimesPendientes(Context context, int textViewResourceId, List<DatosAnimesPendientes> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            return getViewOptimize(pos, convertView, parent);
        }

        public View getViewOptimize(int pos, View convertView, ViewGroup parent) {
            AdaptadorAnimesPendientes.ViewHolder viewHolder = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.mostrar_datos_animes_pendientes, null);
                viewHolder = new AdaptadorAnimesPendientes.ViewHolder();

                viewHolder.mostrarAP = convertView.findViewById(R.id.mostrarAnimePendiente);
                viewHolder.mostrarEPA = convertView.findViewById(R.id.mostrarAPActual);
                viewHolder.mostraT = convertView.findViewById(R.id.mostrarTemporada);
                viewHolder.mostrarY = convertView.findViewById(R.id.mostrarAño);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (AdaptadorAnimesPendientes.ViewHolder) convertView.getTag();
            }

            DatosAnimesPendientes Valor = getItem(pos);
            viewHolder.mostrarAP.setText(Valor.getAnimePendiente());
            viewHolder.mostrarEPA.setText(Valor.getEpisodioActual());
            viewHolder.mostraT.setText(Valor.getTemporada());
            viewHolder.mostrarY.setText(Valor.getAño());


            return convertView;
        }

        private class ViewHolder {
            public TextView mostrarAP;
            public TextView mostrarEPA;
            public TextView mostraT;
            public TextView mostrarY;

        }
    }

    private class ConsultaRellenarAnimesPendientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            mAdaptador2 = null;
            DatosAnimesP = null;

            try {
                if (DatosAnimesP == null) {
                    DatosAnimesP = new LinkedList<DatosAnimesPendientes>();

                    if (conexionSQL == null) {
                        DriverManager.setLoginTimeout(4);

                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);

                    }
                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT * FROM AnimesPendientes";

                    ResultSet rs = st.executeQuery(SQL);
                    while (rs.next()) {
                        DatosAnimesPendientes AnimePendientesL = new DatosAnimesPendientes();


                        AnimePendientesL.setAnimePendiente("Anime: " + rs.getString("Anime"));
                        AnimePendientesL.setEpisodioActual("Episodio Actual: " + rs.getString("EpiActual"));
                        AnimePendientesL.setTemporada("Temporada: " + rs.getString("Temporada"));
                        AnimePendientesL.setAño("Año: " + rs.getString("Año"));


                        DatosAnimesP.add(AnimePendientesL);
                    }
                }

            } catch (Exception e) {
                D1 = "Fallo en conexion a BD";
                D2 = "Fallo en conexion a BD";
                D3 = "Fallo en conexion a BD";
                D4 = "Fallo en conexion a BD";
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            mAdaptador2 = new AdaptadorAnimesPendientes(mContext, R.layout.mostrar_datos_animes_pendientes, DatosAnimesP);
            ListadoDato.setAdapter(mAdaptador2);

        }
    }


}
